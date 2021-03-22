package src.PacketTransmission.StopAndWait;

import src.PacketTransmission.ReceiverNode;
import src.PacketTransmission.Packet.Packet;
import services.Console.Console;

public final class ReceiverNodeSW extends ReceiverNode {
   public ReceiverNodeSW(int frameSize) {
      super(frameSize, 1);
   }

   public synchronized void setPacket(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         Console.log("\n", packet.type, " Packet Received  =>\t", packet, "\n");
         this.frames[packet.num] = packet;

         ++this.windowFirst;
      } else
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
      this.layer.transmit(Packet.createAck(packet.num));
   }
}
