package src.PacketTransmission.StopAndWait;

import services.Console.Console;
import src.PacketTransmission.SenderNode;
import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.TimedPacket;

public final class SenderNodeSW extends SenderNode {
   public SenderNodeSW(Boolean[] frames) {
      super(frames, 1);
   }

   public synchronized void setAck(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         Console.log("\n", packet.type, " Packet Received =>\t", packet, "\n");
         this.windowFirst = (this.windowFirst + 1) % this.frames.length;
         this.frameResendTime = TimedPacket.getEpochSec();
         if (packet.num == this.frames.length - 1)
            this.isNodeOpen = false;
      } else
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
   }
}
