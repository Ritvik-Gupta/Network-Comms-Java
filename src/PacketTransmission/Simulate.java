package src.PacketTransmission;

import src.PacketTransmission.GoBackN.ReceiverNodeGB;
import src.PacketTransmission.GoBackN.SenderNodeGB;
import src.PacketTransmission.SelectiveRepeat.ReceiverNodeSR;
import src.PacketTransmission.SelectiveRepeat.SenderNodeSR;
import src.PacketTransmission.StopAndWait.ReceiverNodeSW;
import src.PacketTransmission.StopAndWait.SenderNodeSW;
import services.Console.Console;

public class Simulate {
   public static void main(String[] args) throws InterruptedException {
      Boolean[] frames = { false, true, true, false, false, false, true, false, true };
      Console.log("\nStarting Stop and Wait ARQ Simulation\n");
      Console.println(frames);

      SenderNode sender = new SenderNodeSR(frames, 4);
      ReceiverNode receiver = new ReceiverNodeSR(frames.length, 4);
      TransmissionLayer transmission = new TransmissionLayer(sender, receiver);

      sender.withTransmission(transmission);
      receiver.withTransmission(transmission);

      Thread senderThread = new Thread(sender);
      Thread transmissionThread = new Thread(transmission);

      transmissionThread.start();
      senderThread.start();

      senderThread.join();
      transmissionThread.join();

      Console.log("\n");
      Console.println(receiver.getFrames());
   }
}
