package src.PacketTransmission.StopAndWait;

import src.PacketTransmission.ReceiverNode;
import src.PacketTransmission.Packet.Packet;
import services.Console.Console;

public final class ReceiverNodeSW extends ReceiverNode {
   private int currentFrame;

   public ReceiverNodeSW(int frameSize) {
      super(frameSize);
      this.currentFrame = 0;
   }

   public synchronized void setPacket(Packet packet) {
      if (packet.num == this.currentFrame) {
         Console.log("\n", packet.type, " Packet Received  =>\t", packet, "\n");
         this.frames[packet.num] = packet;

         ++this.currentFrame;
      } else
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
      this.layer.transmit(Packet.createAck(packet.num));
   }
}
