package services.Console;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class Console {
   private static void printAny(Object arg, boolean nextLine) {
      if (nextLine)
         System.out.println(arg);
      else
         System.out.print(arg);
   }

   public static void print(String str, Object... args) {
      Console.printAny(String.format(str, args), false);
   }

   public static void log(Object... args) {
      String str = "";
      for (Object arg : args)
         str += arg == null ? arg : arg.toString();
      Console.printAny(str, false);
   }

   public static <T> void println(T[] arr) {
      Console.print("Array Length :\t%d\n", arr.length);
      for (int pos = 0; pos < arr.length; ++pos) {
         T elm = arr[pos];
         Console.print("\t( %d )\t->\t%s\n", pos, elm == null ? elm : elm.toString());
      }
   }

   public static <T> void println(Collection<T> collection) {
      Iterator<T> itr = collection.iterator();
      int pos = 0;
      Console.print("Collection Size :\t%d\n", collection.size());
      while (itr.hasNext())
         Console.print("\t( %d )\t->\t%s\n", pos++, itr.next().toString());
   }

   public static <T, U> void println(Map<T, U> map) {
      Console.print("Map Size :\t%d\n", map.size());
      for (Entry<T, U> entry : map.entrySet()) {
         Console.print("\t");
         Console.println(entry);
      }
   }

   public static <T, U> void println(Entry<T, U> entry) {
      Console.print("%s\t->\t%s\n", entry.getKey().toString(), entry.getValue().toString());
   }
}
