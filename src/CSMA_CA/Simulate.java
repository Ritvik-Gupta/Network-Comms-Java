package src.CSMA_CA;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import services.Console.Console;
import src.CSMA_CA.TransmissionEntity.ReceiverNode;
import src.CSMA_CA.TransmissionEntity.SenderNode;
import src.CSMA_CA.TransmissionEntity.TransmissionMedium;

public final class Simulate {
   public static Boolean[] parseBits(String transmissionData) throws Exception {
      int size = transmissionData.length();
      Boolean[] frames = new Boolean[size];
      for (int pos = 0; pos < size; ++pos) {
         if (transmissionData.charAt(pos) == '1')
            frames[pos] = true;
         else if (transmissionData.charAt(pos) == '0')
            frames[pos] = false;
         else
            throw new Exception("Transmission Bits can only contain 0s and 1s");
      }
      return frames;
   }

   public static void main(String[] args) throws InterruptedException, IOException, Exception {
      Console.log("\nStarting Go Back ARQ for CSMA/CA Simulation\n");

      ArrayList<SenderNode> senderNodes = new ArrayList<>();
      ArrayList<Integer> nodeFrameSizes = new ArrayList<>();
      for (int pos = 0; pos < args.length; ++pos) {
         Boolean[] frames = parseBits(args[pos]);
         senderNodes.add(new SenderNode(pos, frames, 4));
         nodeFrameSizes.add(frames.length);

         Console.log("\nFrames for Sender Node < ", pos, " > :\n");
         Console.println(frames);
      }

      ReceiverNode receiver = new ReceiverNode(nodeFrameSizes);
      TransmissionMedium transmission = new TransmissionMedium(senderNodes, receiver);

      senderNodes.forEach(node -> node.withTransmission(transmission));
      receiver.withTransmission(transmission);

      FileWriter simulationLogFile = new FileWriter("./src/CSMA_CA/SimulationLogs.txt");
      for (SenderNode node : senderNodes)
         node.writeLogsInFile(simulationLogFile);

      receiver.writeLogsInFile(simulationLogFile);
      transmission.writeLogsInFile(simulationLogFile);

      ArrayList<Thread> senderNodeThreads = new ArrayList<>();
      senderNodes.forEach(node -> senderNodeThreads.add(new Thread(node)));
      Thread transmissionThread = new Thread(transmission);

      transmissionThread.start();
      senderNodeThreads.forEach(node -> node.start());
      for (Thread node : senderNodeThreads)
         node.join();

      transmission.closeLayer();
      transmissionThread.join();

      Console.log("\n");
      for (int pos = 0; pos < args.length; ++pos) {
         Console.log("\nFrames Received from Sender Node < ", pos, " > :\n");
         Console.println(receiver.getFrames(pos));
      }

      simulationLogFile.close();
   }
}
