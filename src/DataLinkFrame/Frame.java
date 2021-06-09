package src.DataLinkFrame;

import java.util.List;

public final class Frame {
   public final Bytes flag;
   public final Bytes data;

   public Frame(String flag, String data) {
      this.flag = Bytes.convert(flag);
      this.data = Frame.addToData(Bytes.convert(data));
   }

   public static Bytes addToData(Bytes data) {
      Bytes updatedData = new Bytes(List.of());
      int oneCount = 0;
      for (boolean bit : data) {
         updatedData.add(bit);
         if (bit)
            oneCount += 1;
         else
            oneCount = 0;
         if (oneCount == 5) {
            updatedData.add(false);
            oneCount = 0;
         }
      }
      return updatedData;
   }

   @Override
   public String toString() {
      return flag.toString() + data.toString();
   }

}
