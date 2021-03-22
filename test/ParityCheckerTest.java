package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import services.ErrorHandler.Handler;
import services.Random.Random;
import src.ParityChecker.ParityChecker;
import src.ParityChecker.ParityType;

public final class ParityCheckerTest extends BaseTest {
   @BeforeEach
   public void beforeEach() {
      System.out.println("Before Each Parity Checher Test");
   }

   public static ArrayList<Arguments> matchTransmissionProvider() {
      ArrayList<Arguments> stream = new ArrayList<>();
      providerRunIterations(Handler.uncheck(__ -> {
         String transmission = Random.word(5, 10, List.of('0', '1'));
         ParityType parity = Random.choose(new ParityType[] { ParityType.EVEN, ParityType.ODD });
         
         ParityChecker senderNode = new ParityChecker(transmission, parity);
         double noiseFactor = Random.range(0, 0.5);
         stream.add(Arguments.of(senderNode, noiseFactor));
      }));
      return stream;
   }

   @ParameterizedTest
   @DisplayName("Parity Check Algorithm should correctly predict fault transmissions")
   @MethodSource("matchTransmissionProvider")
   public void matchTransmission(ParityChecker senderNode, double noiseFactor) throws Exception {
      ParityChecker receiverNode = ParityChecker.generateRandomNoise(senderNode, noiseFactor);
      boolean shouldNotThrow = ParityChecker.calculateTotalFaults(senderNode, receiverNode) % 2 == 0;

      Assertions.assertSame(shouldNotThrow, receiverNode.isValidParity());
   }
}
