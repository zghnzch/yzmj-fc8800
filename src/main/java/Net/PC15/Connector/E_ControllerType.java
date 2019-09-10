package Net.PC15.Connector;

public enum E_ControllerType {
   FC8900(0),
   FC8800(1),
   MC5800(2);

   private final int value;

   private E_ControllerType(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }
}
