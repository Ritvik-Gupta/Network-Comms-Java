package src.PacketTransmission.Packet;

public class PacketFrame {
   public final int num;
   public final Boolean message;

   public PacketFrame(int packetNum, Boolean message) {
      this.num = packetNum;
      this.message = message;
   }

   public PacketFrame(PacketFrame frame) {
      this.num = frame.num;
      this.message = frame.message;
   }
}
