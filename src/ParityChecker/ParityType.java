package src.ParityChecker;

public enum ParityType {
   ODD(false), EVEN(true);

   private final boolean isEvenParity;

   private ParityType(boolean isEvenParity) {
      this.isEvenParity = isEvenParity;
   }

   public boolean isEven() {
      return this.isEvenParity;
   }
}
