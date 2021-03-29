package src.CSMA_CA.Packet;

public class PacketFrame {
   public final int num;
   public final Boolean message;
   public final int senderId;

   public PacketFrame(int packetNum, Boolean message, int senderId) {
      this.num = packetNum;
      this.message = message;
      this.senderId = senderId;
   }

   public PacketFrame(PacketFrame frame) {
      this.num = frame.num;
      this.message = frame.message;
      this.senderId = frame.senderId;
   }
}
