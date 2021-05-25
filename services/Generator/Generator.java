package services.Generator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Generator<YieldType, ReturnType> implements Iterable<YieldType> {
   private static final ThreadGroup generatorThreadGroup = new ThreadGroup("Generator Thread Group");

   private static final class FetchCondition {
      private boolean isActive;

      public synchronized void advance() {
         isActive = true;
         notify();
      }

      public synchronized void await() throws InterruptedException {
         try {
            if (isActive)
               return;
            wait();
         } finally {
            isActive = false;
         }
      }
   }

   private Thread producer;
   private YieldType nextItem;
   private boolean hasFinished;
   private ReturnType returnValue;
   private boolean isNextItemAvailable;
   private RuntimeException exceptionRaisedByProducer;
   private final FetchCondition itemRequested = new FetchCondition();
   private final FetchCondition itemAvailableOrHasFinished = new FetchCondition();

   public ReturnType fetchReturn() {
      if (returnValue == null)
         throw new NoSuchElementException();
      return returnValue;
   }

   @Override
   public Iterator<YieldType> iterator() {
      return new Iterator<>() {
         @Override
         public boolean hasNext() {
            return searchForNext();
         }

         @Override
         public YieldType next() {
            if (!searchForNext())
               throw new NoSuchElementException();
            isNextItemAvailable = false;
            return nextItem;
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }

         private boolean searchForNext() {
            if (isNextItemAvailable)
               return true;
            if (hasFinished)
               return false;
            if (producer == null)
               startProducer();
            itemRequested.advance();
            try {
               itemAvailableOrHasFinished.await();
            } catch (InterruptedException err) {
               hasFinished = true;
            }
            if (exceptionRaisedByProducer != null)
               throw exceptionRaisedByProducer;
            return !hasFinished;
         }
      };
   }

   protected abstract ReturnType generate() throws InterruptedException;

   protected void yieldNext(YieldType element) throws InterruptedException {
      nextItem = element;
      isNextItemAvailable = true;
      itemAvailableOrHasFinished.advance();
      itemRequested.await();
   }

   private void startProducer() {
      assert producer == null;
      producer = new Thread(generatorThreadGroup, new Runnable() {
         @Override
         public void run() {
            try {
               itemRequested.await();
               returnValue = generate();
            } catch (InterruptedException err) {
            } catch (RuntimeException err) {
               exceptionRaisedByProducer = err;
            }
            hasFinished = true;
            itemAvailableOrHasFinished.advance();
         }
      });
      producer.setDaemon(true);
      producer.start();
   }
}
