package src.PacketTransmission.GoBackN;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.TimedPacket;
import src.PacketTransmission.TransmissionEntity.SenderNode;

public final class SenderNodeGB extends SenderNode {
   public SenderNodeGB(Boolean[] frames, int windowSize) {
      super(frames, windowSize);
   }

   public synchronized void setAck(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         this.writeLog("Packet Received =>\t", packet);
         this.windowFirst = packet.num + 1;
         this.frameResendTime = TimedPacket.getEpochSec();
         if (packet.num == this.frames.length - 1)
            this.isNodeOpen = false;
      } else
         this.writeLog("Packet Denied =>\t", packet);
   }
}
