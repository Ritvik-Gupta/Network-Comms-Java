package src.Hamming;

import java.util.List;

public final class Main {
   public static void main(String[] args) throws Exception {
      HammingCode senderNode = new HammingCode("1001101");
      System.out.println("\nSender Node Before Transmission\n");
      senderNode.print();

      HammingCode receiverNode = senderNode.manualNoise(List.of(11));
      System.out.println("\nReceiver Node After Transmission\n");
      receiverNode.print();

      Integer correctionPos = receiverNode.errorCorrection();
      if (correctionPos == null)
         System.out.println("\nNo Error was Found\n");
      else
         System.out.println("\nCorrection done at Position :\t" + correctionPos + "\n");
      System.out.println("\nReceiver Node After Error Correction\n");
      receiverNode.print();

      System.out.println();
   }
}
