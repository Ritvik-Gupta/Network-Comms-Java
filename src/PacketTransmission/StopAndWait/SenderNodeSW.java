package src.PacketTransmission.StopAndWait;

import src.PacketTransmission.SenderNode;
import src.PacketTransmission.Packet.Packet;
import src.PacketTransmission.Packet.TimedPacket;
import services.Console.Console;

public final class SenderNodeSW extends SenderNode {
   private int currentFrame;
   private double frameResendTime;

   public SenderNodeSW(Boolean[] frames) {
      super(frames);
      this.currentFrame = 0;
      this.frameResendTime = TimedPacket.getEpochSec();
   }

   public synchronized void setAck(Packet packet) {
      if (packet.num == this.currentFrame) {
         Console.log("\n", packet.type, " Packet Received =>\t", packet, "\n");
         this.currentFrame = (this.currentFrame + 1) % this.frames.length;
         this.frameResendTime = TimedPacket.getEpochSec();
         if (packet.num == this.frames.length - 1)
            this.isNodeOpen = false;
      } else
         Console.log("\n", packet.type, " Packet Denied =>\t", packet, "\n");
   }

   public void run() {
      while (this.isNodeOpen) 
         if (TimedPacket.getEpochSec() >= this.frameResendTime) {
            Console.log("\nCurrent Frame :\t", this.currentFrame, "\n");
            this.layer.transmit(Packet.createData(this.frames[this.currentFrame]));
            this.frameResendTime = TimedPacket.getEpochSec() + 4;
         }
      
      Console.log("\nClosing Sender Node\n");
      this.layer.closeLayer();
   }
}
