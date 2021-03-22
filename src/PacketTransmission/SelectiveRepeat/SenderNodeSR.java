package src.PacketTransmission.SelectiveRepeat;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.TimedPacket;
import src.PacketTransmission.TransmissionEntity.SenderNode;

public final class SenderNodeSR extends SenderNode {
   public SenderNodeSR(Boolean[] frames, int windowSize) {
      super(frames, windowSize);
   }

   public synchronized void setAck(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         this.writeLog("Packet Received =>\t", packet);
         switch (packet.type) {
         case ACK:
            this.windowFirst = packet.num + 1;
            this.frameResendTime = TimedPacket.getEpochSec();
            if (packet.num == this.frames.length - 1)
               this.isNodeOpen = false;
            break;

         case NAK:
            this.writeLog("Resent Frame :\t", packet.num);
            this.frameResendTime = TimedPacket.getEpochSec() + 3;
            this.layer.transmit(Packet.createData(this.frames[packet.num]));
            break;

         default:
            break;
         }
      } else
         this.writeLog("Packet Denied =>\t", packet);
   }
}
