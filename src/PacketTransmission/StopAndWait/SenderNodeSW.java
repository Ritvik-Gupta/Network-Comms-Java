package src.PacketTransmission.StopAndWait;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.TimedPacket;
import src.PacketTransmission.TransmissionEntity.SenderNode;

public final class SenderNodeSW extends SenderNode {
   public SenderNodeSW(Boolean[] frames) {
      super(frames, 1);
   }

   public synchronized void setAck(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         this.writeLog("Packet Received =>\t", packet);
         this.windowFirst = (this.windowFirst + 1) % this.frames.length;
         this.frameResendTime = TimedPacket.getEpochSec();
         if (packet.num == this.frames.length - 1)
            this.isNodeOpen = false;
      } else
         this.writeLog("Packet Denied =>\t", packet);
   }
}
