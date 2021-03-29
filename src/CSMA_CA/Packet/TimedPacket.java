package src.CSMA_CA.Packet;

import java.time.ZonedDateTime;

public final class TimedPacket extends Packet {
   public final double endTime;

   public TimedPacket(Packet packet, double interval) {
      super(packet, packet.type);
      this.endTime = getEpochSec() + interval;
   }

   public static long getEpochSec() {
      return ZonedDateTime.now().toInstant().getEpochSecond();
   }
}
