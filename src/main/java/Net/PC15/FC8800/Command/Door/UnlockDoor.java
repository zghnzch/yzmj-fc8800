package Net.PC15.FC8800.Command.Door;

import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.Door.Parameter.RemoteDoor_Parameter;
import Net.PC15.Util.ByteUtil;
import io.netty.buffer.ByteBuf;

public class UnlockDoor extends FC8800Command {
   public UnlockDoor(RemoteDoor_Parameter par) {
      this._Parameter = par;
      ByteBuf dataBuf = ByteUtil.ALLOCATOR.buffer(4);
      dataBuf.writeBytes(par.Door.DoorPort);
      this.CreatePacket(3, 4, 1, 4, dataBuf);
   }

   protected void Release0() {
   }
}
