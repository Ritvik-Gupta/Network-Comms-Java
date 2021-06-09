package src.DataLinkFrame;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ranges.RangeException;

public final class Bytes extends ArrayList<Boolean> {
   public Bytes(List<Boolean> bits) {
      super(bits);
   }

   @Override
   public String toString() {
      return this.stream().reduce("", (a, b) -> a + (b ? 1 : 0), String::join);
   }

   public static Bytes convert(String message) {
      ArrayList<Boolean> bytes = new ArrayList<>();
      for (int pos = 0; pos < message.length(); ++pos) {
         if (message.charAt(pos) == '1')
            bytes.add(true);
         else if (message.charAt(pos) == '0')
            bytes.add(false);
         else
            throw new RangeException((short) -1, "Bytes cannot contain anything other than 0 or 1");
      }
      return new Bytes(bytes);
   }
}
