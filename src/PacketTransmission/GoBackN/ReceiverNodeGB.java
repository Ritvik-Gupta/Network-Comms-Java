package src.PacketTransmission.GoBackN;

import src.PacketTransmission.ReceiverNode;
import src.PacketTransmission.Packet.Packet;
import services.Console.Console;

public final class ReceiverNodeGB extends ReceiverNode {
   public ReceiverNodeGB(int frameSize) {
      super(frameSize, 1);
   }

   public synchronized void setPacket(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         Console.log("\n", packet.type, " Packet Received  =>\t", packet, "\n");
         this.frames[packet.num] = packet;

         ++this.windowFirst;
         this.layer.transmit(Packet.createAck(packet.num));
      } else {
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
         if (this.windowFirst == this.frames.length)
            this.layer.transmit(Packet.createAck(this.windowFirst - 1));
      }
   }
}
