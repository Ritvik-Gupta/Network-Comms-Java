package src.CSMA_CA.Packet;

public class Packet extends PacketFrame {
   public final PacketType type;

   public Packet(int packetNum, boolean message, int senderId, PacketType packetType) {
      super(packetNum, message, senderId);
      this.type = packetType;
   }

   public Packet(PacketFrame frame, PacketType packetType) {
      super(frame);
      this.type = packetType;
   }

   public String toString() {
      String str = this.type + " Packet Number ( " + this.num + " )";
      if (this.type == PacketType.DATA)
         str += " by Sender < " + this.senderId + " > with Message : " + this.message;
      return str;
   }

   public static Packet createRTS(PacketFrame frame) {
      return new Packet(frame, PacketType.DATA);
   }

   public static Packet createCTS(int frameNum, int senderId) {
      return new Packet(new PacketFrame(frameNum, null, senderId), PacketType.ACK);
   }
}
