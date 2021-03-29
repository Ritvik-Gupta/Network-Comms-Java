package src.CSMA_CA.TransmissionEntity;

import java.util.ArrayList;

import src.CSMA_CA.Packet.Packet;
import src.CSMA_CA.Packet.PacketFrame;
import src.CSMA_CA.Packet.TimedPacket;

public final class SenderNode extends TransmissionEntity implements Runnable {
   private int id;
   private final PacketFrame[] frames;
   private TransmissionMedium layer;
   private boolean isNodeOpen;
   private final int windowSize;
   private int windowFirst;
   private double frameResendTime;

   public SenderNode(int id, Boolean[] frames, int windowSize) {
      super("Sender Node < " + id + " >");

      this.frames = new PacketFrame[frames.length];
      for (int pos = 0; pos < frames.length; ++pos)
         this.frames[pos] = new PacketFrame(pos, frames[pos], id);

      this.id = id;
      this.isNodeOpen = true;
      this.windowFirst = 0;
      this.windowSize = windowSize;
      this.frameResendTime = TimedPacket.getEpochSec();
   }

   public synchronized void setAck(Packet packet) {
      if (this.isInsideWindow(packet.num)) {
         this.writeLog("Node < ", this.id, " > Packet Received =>\t", packet);
         this.windowFirst = packet.num + 1;
         this.frameResendTime = TimedPacket.getEpochSec();
         if (packet.num == this.frames.length - 1)
            this.isNodeOpen = false;
      } else
         this.writeLog("Node < ", this.id, " > Packet Denied =>\t", packet);
   }

   public int frameSize() {
      return this.frames.length;
   }

   public void withTransmission(TransmissionMedium transmission) {
      this.layer = transmission;
   }

   private boolean isInsideWindow(int packetNum) {
      return packetNum >= this.windowFirst && packetNum < this.windowFirst + this.windowSize;
   }

   public void run() {
      while (this.isNodeOpen)
         if (TimedPacket.getEpochSec() >= this.frameResendTime) {
            ArrayList<Packet> packets = new ArrayList<>();
            int windowBound = Math.min(this.windowFirst + this.windowSize, this.frames.length);
            for (int windowPos = this.windowFirst; windowPos < windowBound; ++windowPos) {
               this.writeLog("Node < ", this.id, " > Current Frame :\t", windowPos);
               packets.add(Packet.createRTS(this.frames[windowPos]));
            }
            this.layer.transmit(packets);
            this.frameResendTime = TimedPacket.getEpochSec() + 3;
         }

      this.writeLog("Closing Sender Node < ", this.id, " > ");
   }
}
