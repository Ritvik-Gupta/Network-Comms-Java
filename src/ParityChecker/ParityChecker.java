package src.ParityChecker;

import java.util.ArrayList;

//* Parity Checker Class to create an Instance of a Node and Generate Random Noise
public final class ParityChecker {
   //? Transmission Packet sent or received depending on the Node Instance
   protected final ArrayList<BitType> transmission;
   //? Parity Type ( ODD or EVEN )
   protected final ParityType parity;
   //? Parity Bit of the Transmission Packet, calculated dynamically based on the Parity Type
   protected BitType parityBit;

   public ParityChecker(String unparsedTransmission, ParityType parity) throws Exception {
      this.transmission = this.parseBits(unparsedTransmission);
      this.parity = parity;
      this.parityBit = BitType.ZERO;
      this.parityBit = this.calculateParityBit();
   }

   //* Override Constrcutor to create a Receiver Node Instance
   private ParityChecker(ArrayList<BitType> transmission, ParityType parity, BitType parityBit) {
      this.transmission = transmission;
      this.parity = parity;
      this.parityBit = parityBit;
   }

   //* Parses 1s and 0s to an Array of Bit Types
   private ArrayList<BitType> parseBits(String receivedTransmission) throws Exception {
      ArrayList<BitType> transmission = new ArrayList<>();
      for (int pos = 0; pos < receivedTransmission.length(); ++pos)
         transmission.add(BitType.parseBit(receivedTransmission.charAt(pos)));
      return transmission;
   }

   //* Word Representation of the Current Transmission state of Node Instance
   public String transmissionInfo() {
      return "transmission : " + this.transmission.toString() + ", Parity Bit : " + this.parityBit;
   }

   //* Total number of 1s in the Transmission Packet
   private int calculateTotalOnes() {
      int totalOnes = 0;
      for (BitType bit : this.transmission)
         totalOnes += bit.value;
      totalOnes += this.parityBit.value;
      return totalOnes;
   }

   //* If Transmission Packet is Valid then add 0, else 1 at the end
   private BitType calculateParityBit() {
      return this.isValidParity() ? BitType.ZERO : BitType.ONE;
   }

   //* If Parity is Even and Number of 1s are also even (or both are odd) then it is valid
   public boolean isValidParity() {
      boolean hasEvenOnes = this.calculateTotalOnes() % 2 == 0;
      return this.parity.isEven() == hasEvenOnes;
   }

   //* Generates Random Noise based on how high the Noise Factor is (flips the bit is true)
   public static ParityChecker generateRandomNoise(
      ParityChecker senderNode, 
      double noiseFactor
   ) throws Exception {
      if (noiseFactor > 0.5 || noiseFactor < 0)
         throw new Exception("Noise Factor must be between 0 and 0.5");

      ArrayList<BitType> transmission = new ArrayList<>();
      senderNode.transmission.forEach(bit -> {
         transmission.add(Math.random() < noiseFactor ? BitType.flip(bit) : bit);
      });
      BitType parityBit = senderNode.parityBit;
      if (Math.random() < noiseFactor)
         parityBit = BitType.flip(parityBit);
      return new ParityChecker(transmission, senderNode.parity, parityBit);
   }

   //* Calculate Total faults that the Packet suffered. Used only for Testing.
   public static int calculateTotalFaults(
      ParityChecker senderNode, 
      ParityChecker receiverNode
   ) throws Exception {
      if (senderNode.transmission.size() != receiverNode.transmission.size())
         throw new Exception();

      int totalFaults = 0;
      for (int pos = 0; pos < senderNode.transmission.size(); ++pos)
         if (senderNode.transmission.get(pos) != receiverNode.transmission.get(pos))
            ++totalFaults;
      if (senderNode.parityBit != receiverNode.parityBit)
         ++totalFaults;
      return totalFaults;
   }
}
