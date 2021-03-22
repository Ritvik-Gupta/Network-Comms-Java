package src.ParityChecker;

import java.util.Arrays;

public enum BitType {
   ONE('1', 1), ZERO('0', 0);

   public final char code;
   public final int value;

   private BitType(char bitCode, int value) {
      this.code = bitCode;
      this.value = value;
   }

   public static BitType flip(BitType bit) {
      return bit == BitType.ONE ? BitType.ZERO : BitType.ONE;
   }

   public static BitType parseBit(char bitCode) throws Exception {
      for (BitType bit : Arrays.asList(BitType.values()))
         if (bit.code == bitCode)
            return bit;
      throw new Exception("Supported Bit Codes are '1' and '0' only");
   }
}
