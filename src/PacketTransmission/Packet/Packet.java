package src.PacketTransmission.Packet;

public class Packet extends PacketFrame {
   public final PacketType type;

   public Packet(int packetNum, boolean message, PacketType packetType) {
      super(packetNum, message);
      this.type = packetType;
   }

   public Packet(PacketFrame frame, PacketType packetType) {
      super(frame);
      this.type = packetType;
   }

   public String toString() {
      String str = this.type + " Packet Number ( " + this.num + " )";
      if (this.type == PacketType.DATA)
         str += " with Message :\t" + this.message;
      return str;
   }

   public static Packet createData(PacketFrame frame) {
      return new Packet(frame, PacketType.DATA);
   }

   public static Packet createAck(int frameNum) {
      return new Packet(new PacketFrame(frameNum, null), PacketType.ACK);
   }

   public static Packet createNak(int frame) {
      return new Packet(new PacketFrame(frame, null), PacketType.NAK);
   }
}
