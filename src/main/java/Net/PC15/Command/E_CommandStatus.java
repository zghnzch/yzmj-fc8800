package Net.PC15.Command;

public enum E_CommandStatus {
   OnReady(0),
   OnWaitResponse(1),
   OnOver(2);

   private final int value;

   private E_CommandStatus(int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }
}
