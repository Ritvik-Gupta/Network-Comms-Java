package src.PacketTransmission.SelectiveRepeat;

import services.Console.Console;
import src.PacketTransmission.SenderNode;
import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.TimedPacket;

public final class SenderNodeSR extends SenderNode {
   public SenderNodeSR(Boolean[] frames, int windowSize) {
      super(frames, windowSize);
   }

   public synchronized void setAck(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         Console.log("\n", packet.type, " Packet Received =>\t", packet, "\n");
         switch (packet.type) {
         case ACK:
            this.windowFirst = packet.num + 1;
            this.frameResendTime = TimedPacket.getEpochSec();
            if (packet.num == this.frames.length - 1)
               this.isNodeOpen = false;
            break;

         case NAK:
            Console.log("\nResent Frame :\t", packet.num, "\n");
            this.frameResendTime = TimedPacket.getEpochSec() + 3;
            this.layer.transmit(Packet.createData(this.frames[packet.num]));
            break;

         default:
            break;
         }
      } else
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
   }
}
