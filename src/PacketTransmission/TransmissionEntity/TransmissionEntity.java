package src.PacketTransmission.TransmissionEntity;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import services.Console.Console;

public abstract class TransmissionEntity {
   private FileWriter file;
   private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("mm:ss:nnnnn");
   private final String messageKey;

   protected TransmissionEntity(String messageKey) {
      this.messageKey = messageKey;
   }

   public void writeLogsInFile(FileWriter file) throws IOException {
      this.file = file;
   }

   protected synchronized void writeLog(Object... args) {
      String str = "";
      for (Object arg : args)
         str += arg == null ? arg : arg.toString();
      Console.log("\n", str, "\n");

      if (this.file != null)
         try {
            String timestamp = "\n[ " + timeFormatter.format(LocalTime.now()) + " ] ";
            this.file.write(timestamp + this.messageKey + " ->\t" + str + "\n");
         } catch (IOException err) {
            err.printStackTrace();
         }
   }
}
