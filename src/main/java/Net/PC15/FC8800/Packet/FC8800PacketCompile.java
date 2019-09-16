package Net.PC15.FC8800.Packet;
import Net.PC15.Packet.INPacket;
import Net.PC15.Packet.INPacketModel;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.UInt32Util;
import io.netty.buffer.ByteBuf;
public class FC8800PacketCompile implements INPacket {
	private FC8800PacketModel _Packet = null;
	private ByteBuf _PacketData;
	private long _CheckSum = 0L;
	public FC8800PacketCompile() {
	}
	public FC8800PacketCompile(String sn, long pwd, short iCmdType, short iCmdIndex, short iCmdPar) {
		long lSource = UInt32Util.GetRndNum();
		this.CreateFrame(sn, pwd, lSource, iCmdType, iCmdIndex, iCmdPar, 0, null);
	}
	public FC8800PacketCompile(String sn, long pwd, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
		long lSource = UInt32Util.GetRndNum();
		this.CreateFrame(sn, pwd, lSource, iCmdType, iCmdIndex, iCmdPar, iDataLen, Databuf);
	}
	public FC8800PacketCompile(String sn, long pwd, long code, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
		this.CreateFrame(sn, pwd, code, iCmdType, iCmdIndex, iCmdPar, iDataLen, Databuf);
	}
	private void CreateFrame(String sn, long pwd, long code, short iCmdType, short iCmdIndex, short iCmdPar, int iDataLen, ByteBuf Databuf) {
		if (this._Packet != null) {
			this._Packet.Release();
		}
		this._Packet = new FC8800PacketModel();
		this._Packet.SetSN(sn);
		this._Packet.SetPassword(pwd);
		this._Packet.SetCode(code);
		this._Packet.SetCmdType(iCmdType);
		this._Packet.SetCmdIndex(iCmdIndex);
		this._Packet.SetCmdPar(iCmdPar);
		this._Packet.SetDataLen(iDataLen);
		if (iDataLen > 0) {
			this._Packet.SetDatabuff(Databuf);
		}
		this.Compile();
	}
	public void Compile() {
		if (this._PacketData != null && this._PacketData.refCnt() > 0) {
			this._PacketData.release();
		}
		long iLen = 34L + this._Packet.GetDataLen();
		iLen *= 2L;
		this._PacketData = ByteUtil.ALLOCATOR.buffer((int) iLen);
		this._PacketData.writeByte(126);
		this._CheckSum = 0L;
		this.Push(StringUtil.FillString(this._Packet.GetSN(), 16, "F").getBytes());
		this.Push(UInt32Util.UINT32ToByteBuf(this._Packet.GetPassword()));
		this.Push(UInt32Util.UINT32ToByteBuf(this._Packet.GetCode()));
		this.Push(this._Packet.GetCmdType());
		this.Push(this._Packet.GetCmdIndex());
		this.Push(this._Packet.GetCmdPar());
		this.Push(UInt32Util.UINT32ToByteBuf(this._Packet.GetDataLen()));
		if (this._Packet.GetDataLen() > 0L) {
			this.Push(this._Packet.GetDatabuff(), false);
		}
		short sum = (short) ((int) (this._CheckSum & 255L));
		this._Packet.SetPacketCheck(sum);
		this.Push(sum);
		this._CheckSum = sum;
		this._PacketData.writeByte(126);
	}
	private void Push(int iByte) {
		this._CheckSum += iByte;
		if (iByte == 126) {
			this._PacketData.writeByte(127);
			this._PacketData.writeByte(1);
		}
		else if (iByte == 127) {
			this._PacketData.writeByte(127);
			this._PacketData.writeByte(2);
		}
		else {
			this._PacketData.writeByte(iByte);
		}
	}
	private void Push(byte[] buf) {
		byte[] var2 = buf;
		int var3 = buf.length;
		for (int var4 = 0; var4 < var3; ++var4) {
			byte b = var2[var4];
			int num = ByteUtil.uByte(b);
			this.Push(num);
		}
	}
	private void Push(ByteBuf buf) {
		this.Push(buf, true);
	}
	private void Push(ByteBuf buf, boolean release) {
		buf.forEachByte((value) -> {
			this.Push(ByteUtil.uByte(value));
			return true;
		});
		if (release) {
			buf.release();
		}
		buf = null;
	}
	public INPacketModel GetPacket() {
		return this._Packet;
	}
	public void SetPacket(INPacketModel packet) {
		this._Packet = (FC8800PacketModel) packet;
		if (this._Packet == null) {
			this.Release();
		}
		this.Compile();
	}
	public ByteBuf GetPacketData() {
		return this._PacketData;
	}
	public void Release() {
		if (this._PacketData != null) {
			if (this._PacketData.refCnt() > 0) {
				this._PacketData.release();
			}
			this._PacketData = null;
		}
		if (this._Packet != null) {
			this._Packet.Release();
			this._Packet = null;
		}
	}
}
