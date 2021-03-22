package src.PacketTransmission.TransmissionEntity;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.PacketFrame;

public abstract class ReceiverNode extends TransmissionEntity {
   protected final PacketFrame[] frames;
   protected TransmissionLayer layer;
   protected final int windowSize;
   protected int windowFirst;

   protected ReceiverNode(int frameSize, int windowSize) {
      super("Receiver Node");

      this.frames = new PacketFrame[frameSize];

      this.windowFirst = 0;
      this.windowSize = windowSize;
   }

   public abstract void setPacket(Packet packet);

   public void withTransmission(TransmissionLayer transmission) {
      this.layer = transmission;
   }

   protected boolean isInsideWindow(int packetNum) {
      return packetNum >= this.windowFirst && packetNum < this.windowFirst + this.windowSize;
   }

   public Boolean[] getFrames() {
      Boolean[] frames = new Boolean[this.frames.length];
      for (int pos = 0; pos < this.frames.length; ++pos)
         frames[pos] = this.frames[pos].message;
      return frames;
   }
}
