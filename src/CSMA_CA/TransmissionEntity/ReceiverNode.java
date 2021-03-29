package src.CSMA_CA.TransmissionEntity;

import java.util.ArrayList;
import java.util.List;

import src.CSMA_CA.Packet.Packet;
import src.CSMA_CA.Packet.PacketFrame;

public final class ReceiverNode extends TransmissionEntity {
   private final ArrayList<PacketFrame[]> frames;
   private TransmissionMedium layer;
   private ArrayList<Integer> currentFrames;

   public ReceiverNode(List<Integer> nodesFramesSize) {
      super("Receiver Node");

      this.frames = new ArrayList<>();
      this.currentFrames = new ArrayList<>();

      nodesFramesSize.forEach(frameSize -> {
         this.frames.add(new PacketFrame[frameSize]);
         this.currentFrames.add(0);
      });
   }

   public synchronized void setPacket(Packet packet) {
      int currentFrame = this.currentFrames.get(packet.senderId);
      if (packet.num == currentFrame) {
         this.writeLog("Packet Received  =>\t", packet);
         this.frames.get(packet.senderId)[packet.num] = packet;

         this.currentFrames.set(packet.senderId, currentFrame + 1);
         this.layer.transmit(Packet.createCTS(packet.num, packet.senderId));
      } else {
         this.writeLog("Packet Denied =>\t", packet);
         if (currentFrame == this.frames.get(packet.senderId).length)
            this.layer.transmit(Packet.createCTS(currentFrame - 1, packet.senderId));
      }
   }

   public void withTransmission(TransmissionMedium transmission) {
      this.layer = transmission;
   }

   public Boolean[] getFrames(int senderId) {
      PacketFrame[] senderNodeFrames = this.frames.get(senderId);

      Boolean[] frames = new Boolean[senderNodeFrames.length];
      for (int pos = 0; pos < senderNodeFrames.length; ++pos)
         frames[pos] = senderNodeFrames[pos].message;
      return frames;
   }
}
