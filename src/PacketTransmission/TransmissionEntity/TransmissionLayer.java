package src.PacketTransmission.TransmissionEntity;

import java.util.ArrayList;
import java.util.LinkedList;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.PacketType;
import src.PacketTransmission.Packet.TimedPacket;

public final class TransmissionLayer extends TransmissionEntity implements Runnable {
   private boolean isLayerOpen;
   private LinkedList<TimedPacket> storedPackets;
   private final SenderNode sender;
   private final ReceiverNode receiver;

   public TransmissionLayer(SenderNode sender, ReceiverNode receiver) {
      super("Transmission Layer");

      this.isLayerOpen = true;
      this.storedPackets = new LinkedList<>();
      this.sender = sender;
      this.receiver = receiver;
   }

   public synchronized void transmit(Packet packet) {
      this.writeLog("Transmit Packet =>\t", packet);
      this.storedPackets.addFirst(new TimedPacket(packet, 0.4));
   }

   public synchronized void transmit(ArrayList<Packet> packets) {
      for (Packet packet : packets) {
         this.writeLog("Transmit Packet =>\t", packet);
         this.storedPackets.addFirst(new TimedPacket(packet, 0.4));
      }
   }

   public void closeLayer() {
      this.writeLog("Closing Transmission Layer");
      this.isLayerOpen = false;
   }

   public void run() {
      while (this.isLayerOpen)
         synchronized (this) {
            for (int pos = this.storedPackets.size() - 1; pos >= 0; --pos)
               if (TimedPacket.getEpochSec() >= this.storedPackets.get(pos).endTime) {
                  Packet packet = this.storedPackets.remove(pos);
                  if (Math.random() > 0.15) {
                     if (packet.type == PacketType.DATA)
                        this.receiver.setPacket(packet);
                     else
                        this.sender.setAck(packet);
                  } else
                     this.writeLog("Packet Lost =>\t", packet);
               }
         }

      this.writeLog("Transmission Layer Closed");
   }
}
