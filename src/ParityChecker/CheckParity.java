package src.ParityChecker;

import java.util.Scanner;

public final class CheckParity {
   private static final Scanner S = new Scanner(System.in);

   private static ParityType promptParity() {
      System.out.print("Is the Parity Type an Even Parity ?: (y/n)\t");
      while (true) {
         String input = S.next();
         if (input.equalsIgnoreCase("y"))
            return ParityType.EVEN;
         if (input.equalsIgnoreCase("n"))
            return ParityType.ODD;
         System.out.print("?: (y/n)\t");
      }
   }

   public static void main(String[] args) throws Exception {
      for (String unparsedTransmission : args) {
         System.out.println("\nTransmission Bits :\t" + unparsedTransmission + "\n");
         ParityType parity = promptParity();

         ParityChecker senderNode = new ParityChecker(unparsedTransmission, parity);
         System.out.println("\nSender Node Transmission Bits :\n\t" + senderNode.transmissionInfo());

         System.out.print("\nEnter a valid Noise Factor -> [0, 0.5] :\t");
         double noiseFactor = S.nextDouble();

         ParityChecker receiverNode = ParityChecker.generateRandomNoise(senderNode, noiseFactor);
         boolean isValidParity = receiverNode.isValidParity();
         System.out.println("\nReceiver Node Transmission Bits :\n\t" + receiverNode.transmissionInfo());
         if (isValidParity)
            System.out.println("\nMessage Received is Valid or has Even Number of Error Bits");
         else
            System.out.println("\nMessage Received is Invalid. Error Detected");
      }
   }
}
