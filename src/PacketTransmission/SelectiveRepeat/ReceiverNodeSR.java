package src.PacketTransmission.SelectiveRepeat;

import java.util.ArrayList;

import src.PacketTransmission.ReceiverNode;
import src.PacketTransmission.Packet.Packet;
import services.Console.Console;

public final class ReceiverNodeSR extends ReceiverNode {
   private int windowSize;
   private int windowFirst;

   public ReceiverNodeSR(int frameSize, int windowSize) {
      super(frameSize);
      this.windowSize = windowSize;
      this.windowFirst = 0;
   }

   private boolean isInsideWindow(int packetNum) {
      return packetNum >= this.windowFirst && packetNum < this.windowFirst + this.windowSize;
   }

   public synchronized void setPacket(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         if (this.frames[packet.num] == null) {
            Console.log("\n", packet.type, " Packet Received  =>\t", packet, "\n");
            this.frames[packet.num] = packet;
         } else
            Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");

         ArrayList<Packet> naks = new ArrayList<>();
         for (int windowPos = this.windowFirst; windowPos < packet.num; ++windowPos)
            if (this.frames[windowPos] == null) {
               Console.log("\nSending NAK for Frame :\t", windowPos, "\n");
               naks.add(Packet.createNak(windowPos));
            }

         if (naks.size() == 0) {
            this.windowFirst = packet.num + 1;
            this.layer.transmit(Packet.createAck(packet.num));
         } else
            this.layer.transmit(naks);
      } else {
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
         if (this.windowFirst == this.frames.length)
            this.layer.transmit(Packet.createAck(this.windowFirst - 1));
      }
   }
}
