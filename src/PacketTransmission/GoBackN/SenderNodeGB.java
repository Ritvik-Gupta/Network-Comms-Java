package src.PacketTransmission.GoBackN;

import services.Console.Console;
import src.PacketTransmission.SenderNode;
import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.TimedPacket;

public final class SenderNodeGB extends SenderNode {
   public SenderNodeGB(Boolean[] frames, int windowSize) {
      super(frames, windowSize);
   }

   public synchronized void setAck(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         Console.log("\n", packet.type, " Packet Received =>\t", packet, "\n");
         this.windowFirst = packet.num + 1;
         this.frameResendTime = TimedPacket.getEpochSec();
         if (packet.num == this.frames.length - 1)
            this.isNodeOpen = false;
      } else
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
   }
}
