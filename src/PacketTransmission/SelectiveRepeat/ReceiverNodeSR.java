package src.PacketTransmission.SelectiveRepeat;

import java.util.ArrayList;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.TransmissionEntity.ReceiverNode;

public final class ReceiverNodeSR extends ReceiverNode {
   public ReceiverNodeSR(int frameSize, int windowSize) {
      super(frameSize, windowSize);
   }

   public synchronized void setPacket(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         if (this.frames[packet.num] == null) {
            this.writeLog("Packet Received  =>\t", packet);
            this.frames[packet.num] = packet;
         } else
            this.writeLog("Packet Denied =>\t", packet);

         ArrayList<Packet> naks = new ArrayList<>();
         for (int windowPos = this.windowFirst; windowPos < packet.num; ++windowPos)
            if (this.frames[windowPos] == null) {
               this.writeLog("Sending NAK for Frame :\t", windowPos);
               naks.add(Packet.createNak(windowPos));
            }

         if (naks.size() == 0) {
            this.windowFirst = packet.num + 1;
            this.layer.transmit(Packet.createAck(packet.num));
         } else
            this.layer.transmit(naks);
      } else {
         this.writeLog("Packet Denied =>\t", packet);
         if (this.windowFirst == this.frames.length)
            this.layer.transmit(Packet.createAck(this.windowFirst - 1));
      }
   }
}
