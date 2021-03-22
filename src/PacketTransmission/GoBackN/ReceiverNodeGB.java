package src.PacketTransmission.GoBackN;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.TransmissionEntity.ReceiverNode;

public final class ReceiverNodeGB extends ReceiverNode {
   public ReceiverNodeGB(int frameSize) {
      super(frameSize, 1);
   }

   public synchronized void setPacket(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         this.writeLog("Packet Received  =>\t", packet);
         this.frames[packet.num] = packet;

         ++this.windowFirst;
         this.layer.transmit(Packet.createAck(packet.num));
      } else {
         this.writeLog("Packet Denied =>\t", packet);
         if (this.windowFirst == this.frames.length)
            this.layer.transmit(Packet.createAck(this.windowFirst - 1));
      }
   }
}
