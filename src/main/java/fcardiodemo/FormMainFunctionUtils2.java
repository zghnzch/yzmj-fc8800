package fcardiodemo;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Command.INCommand;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.Connector.TCPClient.TCPClientDetail;
import Net.PC15.Connector.UDP.UDPDetail;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.*;
import Net.PC15.FC8800.Command.System.Parameter.*;
import Net.PC15.FC8800.Command.System.Result.*;
import Net.PC15.FC8800.FC8800Identity;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.TimeUtil;
import Net.PC15.Util.UInt32Util;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.Contract;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static fcardiodemo.FormMain.*;
import static fcardiodemo.FormMain.mSystemTransactionList;
/***
 * @class FormMainFunctionUtils2
 * @description TODO
 * @author zch
 * @date 2019/9/12
 * @version V0.0.1.201909121033.01
 * @modfiyDate 201909121033
 * @createDate 201909121033
 * @package fcardiodemo
 */
public class FormMainFunctionUtils2 {
	// 读取SN
	static void butReadSNActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			myLog.error("CommandDetail is null");
			return;
		}
		myLog.info("ReadSN:"+dt.toString());
		CommandParameter par = new CommandParameter(dt);
		INCommand cmd = new ReadSN(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			x.append("控制器SN：");
			ReadSN_Result result = (ReadSN_Result) y;
			x.append(result.SN);
			txtWriteSN.setText(result.SN);
			txtSN.setText(result.SN);
		});
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butReadSNActionPerformed
	static void butWriteSNActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteSNActionPerformed
		String sSN = txtWriteSN.getText();
		if (sSN.length() != 16) {
			JOptionPane.showMessageDialog(null, "SN必须是16位！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		WriteSN_Parameter par = new WriteSN_Parameter(dt, sSN);
		INCommand cmd = new WriteSN(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			x.append("控制器SN：");
			String sSN1 = txtWriteSN.getText();
			x.append(sSN1);
			txtSN.setText(sSN1);
		});
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butWriteSNActionPerformed
	static void butReadVersionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadVersionActionPerformed
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		INCommand cmd = new ReadVersion(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadVersion_Result result = (ReadVersion_Result) y;
			x.append("控制板固件版本号：");
			x.append(result.Version);
		});
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butReadVersionActionPerformed
	static void ButWriteDeadlineActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButWriteDeadlineActionPerformed
		CommandDetail dt = getCommandDetail();
		WriteDeadline_Parameter par = new WriteDeadline_Parameter(dt, 65535);
		INCommand cmd = new WriteDeadline(par);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_ButWriteDeadlineActionPerformed
	static void ButReadDeadlineActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButReadDeadlineActionPerformed
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		INCommand cmd = new ReadDeadline(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadDeadline_Result result = (ReadDeadline_Result) y;
			if (result.Deadline == 0) {
				x.append("控制板已失效！");
			}
			else if (result.Deadline == 65535) {
				x.append("控制板永久有效！");
			}
			else {
				x.append("控制板剩余有效天数：");
				x.append(result.Deadline);
				x.append("天.");
			}
		});
		_Allocator.AddCommand(cmd);
	}
	static void butWriteTCPSettingActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteTCPSettingActionPerformed
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 15000;// UDP延迟增加
		if (mReadTCPDetail == null) {
			JOptionPane.showMessageDialog(null, "请先读取TCP参数！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		mReadTCPDetail.SetServerIP("192.168.1.2");
		mReadTCPDetail.SetServerAddr("192.168.1.2");
		WriteTCPSetting_Parameter par = new WriteTCPSetting_Parameter(dt, mReadTCPDetail);
		INCommand cmd = new WriteTCPSetting(par);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butWriteTCPSettingActionPerformed
	static void butReadTCPSettingActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadTCPSettingActionPerformed
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		// UDP延迟增加
		dt.Timeout = 15000;
		CommandParameter par = new CommandParameter(dt);
		INCommand cmd = new ReadTCPSetting(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadTCPSetting_Result result = (ReadTCPSetting_Result) y;
			TCPDetail tcp = result.TCP;
			PirntTCPDetail(tcp, x);
		});
		_Allocator.AddCommand(cmd);
	}
	static void butResetConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butResetConnectPasswordActionPerformed
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		INCommand cmd = new ResetConnectPassword(par);
		_Allocator.AddCommand(cmd);
	}
	static void butWriteConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteConnectPasswordActionPerformed
		// String pwd = txtPassword.getText();
		String pwd = "12345678";
		if (pwd.length() != 8) {
			JOptionPane.showMessageDialog(null, "通讯密码必须是8位！", "错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		WriteConnectPassword_Parameter par = new WriteConnectPassword_Parameter(dt, pwd);
		INCommand cmd = new WriteConnectPassword(par);
		_Allocator.AddCommand(cmd);
	}
	static void butReadConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadConnectPasswordActionPerformed
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		INCommand cmd = new ReadConnectPassword(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			x.append("通讯密码：");
			ReadConnectPassword_Result result = (ReadConnectPassword_Result) y;
			x.append(result.Password);
			txtPassword.setText(result.Password);
		});
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butReadConnectPasswordActionPerformed
	public static void PirntTCPDetail(TCPDetail tcp, StringBuilder x) {
		x.append("MAC：");
		x.append(tcp.GetMAC());
		x.append("IP：");
		x.append(tcp.GetIP());
		x.append(",子网掩码：");
		x.append(tcp.GetIPMask());
		x.append(",网关：");
		x.append(tcp.GetIPGateway());
		x.append("DNS1：");
		x.append(tcp.GetDNS());
		x.append(",DNS2：");
		x.append(tcp.GetDNSBackup());
		x.append("TCP端口：");
		x.append(tcp.GetTCPPort());
		x.append(",UDP端口：");
		x.append(tcp.GetUDPPort());
		x.append("服务器IP：");
		x.append(tcp.GetServerIP());
		x.append(",端口：");
		x.append(tcp.GetServerPort());
		x.append("服务器域名：");
		x.append(tcp.GetServerAddr());
	}
	static void IniCardDataBase() {
		cmbCardDataBaseType.removeAllItems();
		cmbCardDataBaseType.addItem("排序卡区域");
		cmbCardDataBaseType.addItem("非排序卡区域");
		cmbCardDataBaseType.addItem("所有区域");
		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
		String[] cols = "序号，卡号，密码，有效期，卡片状态，有效次数，特权，门1权限，门1时段，门2权限，门2时段，门3权限，门3时段，门4权限，门4时段，节假日限制".split("，");
		// tblCard.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tblCard.setColumnSelectionAllowed(true);
		int[] colWidth = new int[]{40, 70, 60, 110, 60, 60, 70, 55, 55, 55, 55, 55, 55, 55, 55, 70};
		for (String col : cols) {
			tableModel.addColumn(col);
		}
		for (int i = 0; i < colWidth.length; i++) {
			TableColumn Column = tblCard.getColumnModel().getColumn(i);
			Column.setMinWidth(colWidth[i]);
			Column.setMaxWidth(150);
			Column.setPreferredWidth(colWidth[i]);
		}
		// 设置table内容居中
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(SwingConstants.CENTER);
		tblCard.setDefaultRenderer(Object.class, tcr);
		// 设置table表头居中
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tblCard.getTableHeader().getDefaultRenderer();
		hr.setHorizontalAlignment(SwingConstants.CENTER);
		//
		tblCard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// 不可整列移动
		tblCard.getTableHeader().setReorderingAllowed(false);
		// 不可拉动表格
		tblCard.getTableHeader().setResizingAllowed(false);
		tableModel.addRow(new Object[]{1, "0000000000", "00000000", "2017-11-14 12:30 ", " 黑名单 ", " 无限制 ", " 防盗设置卡 ", " 有 ", " 64 ", " 有 ", " 64 ", " 有 ", " 64 ", " 有 ", " 64 ", "无"});
		tblCard.invalidate();
		tblCard.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				tblCard_mouseClicked();
			}
		});
	}
	public static void tblCard_mouseClicked() {
		int numRow = tblCard.getSelectedRow();
		javax.swing.table.TableModel model = tblCard.getModel();
		long Card = Long.parseLong(model.getValueAt(numRow, 1).toString());
		CardDetail cd = new CardDetail();
		try {
			cd.SetCardData(String.valueOf(Card));
		}
		catch (Exception e) {
		}
		int Index = mCardList.indexOf(cd);
		if (Index == -1) {
			return;
		}
		cd = mCardList.get(Index);
		// 序号，卡号，密码，有效期，卡片状态，有效次数，特权，门1权限，门1时段，门2权限，门2时段，门3权限，门3时段，门4权限，门4时段，节假日限制
		txtCardData.setText(model.getValueAt(numRow, 1).toString());
		txtCardPassword.setText(model.getValueAt(numRow, 2).toString());
		TxtCardExpiry.setText(model.getValueAt(numRow, 3).toString());
		txtOpenTimes.setText(model.getValueAt(numRow, 5).toString());
		cmbCardStatus.setSelectedIndex(cd.CardStatus);
		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
		for (int i = 1; i <= 4; i++) {
			doors[i - 1].setSelected(cd.GetDoor(i));
		}
	}
	public static String WriteFile(String sFileName, StringBuilder strBuf, boolean append) {
		File[] roots = File.listRoots();
		String Path = "d:\\";
		for (int i = 1; i < roots.length; i++) {
			if (roots[i].getFreeSpace() > 0) {
				Path = roots[i].getPath();
				break;
			}
		}
		Path += sFileName + ".txt";
		try {
			File file = new File(Path);
			if (!file.exists()) {
				file.createNewFile();
			}
			else {
				if (!append) {
					file.delete();
					file.createNewFile();
				}
			}
			FileOutputStream out = new FileOutputStream(file, true);
			out.write(strBuf.toString().getBytes("utf-8"));
			out.close();
		}
		catch (Exception e) {
			return null;
		}
		return Path;
	}
	public static String GetBooleanStr(boolean v) {
		if (v) {
			return "有";
		}
		else {
			return "无";
		}
	}
	public static void ClearCardList() {
		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
		tableModel.setRowCount(0);// 清除原有行
	}
	public static void FillCardToList() {
		if (mCardList == null) {
			return;
		}
		if (mCardList.size() == 0) {
			return;
		}
		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
		ClearCardList();
		int Index = 1;
		for (CardDetail cd : mCardList) {
			Object[] row = CardDetailToRow(cd, Index);
			// 添加数据到表格
			tableModel.addRow(row);
			Index += 1;
		}
		// 更新表格
		tblCard.invalidate();
	}
	public static Object[] CardDetailToRow(CardDetail cd, int Index) {
		String OpenTimes;// 有效期
		String Privilege = PrivilegeList[0];// "无";//特权
		if (cd.OpenTimes == 65535) {
			OpenTimes = OpenTimesUnlimited;// "无限制";
		}
		else {
			OpenTimes = String.valueOf(cd.OpenTimes);
		}
		if (cd.IsPrivilege()) {
			Privilege = PrivilegeList[1];// "首卡特权卡";
		}
		else if (cd.IsTiming()) {
			Privilege = PrivilegeList[2];// "常开特权卡";
		}
		else if (cd.IsGuardTour()) {
			Privilege = PrivilegeList[3];// "巡更卡";
		}
		else if (cd.IsAlarmSetting()) {
			Privilege = PrivilegeList[4];// "防盗设置卡";
		}
		Object[] row = new Object[]{Index, cd.GetCardData(), cd.Password.replaceAll("F", ""), TimeUtil.FormatTimeHHmm(cd.Expiry), CardStatusList[cd.CardStatus], OpenTimes, Privilege, GetBooleanStr(cd.GetDoor(1)), cd.GetTimeGroup(1), GetBooleanStr(cd.GetDoor(2)), cd.GetTimeGroup(2), GetBooleanStr(cd.GetDoor(3)), cd.GetTimeGroup(3), GetBooleanStr(cd.GetDoor(4)), cd.GetTimeGroup(4), GetBooleanStr(cd.HolidayUse)};
		return row;
	}
	public static CardDetail GetCardDetail() {
		String Card = txtCardData.getText();
		String Password = txtCardPassword.getText();
		String Expiry = TxtCardExpiry.getText();
		String OpenTimes = txtOpenTimes.getText();
		int CardStatus = cmbCardStatus.getSelectedIndex();
		int iOpenTimes = 0;
		Calendar CardExpiry = Calendar.getInstance();
		long CardData = 0;
		if (StringUtil.IsNullOrEmpty(Card)) {
			JOptionPane.showMessageDialog(null, "卡号不能为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (!StringUtil.IsNum(Card) || Card.length() > 10) {
			JOptionPane.showMessageDialog(null, "卡号为1-10位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		CardData = Long.parseLong(Card);
		if (CardData > UInt32Util.UINT32_MAX || CardData == 0) {
			JOptionPane.showMessageDialog(null, "卡号不能大于4294967295！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (!StringUtil.IsNullOrEmpty(Password)) {
			if (Password.length() > 8 || Password.length() < 4 || !StringUtil.IsNum(Password)) {
				JOptionPane.showMessageDialog(null, "密码为4-8位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date dt1 = df.parse(Expiry);
			CardExpiry.setTime(dt1);
			dt1 = CardExpiry.getTime();
		}
		catch (Exception exception) {
			JOptionPane.showMessageDialog(null, "截止日期格式不正确（yyyy-MM-dd hh:mm）！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (OpenTimes.equals(OpenTimesUnlimited)) {
			OpenTimes = "65535";
		}
		if (!StringUtil.IsNum(OpenTimes) || OpenTimes.length() > 5) {
			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		iOpenTimes = Integer.parseInt(OpenTimes);
		if (iOpenTimes > 65535) {
			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
		CardDetail cd = new CardDetail();// 设定卡号
		try {
			cd.SetCardData(Card);
		}
		catch (Exception e) {
		}
		cd.Password = Password;// 设定密码
		cd.Expiry = CardExpiry;// 设定有效期
		cd.OpenTimes = iOpenTimes;// 开门次数
		cd.CardStatus = (byte) CardStatus;
		// 设定4个门的开门时段
		for (int i = 1; i <= 4; i++) {
			cd.SetTimeGroup(i, 1);// 每个门都设定为1门
			cd.SetDoor(i, doors[i - 1].isSelected());// 设定每个门权限
		}
		cd.SetNormal();// 设定卡片没有特权
		cd.HolidayUse = true;// 设定受节假日限制。
		return cd;
	}
	public static Net.PC15.FC89H.Command.Data.CardDetail GetFC89HCardDetail() {
		String Card = txtCardData.getText();
		String Password = txtCardPassword.getText();
		String Expiry = TxtCardExpiry.getText();
		String OpenTimes = txtOpenTimes.getText();
		int CardStatus = cmbCardStatus.getSelectedIndex();
		int iOpenTimes = 0;
		Calendar CardExpiry = Calendar.getInstance();
		long CardData = 0;
		if (StringUtil.IsNullOrEmpty(Card)) {
			JOptionPane.showMessageDialog(null, "卡号不能为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (!StringUtil.IsNum(Card) || Card.length() > 10) {
			JOptionPane.showMessageDialog(null, "卡号为1-10位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		CardData = Long.parseLong(Card);
		if (CardData > UInt32Util.UINT32_MAX || CardData == 0) {
			JOptionPane.showMessageDialog(null, "卡号不能大于4294967295！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (!StringUtil.IsNullOrEmpty(Password)) {
			if (Password.length() > 8 || Password.length() < 4 || !StringUtil.IsNum(Password)) {
				JOptionPane.showMessageDialog(null, "密码为4-8位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			Date dt1 = df.parse(Expiry);
			CardExpiry.setTime(dt1);
			dt1 = CardExpiry.getTime();
		}
		catch (Exception exception) {
			JOptionPane.showMessageDialog(null, "截止日期格式不正确（yyyy-MM-dd hh:mm）！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (OpenTimes.equals(OpenTimesUnlimited)) {
			OpenTimes = "65535";
		}
		if (!StringUtil.IsNum(OpenTimes) || OpenTimes.length() > 5) {
			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		iOpenTimes = Integer.parseInt(OpenTimes);
		if (iOpenTimes > 65535) {
			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
		Net.PC15.FC89H.Command.Data.CardDetail cd = new Net.PC15.FC89H.Command.Data.CardDetail();// 设定卡号
		cd.Password = Password;// 设定密码
		cd.Expiry = CardExpiry;// 设定有效期
		cd.OpenTimes = iOpenTimes;// 开门次数
		cd.CardStatus = (byte) CardStatus;
		// 设定4个门的开门时段
		for (int i = 1; i <= 4; i++) {
			cd.SetTimeGroup(i, 1);// 每个门都设定为1门
			cd.SetDoor(i, doors[i - 1].isSelected());// 设定每个门权限
		}
		cd.SetNormal();// 设定卡片没有特权
		cd.HolidayUse = true;// 设定受节假日限制。
		return cd;
	}
	/**
	 * 打印记录详情
	 * @param dt
	 * @param buf
	 */
	public static void PrintTransactionDatabaseDetail(String TransactionName, TransactionDetail dt, StringBuilder buf) {
		buf.append(TransactionName);
		buf.append("：容量：");
		buf.append(dt.DataBaseMaxSize);
		buf.append("：可读数量：");
		buf.append(dt.readable());
		buf.append("：写索引：");
		buf.append(dt.WriteIndex);
		buf.append("：读索引：");
		buf.append(dt.ReadIndex);
		buf.append("：循环存储：");
		buf.append(dt.IsCircle);
	}
	/**
	 * 打印记录日志
	 * @param TransactionList ArrayList<AbstractTransaction>
	 * @param log StringBuilder
	 */
	public static void PrintTransactionDatabase(ArrayList<AbstractTransaction> TransactionList, StringBuilder log, String[] sTransactionList) {
		int Quantity = TransactionList.size();
		int code = 0;
		String sCode = null;
		for (int i = 0; i < Quantity; i++) {
			AbstractTransaction Transaction = TransactionList.get(i);
			log.append("序号：");
			log.append(Transaction.SerialNumber);
			if (!Transaction.IsNull()) {
				code = Transaction.TransactionCode();
				sCode = null;
				log.append("，时间：");
				log.append(TimeUtil.FormatTime(Transaction.TransactionDate()));
				log.append("，事件：");// 类型
				log.append(code);
				if (code < 255) {
					sCode = sTransactionList[code];
					if (StringUtil.IsNullOrEmpty(sCode)) {
						sCode = "未知类型";
					}
				}
				else {
					sCode = "未知类型";
				}
				log.append("--");
				log.append(sCode);
				if (Transaction instanceof AbstractDoorTransaction) {
					AbstractDoorTransaction dt = (AbstractDoorTransaction) Transaction;
					if (dt.Door != 255) {
						log.append("，门号：");
						log.append(dt.Door);
					}
				}
				if (Transaction instanceof CardTransaction) {
					CardTransaction cardTrn = (CardTransaction) Transaction;
					log.append("，卡号：");
					log.append(cardTrn.CardData);
					log.append("，门号：");
					log.append(cardTrn.DoorNum());
					if (cardTrn.IsEnter()) {
						log.append("，进门读卡");
					}
					else {
						log.append("，出门读卡");
					}
				}
			}
		}
	}
	// public static int SearchTimes = 0;
	// public static int SearchNetFlag;
	public static void PrintSearchDoor(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst, StringBuilder log) {
		ByteBuf tmpBuf = ByteUtil.ALLOCATOR.buffer(256, 300);
		TCPDetail td = new TCPDetail();
		for (SearchEquptOnNetNum_Result.SearchResult r : lst) {
			log.append("SN：");
			log.append(r.SN);
			if (r.ResultData != null) {
				log.append("，附带数据：");
				log.append(ByteUtil.ByteToHex(r.ResultData));
				if (r.ResultData.length == 0x89) {
					tmpBuf.readerIndex(0);
					tmpBuf.writerIndex(0);
					tmpBuf.writeBytes(r.ResultData);
					td.SetBytes(tmpBuf);
					;
					PirntTCPDetail(td, log);
				}
			}
		}
	}
	/**
	 * 设定控制器的网络标志
	 * @param lst lst
	 */
	public static void SetDoorNetFlag(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst) {
		// ByteBuf tmpBuf = ByteUtil.ALLOCATOR.buffer(256, 300);
		// TCPDetail td = new TCPDetail();
		for (SearchEquptOnNetNum_Result.SearchResult r : lst) {
			/*
			 * if (r.ResultData != null) {
			 *
			 * if (r.ResultData.length == 0x89) { tmpBuf.readerIndex(0);
			 * tmpBuf.writerIndex(0); tmpBuf.writeBytes(r.ResultData); td.SetBytes(tmpBuf);
			 *
			 *
			 *
			 *
			 * } }
			 */
			CommandDetail detail = new CommandDetail();
			UDPDetail udp = new UDPDetail("255.255.255.255", 8101);
			udp.Broadcast = true;
			detail.Connector = udp;
			detail.Identity = new FC8800Identity(r.SN, FC8800Command.NULLPassword, E_ControllerType.FC8800);
			SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(detail, SearchNetFlag);
			WriteEquptNetNum cmd = new WriteEquptNetNum(par);
			_Allocator.AddCommand(cmd);
		}
	}
	public static CommandDetail getCommandDetail() {
		CommandDetail detail = new CommandDetail();
		String ip = "0", strPort = "0";
		String udpIp = "0", udpPort = "0";
		String tcpServerIp = "0", tcpServerPort = "0";
		//		if (RadTCPClient.isSelected()) {
		//
		//			strPort = txtLocalPort.getText();
		//		}
		//		if (RadUDP.isSelected()) {
		//			udpIp = txtUDPRemoteIP.getText();
		//			udpPort = txtUDPRemotePort.getText();
		//		}
		//		udpIp = txtUdpIp.getText();
		//		udpPort = txtUDPRemotePort.getText();
		ip = txtTCPClienpIP.getText();
		strPort = txtTCPClienpPort.getText();
		myLog.info("tcpclient:"+txtTCPClienpIP.getText() + " " + txtTCPClienpPort.getText());
		myLog.info("udp:"+txtUdpIp.getText() + " " + txtUdpPort.getText());
		myLog.info("tcpserver:"+txtTcpServerPort.getText() + " " + txtTcpServerIp.getText());
		int iPort = Integer.parseInt(strPort);
		if (ip.length() == 0) {
			JOptionPane.showMessageDialog(null, "必须输入IP地址！", "错误", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (iPort <= 0 || iPort > 65535) {
			JOptionPane.showMessageDialog(null, "TCP端口取值范围：1-65535！", "错误", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		detail.Connector = new TCPClientDetail(ip, iPort);
		//		if (RadUDP.isSelected()) {
		//			UDPDetail udpd = new UDPDetail(ip, iPort);
		//			udpd.Broadcast = true;
		//			// udpd.LocalPort = 10088;//设定本地绑定端口号
		//			detail.Connector = udpd;
		//		}
		String sn, pwd;
		sn = txtSN.getText();
		if (sn.length() != 16) {
			JOptionPane.showMessageDialog(null, "SN必须是16位！", "错误", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		pwd = txtPassword.getText();
		if (pwd.length() != 8) {
			JOptionPane.showMessageDialog(null, "通讯密码必须是8位！", "错误", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		detail.Identity = new FC8800Identity(sn, pwd, E_ControllerType.FC8800);
		return detail;
	}
	public static void AddCommandResultCallback(String sCommand, CommandResultCallback callback) {
		if (!CommandResult.containsKey(sCommand)) {
			CommandResult.put(sCommand, callback);
		}
		/*
		 * else { CommandResult.remove(sCommand); CommandResult.put(sCommand, callback);
		 * }
		 */
	}
	/**
	 * 记录事件的描述到StringBuilder中
	 */
	public static void logTransaction(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf, String[] sTransactionList) {
		AbstractTransaction Transaction = WatchTransaction.EventData;
		// 打印消息类型
		if (WatchTransaction.CmdIndex >= 1 && WatchTransaction.CmdIndex <= 6) {
			strBuf.append(mWatchTypeNameList[WatchTransaction.CmdIndex]);// 类型
		}
		else if (WatchTransaction.CmdIndex == 0x23 && WatchTransaction.CmdIndex == 0x22) {
			int tmpType = WatchTransaction.CmdIndex - 27;
			strBuf.append(mWatchTypeNameList[tmpType]);// 类型
		}
		else {
			strBuf.append("未知消息类型：");// 类型
			strBuf.append(WatchTransaction.CmdIndex);
		}
		strBuf.append("，消息时间：");
		strBuf.append(TimeUtil.FormatTime(Transaction.TransactionDate()));
		if (sTransactionList == null) {
			return;
		}
		int code = Transaction.TransactionCode();
		String sCode = null;
		strBuf.append("消息代码：");// 类型
		strBuf.append(code);
		if (code < 255) {
			sCode = sTransactionList[code];
			if (StringUtil.IsNullOrEmpty(sCode)) {
				sCode = "未知类型";
			}
		}
		else {
			sCode = "未知类型";
		}
		strBuf.append("--");
		strBuf.append(sCode);
		if (Transaction instanceof AbstractDoorTransaction) {
			AbstractDoorTransaction dt = (AbstractDoorTransaction) Transaction;
			if (dt.Door != 255) {
				strBuf.append("，门号：");
				strBuf.append(dt.Door);
			}
		}
		if (Transaction instanceof CardTransaction) {
			CardTransaction cardTrn = (CardTransaction) WatchTransaction.EventData;
			strBuf.append("卡号：");
			strBuf.append(cardTrn.CardData);
			strBuf.append("，门号：");
			strBuf.append(cardTrn.DoorNum());
			if (cardTrn.IsEnter()) {
				strBuf.append("，进门读卡");
			}
			else {
				strBuf.append("，出门读卡");
			}
		}
	}
	/**
	 * 打印监控消息
	 * @param WatchTransaction 监控消息
	 * @param strBuf 日志缓冲区
	 */
	static void PrintWatchEvent(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf) {
		switch (WatchTransaction.CmdIndex) {
			case 1:// 读卡信息
				logTransaction(WatchTransaction, strBuf, mCardTransactionList);
				break;
			case 2:// 出门开关信息
				logTransaction(WatchTransaction, strBuf, mButtonTransactionList);
				break;
			case 3:// 门磁信息
				logTransaction(WatchTransaction, strBuf, mDoorSensorTransactionList);
				break;
			case 4:// 远程开门信息
				logTransaction(WatchTransaction, strBuf, mSoftwareTransactionList);
				break;
			case 5:// 报警信息
				logTransaction(WatchTransaction, strBuf, mAlarmTransactionList);
				break;
			case 6:// 系统信息
				logTransaction(WatchTransaction, strBuf, mSystemTransactionList);
				break;
			default:
				logTransaction(WatchTransaction, strBuf, null);
		}
	}
	static void GetCommandDetail(StringBuilder strBuf, INCommand cmd) {
		if (cmd == null) {
			strBuf.append("命令类型：null");
			return;
		}
		strBuf.append("命令类型：");
		strBuf.append(GetCommandName(cmd));
		if (cmd.getCommandParameter() != null) {
			strBuf.append("，SN:");
			CommandDetail det = cmd.getCommandParameter().getCommandDetail();
			strBuf.append(det.Identity.GetIdentity());
			strBuf.append(",");
			GetConnectorDetail(strBuf, det.Connector);
		}
	}
	@Contract("null -> !null")
	static String GetCommandName(INCommand cmd) {
		if (cmd == null) {
			return "null";
		}
		String sKey = cmd.getClass().getName();
		return FormUtils.CommandName.getOrDefault(sKey, sKey);
	}
	static void GetConnectorDetail(StringBuilder strBuf, ConnectorDetail conn) {
		if (conn == null) {
			strBuf.append("ConnectorDetail：null");
			return;
		}
		if (conn instanceof TCPClientDetail) {
			TCPClientDetail tcp = (TCPClientDetail) conn;
			strBuf.append("TCP远程设备:");
			strBuf.append(tcp.IP);
			strBuf.append(":");
			strBuf.append(tcp.Port);
		}
		else if (conn instanceof UDPDetail) {
			UDPDetail udp = (UDPDetail) conn;
			strBuf.append("UDP远程设备:");
			strBuf.append(udp.IP);
			strBuf.append(":");
			strBuf.append(udp.Port);
		}
		// strBuf.append(",");
	}
}
