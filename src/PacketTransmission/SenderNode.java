package src.PacketTransmission;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.PacketFrame;

public abstract class SenderNode implements Runnable {
   protected boolean isNodeOpen;
   protected final PacketFrame[] frames;
   protected TransmissionLayer layer;

   protected SenderNode(Boolean[] frames) {
      this.isNodeOpen = true;
      this.frames = new PacketFrame[frames.length];
      for (int pos = 0; pos < frames.length; ++pos)
         this.frames[pos] = new PacketFrame(pos, frames[pos]);
   }

   public abstract void setAck(Packet packet);

   public void withTransmission(TransmissionLayer transmission) {
      this.layer = transmission;
   }
}
