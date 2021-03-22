package src.PacketTransmission;

import java.util.ArrayList;
import java.util.LinkedList;

import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.PacketType;
import src.PacketTransmission.Packet.TimedPacket;
import services.Console.Console;

public final class TransmissionLayer implements Runnable {
   private boolean isLayerOpen;
   private LinkedList<TimedPacket> storedPackets;
   private final SenderNode sender;
   private final ReceiverNode receiver;

   public TransmissionLayer(SenderNode sender, ReceiverNode receiver) {
      this.isLayerOpen = true;
      this.storedPackets = new LinkedList<>();
      this.sender = sender;
      this.receiver = receiver;
   }

   public synchronized void transmit(Packet packet) {
      Console.log("\nTransmit ", packet.type, " Packet =>\t", packet, "\n");
      this.storedPackets.addFirst(new TimedPacket(packet, 0.4));
   }

   public synchronized void transmit(ArrayList<Packet> packets) {
      for (Packet packet : packets) {
         Console.log("\nTransmit ", packet.type, " Packet =>\t", packet, "\n");
         this.storedPackets.addFirst(new TimedPacket(packet, 0.4));
      }
   }

   public void closeLayer() {
      Console.log("\nClosing Transmission Layer\n");
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
                     Console.log("\n", packet.type, " Packet Lost =>\t", packet, "\n");
               }
         }

      Console.log("\nTransmission Layer Closed\n");
   }
}
