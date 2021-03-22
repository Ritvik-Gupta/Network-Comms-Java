package src.PacketTransmission;

import src.PacketTransmission.GoBackN.ReceiverNodeGB;
import src.PacketTransmission.GoBackN.SenderNodeGB;
import src.PacketTransmission.SelectiveRepeat.ReceiverNodeSR;
import src.PacketTransmission.SelectiveRepeat.SenderNodeSR;
import src.PacketTransmission.StopAndWait.ReceiverNodeSW;
import src.PacketTransmission.StopAndWait.SenderNodeSW;
import src.PacketTransmission.TransmissionEntity.ReceiverNode;
import src.PacketTransmission.TransmissionEntity.SenderNode;
import src.PacketTransmission.TransmissionEntity.TransmissionLayer;

import java.io.FileWriter;
import java.io.IOException;

import services.Console.Console;

public class Simulate {

   public static Boolean[] parseBits(String transmissionData) throws Exception {
      int size = transmissionData.length();
      Boolean [] frames = new Boolean[size];
      for(int pos=0;pos<size;++pos) {
         if(transmissionData.charAt(pos) == '1') frames[pos] = true;
         else if(transmissionData.charAt(pos) == '0') frames[pos] = false;
         else throw new Exception("Transmission Bits can only contain 0s and 1s");
      }
      return frames;
   }

   public static void main(String[] args) throws InterruptedException, IOException, Exception {
      Boolean[] frames = parseBits(args[0]);
      Console.log("\nStarting Selective Repeat ARQ Simulation\n");
      Console.println(frames);

      SenderNode sender = new SenderNodeSR(frames, 4);
      ReceiverNode receiver = new ReceiverNodeSR(frames.length, 4);
      TransmissionLayer transmission = new TransmissionLayer(sender, receiver);

      sender.withTransmission(transmission);
      receiver.withTransmission(transmission);

      FileWriter simulationLogFile = new FileWriter("./src/PacketTransmission/SimulationLogs.txt");
      sender.writeLogsInFile(simulationLogFile);
      receiver.writeLogsInFile(simulationLogFile);
      transmission.writeLogsInFile(simulationLogFile);

      Thread senderThread = new Thread(sender);
      Thread transmissionThread = new Thread(transmission);

      transmissionThread.start();
      senderThread.start();

      senderThread.join();
      transmissionThread.join();

      Console.log("\n");
      Console.println(receiver.getFrames());

      simulationLogFile.close();
   }
}
