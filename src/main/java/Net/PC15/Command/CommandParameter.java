package Net.PC15.Command;

public class CommandParameter implements INCommandParameter {
   protected CommandDetail _Detail;

   public CommandParameter(CommandDetail detail) {
      this._Detail = detail;
   }

   public CommandDetail getCommandDetail() {
      return this._Detail;
   }

   public void setCommandDetail(CommandDetail detail) {
      this._Detail = detail;
   }
}
