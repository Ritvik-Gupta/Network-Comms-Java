package src.PacketTransmission;

import java.util.ArrayList;

import services.Console.Console;
import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.PacketFrame;
import src.PacketTransmission.Packet.TimedPacket;

public abstract class SenderNode implements Runnable {
   protected final PacketFrame[] frames;
   protected TransmissionLayer layer;
   protected boolean isNodeOpen;
   protected int windowSize;
   protected int windowFirst;
   protected double frameResendTime;

   protected SenderNode(Boolean[] frames, int windowSize) {
      this.frames = new PacketFrame[frames.length];
      for (int pos = 0; pos < frames.length; ++pos)
         this.frames[pos] = new PacketFrame(pos, frames[pos]);

      this.isNodeOpen = true;
      this.windowFirst = 0;
      this.windowSize = windowSize;
      this.frameResendTime = TimedPacket.getEpochSec();
   }

   public abstract void setAck(Packet packet);

   public void withTransmission(TransmissionLayer transmission) {
      this.layer = transmission;
   }

   protected boolean isInsideWindow(int packetNum) {
      return packetNum >= this.windowFirst && packetNum < this.windowFirst + this.windowSize;
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
