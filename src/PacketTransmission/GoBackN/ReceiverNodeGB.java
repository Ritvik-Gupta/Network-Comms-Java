package src.PacketTransmission.GoBackN;

import src.PacketTransmission.ReceiverNode;
import src.PacketTransmission.Packet.Packet;
import services.Console.Console;

public final class ReceiverNodeGB extends ReceiverNode {
   private int currentFrame;

   public ReceiverNodeGB(int frameSize) {
      super(frameSize);
      this.currentFrame = 0;
   }

   public synchronized void setPacket(Packet packet) {
      if (packet.num == this.currentFrame) {
         Console.log("\n", packet.type, " Packet Received  =>\t", packet, "\n");
         this.frames[packet.num] = packet;

         ++this.currentFrame;
         this.layer.transmit(Packet.createAck(packet.num));
      } else {
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
         if (this.currentFrame == this.frames.length)
            this.layer.transmit(Packet.createAck(this.currentFrame - 1));
      }
   }
}
