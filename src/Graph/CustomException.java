package src.Graph;

import java.util.NoSuchElementException;

public final class CustomException extends NoSuchElementException {
   private final String title;

   public CustomException(String title, String msg) {
      super(msg);
      this.title = title;
   }

   @Override
   public String toString() {
      return title + getMessage();
   }
}
