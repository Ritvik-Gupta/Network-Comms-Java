package src.PacketTransmission.StopAndWait;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.TransmissionEntity.ReceiverNode;

public final class ReceiverNodeSW extends ReceiverNode {
   public ReceiverNodeSW(int frameSize) {
      super(frameSize, 1);
   }

   public synchronized void setPacket(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         this.writeLog("Packet Received  =>\t", packet);
         this.frames[packet.num] = packet;

         ++this.windowFirst;
      } else
         this.writeLog("Packet Denied =>\t", packet);
      this.layer.transmit(Packet.createAck(packet.num));
   }
}
