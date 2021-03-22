package src.PacketTransmission.GoBackN;

import java.util.ArrayList;

import src.PacketTransmission.SenderNode;
import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.TimedPacket;
import services.Console.Console;

public final class SenderNodeGB extends SenderNode {
   private int windowSize;
   private int windowFirst;
   private double frameResendTime;

   public SenderNodeGB(Boolean[] frames, int windowSize) {
      super(frames);
      this.windowSize = windowSize;
      this.windowFirst = 0;
      this.frameResendTime = TimedPacket.getEpochSec();
   }

   private boolean isInsideWindow(int packetNum) {
      return packetNum >= this.windowFirst && packetNum < this.windowFirst + this.windowSize;
   }

   public synchronized void setAck(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         Console.log("\n", packet.type, " Packet Received =>\t", packet, "\n");
         this.windowFirst = packet.num + 1;
         this.frameResendTime = TimedPacket.getEpochSec();
         if (packet.num == this.frames.length - 1)
            this.isNodeOpen = false;
      } else
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
   }

   public void run() {
      while (this.isNodeOpen)
         if (TimedPacket.getEpochSec() >= this.frameResendTime) {
            ArrayList<Packet> packets = new ArrayList<>();
            int windowBound = Math.min(this.windowFirst + this.windowSize, this.frames.length);
            for (int windowPos = this.windowFirst; windowPos < windowBound; ++windowPos) {
               Console.log("\nCurrent Frame :\t", windowPos, "\n");
               packets.add(Packet.createData(this.frames[windowPos]));
            }
            this.layer.transmit(packets);
            this.frameResendTime = TimedPacket.getEpochSec() + 3;
         }

      Console.log("\nClosing Sender Node\n");
      this.layer.closeLayer();
   }
}
