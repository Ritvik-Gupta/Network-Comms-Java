package src.PacketTransmission;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.PacketFrame;

public abstract class ReceiverNode {
   protected boolean isNodeOpen;
   protected final PacketFrame[] frames;
   protected TransmissionLayer layer;

   protected ReceiverNode(int frameSize) {
      this.isNodeOpen = true;
      this.frames = new PacketFrame[frameSize];
   }

   public abstract void setPacket(Packet packet);

   public void withTransmission(TransmissionLayer transmission) {
      this.layer = transmission;
   }

   public Boolean[] getFrames() {
      Boolean[] frames = new Boolean[this.frames.length];
      for (int pos = 0; pos < this.frames.length; ++pos)
         frames[pos] = this.frames[pos].message;
      return frames;
   }
}
