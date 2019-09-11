package fcardiodemo;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Command.INCommand;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.Connector.TCPClient.TCPClientDetail;
import Net.PC15.Connector.UDP.UDPDetail;
import Net.PC15.Data.AbstractTransaction;
import Net.PC15.FC8800.Command.Card.*;
import Net.PC15.FC8800.Command.Card.Parameter.*;
import Net.PC15.FC8800.Command.Card.Result.*;
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.DateTime.ReadTime;
import Net.PC15.FC8800.Command.DateTime.Result.ReadTime_Result;
import Net.PC15.FC8800.Command.DateTime.WriteTime;
import Net.PC15.FC8800.Command.Door.*;
import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
import Net.PC15.FC8800.Command.Door.Parameter.RemoteDoor_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.*;
import Net.PC15.FC8800.Command.System.Parameter.*;
import Net.PC15.FC8800.Command.System.Result.*;
import Net.PC15.FC8800.Command.Transaction.*;
import Net.PC15.FC8800.Command.Transaction.Parameter.*;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseDetail_Result;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabase_Result;
import Net.PC15.FC8800.FC8800Identity;
import Net.PC15.Util.ByteUtil;
import Net.PC15.Util.StringUtil;
import Net.PC15.Util.TimeUtil;
import Net.PC15.Util.UInt32Util;
import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;
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
import java.util.*;

import static fcardiodemo.FormMain.*;
/***
 * @class FormMainFunctionUtils
 * @description 方法util
 * @author zch
 * @date 2019/9/11
 * @version V0.0.1.201909111632.01
 * @modfiyDate 201909111632
 * @createDate 201909111632
 * @package fcardiodemo
 */
public class FormMainFunctionUtils {
	static void txtLogMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getClickCount() == 3) {
			strLog.delete(0, strLog.length());
			txtLog.setText("");
		}
	}
	/**
	 * 读取新记录
	 * @param evt evt
	 */
	static void butReadTransactionDatabaseActionPerformed(java.awt.event.ActionEvent evt) {
		// 读取新记录
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 1000;
		int type = cmbTransactionType.getSelectedIndex() + 1;
		String strPacketSize = txtReadTransactionDatabasePacketSize.getText();
		if (!StringUtil.IsNum(strPacketSize) || strPacketSize.length() > 3) {
			JOptionPane.showMessageDialog(null, "单次读取数量必须为数字，取值范围1-300！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int packetSize = Integer.parseInt(strPacketSize);
		if (packetSize > 300 || packetSize < 0) {
			JOptionPane.showMessageDialog(null, "单次读取数量必须为数字，取值范围1-300！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String strQuantity = txtReadTransactionDatabaseQuantity.getText();
		if (!StringUtil.IsNum(strQuantity) || strQuantity.length() > 6) {
			JOptionPane.showMessageDialog(null, "读新记录数量必须是数字，取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int quantity = Integer.parseInt(strQuantity);
		if (quantity < 0) {
			JOptionPane.showMessageDialog(null, "读新记录数量必须大于0！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ReadTransactionDatabase_Parameter par = new ReadTransactionDatabase_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
		par.PacketSize = packetSize;
		par.Quantity = quantity;
		// 徐铭康修改
		// ReadTransactionDatabase cmd = new ReadTransactionDatabase(par);
		ReadTransactionDatabase cmd = new ReadTransactionDatabase(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) y;
			// x.append("记录类型：");
			x.append("记录类型：");
			x.append(mWatchTypeNameList[result.DatabaseType.getValue()]);
			x.append("，本次读取数量：");
			x.append(result.Quantity);
			x.append("，未读取新记录数量：");
			x.append(result.readable);
			if (result.Quantity > 0) {
				// 开始写出记录日志
				StringBuilder log = new StringBuilder(result.Quantity * 100);
				String[] sTransactionList = null;
				switch (result.DatabaseType) {
					case OnCardTransaction:
						sTransactionList = mCardTransactionList;
						break;
					case OnButtonTransaction:
						sTransactionList = mButtonTransactionList;
						break;
					case OnDoorSensorTransaction:
						sTransactionList = mDoorSensorTransactionList;
						break;
					case OnSoftwareTransaction:
						sTransactionList = mSoftwareTransactionList;
						break;
					case OnAlarmTransaction:
						sTransactionList = mAlarmTransactionList;
						break;
					case OnSystemTransaction:
						sTransactionList = mSystemTransactionList;
						break;
					default:
						Logger.getRootLogger().info("");
				}
				PrintTransactionDatabase(result.TransactionList, log, sTransactionList);
				String path = WriteFile("门禁调试器记录数据库", log, false);
				if (path == null) {
					x.append("写日志失败！");
				}
				else {
					x.append("以保存到日志文件，");
					x.append(path);
				}
				if (result.Quantity < 1000) {
					x.append(log);
				}
			}
		});
		_Allocator.AddCommand(cmd);
	}
	static void butWriteTransactionDatabaseWriteIndexActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		int type = cmbTransactionType.getSelectedIndex() + 1;
		String strWriteIndex = txtTransactionDatabaseWriteIndex.getText();
		if (!StringUtil.IsNum(strWriteIndex) || strWriteIndex.length() > 6) {
			JOptionPane.showMessageDialog(null, "写索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int index = Integer.parseInt(strWriteIndex);
		if (index > 160000 || index < 0) {
			JOptionPane.showMessageDialog(null, "写索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		WriteTransactionDatabaseWriteIndex_Parameter par = new WriteTransactionDatabaseWriteIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
		par.WriteIndex = index;
		WriteTransactionDatabaseWriteIndex cmd = new WriteTransactionDatabaseWriteIndex(par);
		_Allocator.AddCommand(cmd);
	}
	static void butWriteTransactionDatabaseReadIndexActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		int type = cmbTransactionType.getSelectedIndex() + 1;
		String strReadIndex = txtTransactionDatabaseReadIndex.getText();
		if (!StringUtil.IsNum(strReadIndex) || strReadIndex.length() > 6) {
			JOptionPane.showMessageDialog(null, "读索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int index = Integer.parseInt(strReadIndex);
		if (index > 160000 || index < 0) {
			JOptionPane.showMessageDialog(null, "读索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		WriteTransactionDatabaseReadIndex_Parameter par = new WriteTransactionDatabaseReadIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
		par.ReadIndex = index;
		par.IsCircle = chkTransactionIsCircle.isSelected();
		WriteTransactionDatabaseReadIndex cmd = new WriteTransactionDatabaseReadIndex(par);
		_Allocator.AddCommand(cmd);
	}
	static void butReadTransactionDatabaseByIndexActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 1000;
		int type = cmbTransactionType.getSelectedIndex() + 1;
		String strReadIndex = txtReadTransactionDatabaseByIndex.getText();
		if (!StringUtil.IsNum(strReadIndex) || strReadIndex.length() > 6) {
			JOptionPane.showMessageDialog(null, "读记录起始索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int index = Integer.parseInt(strReadIndex);
		if (index > 160000 || index < 0) {
			JOptionPane.showMessageDialog(null, "读记录起始索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String strQuantity = txtReadTransactionDatabaseByQuantity.getText();
		if (!StringUtil.IsNum(strQuantity) || strQuantity.length() > 3) {
			JOptionPane.showMessageDialog(null, "读记录数量必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		int Quantity = Integer.parseInt(strQuantity);
		if (Quantity > 400 || Quantity <= 0) {
			JOptionPane.showMessageDialog(null, "读记录数量取值范围1-400！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		ReadTransactionDatabaseByIndex_Parameter par = new ReadTransactionDatabaseByIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
		par.ReadIndex = index;
		par.Quantity = Quantity;
		// ReadTransactionDatabaseByIndex cmd = new ReadTransactionDatabaseByIndex(par);
		ReadTransactionDatabaseByIndex cmd = new ReadTransactionDatabaseByIndex(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadTransactionDatabaseByIndex_Result result = (ReadTransactionDatabaseByIndex_Result) y;
			x.append("记录类型：");
			x.append(mWatchTypeNameList[result.DatabaseType.getValue()]);
			x.append("，起始号：");
			x.append(result.ReadIndex);
			x.append("，读取数量：");
			x.append(result.Quantity);
			if (result.Quantity > 0) {
				// 开始写出记录日志
				StringBuilder log = new StringBuilder(result.Quantity * 100);
				String[] sTransactionList = null;
				switch (result.DatabaseType) {
					case OnCardTransaction:
						sTransactionList = mCardTransactionList;
						break;
					case OnButtonTransaction:
						sTransactionList = mButtonTransactionList;
						break;
					case OnDoorSensorTransaction:
						sTransactionList = mDoorSensorTransactionList;
						break;
					case OnSoftwareTransaction:
						sTransactionList = mSoftwareTransactionList;
						break;
					case OnAlarmTransaction:
						sTransactionList = mAlarmTransactionList;
						break;
					case OnSystemTransaction:
						sTransactionList = mSystemTransactionList;
						break;
				}
				PrintTransactionDatabase(result.TransactionList, log, sTransactionList);
				x.append(log);
			}
		});
		_Allocator.AddCommand(cmd);
	}
	static void butClearTransactionDatabaseActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		int type = cmbTransactionType.getSelectedIndex() + 1;
		ClearTransactionDatabase_Parameter par = new ClearTransactionDatabase_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
		ClearTransactionDatabase cmd = new ClearTransactionDatabase(par);
		_Allocator.AddCommand(cmd);
	}
	static void butTransactionDatabaseEmptyActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		TransactionDatabaseEmpty cmd = new TransactionDatabaseEmpty(par);
		_Allocator.AddCommand(cmd);
	}
	/**
	 * 读取记录数据库详情
	 * @param evt evt
	 */
	static void butReadTransactionDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		ReadTransactionDatabaseDetail cmd = new ReadTransactionDatabaseDetail(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadTransactionDatabaseDetail_Result result = (ReadTransactionDatabaseDetail_Result) y;
			PrintTransactionDatabaseDetail(mWatchTypeNameList[1], result.DatabaseDetail.CardTransactionDetail, x);
			PrintTransactionDatabaseDetail(mWatchTypeNameList[2], result.DatabaseDetail.ButtonTransactionDetail, x);
			PrintTransactionDatabaseDetail(mWatchTypeNameList[3], result.DatabaseDetail.DoorSensorTransactionDetail, x);
			PrintTransactionDatabaseDetail(mWatchTypeNameList[4], result.DatabaseDetail.SoftwareTransactionDetail, x);
			PrintTransactionDatabaseDetail(mWatchTypeNameList[5], result.DatabaseDetail.AlarmTransactionDetail, x);
			PrintTransactionDatabaseDetail(mWatchTypeNameList[6], result.DatabaseDetail.SystemTransactionDetail, x);
		});
		_Allocator.AddCommand(cmd);
	}
	static void butDeleteCardByListActionPerformed(java.awt.event.ActionEvent evt) {
		// 删除列表中的卡片
		if (mCardList == null) {
			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 5000;
		// 此函数超时时间设定长一些
		int ilstLen = mCardList.size();
		// 徐铭康修改
		// DeleteCard cmd = new DeleteCard(par);
		FC8800Command cmd = null;
		String[] lst = new String[ilstLen];
		for (int i = 0; i < ilstLen; i++) {
			lst[i] = mCardList.get(i).GetCardData();
		}
		DeleteCard_Parameter par = new DeleteCard_Parameter(dt, lst);
		cmd = new DeleteCard(par);
		_Allocator.AddCommand(cmd);
	}
	static void butWriteCardListBySortActionPerformed(java.awt.event.ActionEvent evt) {
		// 将列表中的卡片上传至排序区
		if (mCardList == null) {
			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 5000;// 此函数超时时间设定长一些
		WriteCardListBySort_Parameter par = new WriteCardListBySort_Parameter(dt, mCardList);
		// 徐铭康修改
		// WriteCardListBySort cmd = new WriteCardListBySort(par);
		WriteCardListBySort cmd = new WriteCardListBySort(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			WriteCardListBySort_Result result = (WriteCardListBySort_Result) y;
			ArrayList<? extends CardDetail> _list = result.CardList;
			x.append("上传完毕");
			if (result.FailTotal > 0) {
				x.append("失败数量：");
				x.append(result.FailTotal);
				x.append("，卡号列表：");
				for (CardDetail c : _list) {
					x.append(c.GetCardData());
				}
			}
		});
		_Allocator.AddCommand(cmd);
	}
	static void butWriteCardListBySequenceActionPerformed(java.awt.event.ActionEvent evt) {
		// 将列表中的卡片上传至非排序区
		if (mCardList == null) {
			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 8000;// 此函数超时时间设定长一些
		// 徐铭康 修改
		WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(dt, mCardList);
		// WriteCardListBySequence cmd = new WriteCardListBySequence(par);
		FC8800Command cmd = new WriteCardListBySequence(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			WriteCardListBySequence_Result result = (WriteCardListBySequence_Result) y;
			ArrayList<? extends CardDetail> _list = result.CardList;
			x.append("上传完毕");
			if (result.FailTotal > 0) {
				x.append("失败数量：");
				x.append(result.FailTotal);
				x.append("，卡号列表：");
				for (CardDetail c : _list) {
					x.append(c.GetCardData());
				}
			}
		});
		_Allocator.AddCommand(cmd);
	}
	static void butCardListAutoCreateActionPerformed(java.awt.event.ActionEvent evt) {
		// 自动创建测试卡列表
		int maxSize = 1000;
		String strSize = txtCardAutoCreateSzie.getText();
		if (!StringUtil.IsNum(strSize) || strSize.length() > 6) {
			JOptionPane.showMessageDialog(null, "待生成的数量为数字，取值范围1-120000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		maxSize = Integer.parseInt(strSize);
		if (mCardList == null) {
			mCardList = new ArrayList<>(10000);
		}
		if ((maxSize + mCardList.size()) > 120000) {
			JOptionPane.showMessageDialog(null, "待生成的数量和列表中的卡数相加超过12万！", "卡片管理", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Random rnd = new Random();
		int max = 90000000;
		int min = 10000000;
		int iSearch = 0;
		CardDetail surCard = GetCardDetail();
		if (surCard == null) {
			return;
		}
		Collections.sort(mCardList);
		// 徐铭康修改
		ArrayList<CardDetail> tmplst = new ArrayList<>(1500);
		// Calendar time=Calendar.getInstance();
		while (maxSize > 0) {
			long card = rnd.nextInt(max) % (max - min + 1) + min;
			// 徐铭康修改
			// CardDetail cd = new CardDetail(card);
			CardDetail cd = new CardDetail();
			try {
				cd.SetCardData(String.valueOf(card));
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, "生成随机卡号异常", "卡片管理", JOptionPane.ERROR_MESSAGE);
				return;
			}
			iSearch = CardDetail.SearchCardDetail(mCardList, cd);
			if (iSearch == -1) {
				if (tmplst.indexOf(cd) == -1) {
					cd.Password = surCard.Password;// 设定密码
					cd.Expiry = surCard.Expiry;// 设定有效期
					cd.OpenTimes = surCard.OpenTimes;// 开门次数
					cd.CardStatus = surCard.CardStatus;
					// 设定4个门的开门时段
					for (int i = 1; i <= 4; i++) {
						cd.SetTimeGroup(i, 1);// 每个门都设定为1门
						cd.SetDoor(i, surCard.GetDoor(i));// 设定每个门权限
					}
					cd.SetNormal();// 设定卡片没有特权
					cd.HolidayUse = true;// 设定受节假日限制。
					tmplst.add(cd);
					if (tmplst.size() >= 1000) {
						mCardList.addAll(tmplst);
						Collections.sort(mCardList);
						tmplst.clear();
					}
					maxSize--;
				}
			}
		}
		if (tmplst.size() > 0) {
			mCardList.addAll(tmplst);
			Collections.sort(mCardList);
			tmplst.clear();
		}
		// Calendar endtime=Calendar.getInstance();
		// int hs=(int)(endtime.getTimeInMillis()- time.getTimeInMillis());
		// System.out.println("耗时：" + hs);
		FillCardToList();
	}
	static void butDeleteCardActionPerformed(java.awt.event.ActionEvent evt) {
		// 删除卡片
		CardDetail cd = GetCardDetail();
		if (cd == null) {
			return;
		}
		// 读取控制器中的卡片数据库信息
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		// 此函数超时时间设定长一些
		dt.Timeout = 5000;
		// 徐铭康修改
		// DeleteCard cmd = new DeleteCard(par);
		String[] lst = new String[1];
		lst[0] = cd.GetCardData();
		DeleteCard_Parameter par = new DeleteCard_Parameter(dt, lst);
		DeleteCard cmd = new DeleteCard(par);
		_Allocator.AddCommand(cmd);
	}
	static void butUploadCardActionPerformed(java.awt.event.ActionEvent evt) {
		// 上传卡片至非排序区
		CardDetail cd = GetCardDetail();
		if (cd == null) {
			return;
		}
		ArrayList<CardDetail> lst = new ArrayList<>(1);
		lst.add(cd);
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 5000;// 此函数超时时间设定长一些
		// 徐铭康修改
		WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(dt, lst);
		// WriteCardListBySequence cmd = new WriteCardListBySequence(par);
		FC8800Command cmd = new WriteCardListBySequence(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			WriteCardListBySequence_Result result = (WriteCardListBySequence_Result) y;
			x.append("上传完毕");
			ArrayList<? extends CardDetail> _list = result.CardList;
			if (result.FailTotal > 0) {
				x.append("失败数量：");
				x.append(result.FailTotal);
				x.append("，卡号列表：");
				for (CardDetail c : _list) {
					x.append(c.GetCardData());
				}
			}
		});
		_Allocator.AddCommand(cmd);
	}
	static void butCardListClearActionPerformed(java.awt.event.ActionEvent evt) {
		ClearCardList();
		mCardList.clear();
		mCardList = null;
	}
	static void butAddCardToListActionPerformed(java.awt.event.ActionEvent evt) {
		CardDetail cd = GetCardDetail();
		if (cd == null) {
			return;
		}
		if (mCardList == null) {
			mCardList = new ArrayList<>(1000);
		}
		// 检查重复
		int iIndex = mCardList.indexOf(cd);
		if (iIndex > -1) {
			mCardList.remove(iIndex);
		}
		mCardList.add(cd);
		if (iIndex > -1) {
			FillCardToList();// 刷新列表
		}
		else {
			Object[] row = CardDetailToRow(cd, mCardList.size());
			DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
			tableModel.addRow(row);
		}
	}
	static void butReadCardDetailActionPerformed(java.awt.event.ActionEvent evt) {
		CardDetail cd = GetCardDetail();
		if (cd == null) {
			return;
		}
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 5000;// 此函数超时时间设定长一些
		ReadCardDetail_Parameter par = new ReadCardDetail_Parameter(dt, cd.GetCardData());
		// ReadCardDetail cmd = new ReadCardDetail(par);
		FC8800Command cmd = new ReadCardDetail(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadCardDetail_Result result = (ReadCardDetail_Result) y;
			if (result.IsReady) {
				CardDetail card = (CardDetail) result.Card;
				x.append("卡片在数据库中存储，卡片信息：");
				Object[] arr = CardDetailToRow(card, 0);
				StringBuilder builder = new StringBuilder(200);
				builder.append("卡号：");
				builder.append(arr[1]);// cd.CardData
				builder.append("，密码：");
				builder.append(arr[2]);// cd.Password
				builder.append("，有效期：");
				builder.append(arr[3]);// cd.Expiry
				builder.append("有效次数：");
				builder.append(arr[5]);// OpenTimes
				builder.append("，特权：");
				builder.append(arr[6]);// Privilege
				builder.append("，卡状态：");
				builder.append(arr[4]);// cd.CardStatus
				builder.append("权限和时段：");
				int arrIndex = 7;
				for (int i = 1; i <= 4; i++) {
					builder.append("门");
					builder.append(i);
					builder.append("：");
					builder.append(arr[arrIndex]);
					arrIndex++;
					builder.append("权限");
					builder.append(",开门时段:");
					builder.append(arr[arrIndex]);
					arrIndex++;
					builder.append("；");
				}
				builder.append("节假日限制:");
				builder.append(arr[arrIndex]);
				x.append(builder);
				builder = null;
			}
			else {
				x.append("卡片未在数据库中存储！");
			}
		});
		_Allocator.AddCommand(cmd);
	}
	static void butClearCardDataBaseActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 5000;
		int iType = cmbCardDataBaseType.getSelectedIndex() + 1;
		ClearCardDataBase_Parameter par = new ClearCardDataBase_Parameter(dt, iType);
		ClearCardDataBase cmd = new ClearCardDataBase(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) y;
			x.append("清空区域：");
			x.append(cmbCardDataBaseType.getSelectedItem());
		});
		_Allocator.AddCommand(cmd);
	}
	static void butReadCardDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {
		// 读取控制器中的卡片数据库信息
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		ReadCardDatabaseDetail cmd = new ReadCardDatabaseDetail(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) y;
			x.append("排序区卡容量：");
			x.append(result.SortDataBaseSize);
			x.append("；排序区卡数量：");
			x.append(result.SortCardSize);
			x.append("非排序区卡容量：");
			x.append(result.SequenceDataBaseSize);
			x.append("；非排序区卡数量：");
			x.append(result.SequenceCardSize);
		});
		_Allocator.AddCommand(cmd);
	}
	static void butReadCardDataBaseActionPerformed(java.awt.event.ActionEvent evt) {
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 2000;
		int iType = cmbCardDataBaseType.getSelectedIndex() + 1;
		ReadCardDataBase_Parameter par = new ReadCardDataBase_Parameter(dt, iType);
		// 徐铭康修改
		// ReadCardDataBase cmd = new ReadCardDataBase(par);
		ReadCardDataBase cmd = new ReadCardDataBase(par);
		String[] CardTypeList = new String[]{"", "排序区", "非排序区", "所有区域"};
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			ReadCardDataBase_Result result = (ReadCardDataBase_Result) y;
			x.append("读取卡片数量：");
			x.append(result.DataBaseSize);
			x.append("；读取卡存储区域：");
			x.append(CardTypeList[result.CardType]);
			if (result.DataBaseSize > 0) {
				mCardList = result.CardList;
				String[] CardStatusList = new String[]{"正常", "挂失卡", "黑名单"};
				DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
				tableModel.setRowCount(0);// 清除原有行
				int Index = 1;
				StringBuilder builder = new StringBuilder(result.DataBaseSize * 140);
				for (CardDetail cd : mCardList) {
					Object[] arr = CardDetailToRow(cd, Index);
					builder.append("卡号：");
					builder.append(arr[1]);// cd.CardData
					builder.append("，密码：");
					builder.append(arr[2]);// cd.Password
					builder.append("，有效期：");
					builder.append(arr[3]);// cd.Expiry
					builder.append("，有效次数：");
					builder.append(arr[5]);// OpenTimes
					builder.append("，特权：");
					builder.append(arr[6]);// Privilege
					builder.append("，卡状态：");
					builder.append(arr[4]);// cd.CardStatus
					builder.append("权限和时段：");
					int arrIndex = 7;
					for (int i = 1; i <= 4; i++) {
						builder.append("门");
						builder.append(i);
						builder.append("：");
						builder.append(arr[arrIndex]);
						arrIndex++;
						builder.append("权限");
						builder.append(",开门时段:");
						builder.append(arr[arrIndex]);
						arrIndex++;
						builder.append("；");
					}
					builder.append("节假日限制:");
					builder.append(arr[arrIndex]);
					// 添加数据到表格
					tableModel.addRow(arr);
					Index += 1;
				}
				// 更新表格
				tblCard.invalidate();
				try {
					String file = WriteFile("CardDatabase", builder, false);
					if (file == null) {
						x.append("卡数据导出失败！");
					}
					else {
						x.append("卡数据导出成功，地址：");
						x.append(file);
					}
				}
				catch (Exception e) {
				}
			}
		});
		_Allocator.AddCommand(cmd);
	}
	static void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 1000;
		CommandParameter par = new CommandParameter(dt);
		WriteTime cmd = new WriteTime(par);
		_Allocator.AddCommand(cmd);
	}
	static void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 1000;
		CommandParameter par = new CommandParameter(dt);
		ReadTime cmd = new ReadTime(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			x.append("控制器时间：");
			ReadTime_Result result = (ReadTime_Result) y;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			x.append(sdf.format(result.ControllerDate.getTime()));
		});
		_Allocator.AddCommand(cmd);
	}
	/**
	 * 自动搜索控制器
	 * @param evt
	 */
	static void butAutoSearchDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butAutoSearchDoorActionPerformed
		// 自动搜索门禁控制器
		CommandDetail dt = new CommandDetail();
		UDPDetail udp = new UDPDetail("255.255.255.255", 8101);
		udp.LocalIP = "192.168.1.138";
		udp.LocalPort = 8886;
		udp.Broadcast = true;
		dt.Connector = udp;
		dt.Identity = new FC8800Identity("0000000000000000", FC8800Command.NULLPassword, E_ControllerType.FC8900);
		Random rnd = new Random();
		int max = 65535;
		int min = 10000;
		// 搜索时，不需要设定重发
		dt.RestartCount = 0;
		dt.Timeout = 5000;// 每隔5秒发送一次，所以这里设定5秒超时
		// 网络标记就是一个随机数
		SearchNetFlag = rnd.nextInt(max) % (max - min + 1) + min;// 网络标记
		SearchTimes = 1;// 搜索次数;
		SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
		SearchEquptOnNetNum cmd = new SearchEquptOnNetNum(par);
		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
			SearchEquptOnNetNum_Result result = (SearchEquptOnNetNum_Result) y;
			x.append("第");
			x.append(SearchTimes);
			x.append("次搜索，本次搜索到控制器：");
			x.append(result.SearchTotal);
			x.append("个。");
			if (result.SearchTotal > 0) {
				PrintSearchDoor(result.ResultList, x);
				// 将已搜索到的控制器设定网络标志，防止下次再搜索到
				SetDoorNetFlag(result.ResultList);
			}
			if (SearchTimes < 4) {
				SearchEquptOnNetNum_Parameter par1 = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
				SearchEquptOnNetNum cmd1 = new SearchEquptOnNetNum(par1);
				_Allocator.AddCommand(cmd1);
			}
			else {
				x.append("*************搜索结束！*******************");
				return;
			}
			SearchTimes++;
		});
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butAutoSearchDoorActionPerformed
	/**
	 * 远程常开
	 * @param evt
	 */
	static void butHoldDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butHoldDoorActionPerformed
		// 远程常开
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 1000;
		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
		par.Door.SetDoor(1, 1);// 设定1号门执行操作
		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
		HoldDoor cmd = new HoldDoor(par);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butHoldDoorActionPerformed
	static void butUnlockDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butUnlockDoorActionPerformed
		// 远程解除锁定
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
		par.Door.SetDoor(1, 1);// 设定1号门执行操作
		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
		UnlockDoor cmd = new UnlockDoor(par);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butUnlockDoorActionPerformed
	static void butLockDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butLockDoorActionPerformed
		// 远程锁定
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
		par.Door.SetDoor(1, 1);// 设定1号门执行操作
		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
		LockDoor cmd = new LockDoor(par);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butLockDoorActionPerformed
	/**
	 * 远程关门
	 * @param evt evt
	 */
	static void butCloseDoorActionPerformed(java.awt.event.ActionEvent evt) {
		// 远程关门
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
		par.Door.SetDoor(1, 1);// 设定1号门执行操作
		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
		CloseDoor cmd = new CloseDoor(par);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butCloseDoorActionPerformed
	/**
	 * 远程开门
	 * @param evt
	 */
	static void butOpenDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butOpenDoorActionPerformed
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		dt.Timeout = 1000;
		OpenDoor_Parameter par = new OpenDoor_Parameter(dt);
		par.Door.SetDoor(1, 1);// 设定1号门执行操作
		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
		OpenDoor cmd = new OpenDoor(par);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butOpenDoorActionPerformed
	/**
	 * 解除报警，可解除所有报警
	 * @param evt
	 */
	static void butCloseAlarmActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butCloseAlarmActionPerformed
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CloseAlarm_Parameter par = new CloseAlarm_Parameter(dt);
		par.Alarm.Alarm = 65535;// 解除所有报警
		CloseAlarm cmd = new CloseAlarm(par);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butCloseAlarmActionPerformed
	static void butCloseWatchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butCloseWatchActionPerformed
		// 关闭数据监控
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		CloseWatch cmd = new CloseWatch(par);
		_Allocator.CloseForciblyConnect(dt.Connector);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butCloseWatchActionPerformed
	static void butBeginWatchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butBeginWatchActionPerformed
		// 打开数据监控
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
		CommandParameter par = new CommandParameter(dt);
		BeginWatch cmd = new BeginWatch(par);
		_Allocator.OpenForciblyConnect(dt.Connector);
		_Allocator.AddCommand(cmd);
	}// GEN-LAST:event_butBeginWatchActionPerformed
	static void butReadSNActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadSNActionPerformed
		// TODO add your handling code here:
		CommandDetail dt = getCommandDetail();
		if (dt == null) {
			return;
		}
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
		WriteDeadline_Parameter par = new WriteDeadline_Parameter(dt, 1000);
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
		if (RadUDP.isSelected()) {
			dt.Timeout = 15000;// UDP延迟增加
		}
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
		if (RadUDP.isSelected()) {
			// UDP延迟增加
			dt.Timeout = 15000;
		}
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
	private static void PirntTCPDetail(TCPDetail tcp, StringBuilder x) {
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
	private static void tblCard_mouseClicked() {
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
	private static String WriteFile(String sFileName, StringBuilder strBuf, boolean append) {
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
	private static String GetBooleanStr(boolean v) {
		if (v) {
			return "有";
		}
		else {
			return "无";
		}
	}
	private static void ClearCardList() {
		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
		tableModel.setRowCount(0);// 清除原有行
	}
	private static void FillCardToList() {
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
	private static Object[] CardDetailToRow(CardDetail cd, int Index) {
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
	private static CardDetail GetCardDetail() {
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
	private static void PrintTransactionDatabaseDetail(String TransactionName, TransactionDetail dt, StringBuilder buf) {
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
	private static void PrintTransactionDatabase(ArrayList<AbstractTransaction> TransactionList, StringBuilder log, String[] sTransactionList) {
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
	//	public static  int SearchTimes = 0;
	//	public static  int SearchNetFlag;
	private static void PrintSearchDoor(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst, StringBuilder log) {
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
	private static void SetDoorNetFlag(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst) {
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
		if (RadTCPClient.isSelected()) {
			ip = txtTCPServerIP.getText();
			strPort = txtLocalPort.getText();
		}
		if (RadUDP.isSelected()) {
			ip = txtUDPRemoteIP.getText();
			strPort = txtUDPRemotePort.getText();
		}
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
		if (RadUDP.isSelected()) {
			UDPDetail udpd = new UDPDetail(ip, iPort);
			udpd.Broadcast = true;
			// udpd.LocalPort = 10088;//设定本地绑定端口号
			detail.Connector = udpd;
		}
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
	private static void AddCommandResultCallback(String sCommand, CommandResultCallback callback) {
		if (!CommandResult.containsKey(sCommand)) {
			CommandResult.put(sCommand, callback);
		}
		/*
		 * else { CommandResult.remove(sCommand); CommandResult.put(sCommand, callback);
		 * }
		 */
	}
	/**
	 * 初始化监控消息处理的相关操作
	 */
	//	public static void IniWatchEvent() {
	//		mWatchTypeNameList = new String[]{"", "读卡信息", "出门开关信息", "门磁信息", "远程开门信息", "报警信息", "系统信息", "连接保活消息", "连接确认信息"};
	//		mCardTransactionList = new String[256];
	//		mButtonTransactionList = new String[256];
	//		mDoorSensorTransactionList = new String[256];
	//		mSoftwareTransactionList = new String[256];
	//		mAlarmTransactionList = new String[256];
	//		mSystemTransactionList = new String[256];
	//		mCardTransactionList[1] = "合法开门";//
	//		mCardTransactionList[2] = "密码开门";// ------------卡号为密码
	//		mCardTransactionList[3] = "卡加密码";//
	//		mCardTransactionList[4] = "手动输入卡加密码";//
	//		mCardTransactionList[5] = "首卡开门";//
	//		mCardTransactionList[6] = "门常开";// --- 常开工作方式中，刷卡进入常开状态
	//		mCardTransactionList[7] = "多卡开门";// -- 多卡验证组合完毕后触发
	//		mCardTransactionList[8] = "重复读卡";//
	//		mCardTransactionList[9] = "有效期过期";//
	//		mCardTransactionList[10] = "开门时段过期";//
	//		mCardTransactionList[11] = "节假日无效";//
	//		mCardTransactionList[12] = "未注册卡";//
	//		mCardTransactionList[13] = "巡更卡";// -- 不开门
	//		mCardTransactionList[14] = "探测锁定";//
	//		mCardTransactionList[15] = "无有效次数";//
	//		mCardTransactionList[16] = "防潜回";//
	//		mCardTransactionList[17] = "密码错误";// ------------卡号为错误密码
	//		mCardTransactionList[18] = "密码加卡模式密码错误";// ----卡号为卡号。
	//		mCardTransactionList[19] = "锁定时(读卡)或(读卡加密码)开门";//
	//		mCardTransactionList[20] = "锁定时(密码开门)";//
	//		mCardTransactionList[21] = "首卡未开门";//
	//		mCardTransactionList[22] = "挂失卡";//
	//		mCardTransactionList[23] = "黑名单卡";//
	//		mCardTransactionList[24] = "门内上限已满，禁止入门。";//
	//		mCardTransactionList[25] = "开启防盗布防状态(设置卡)";//
	//		mCardTransactionList[26] = "撤销防盗布防状态(设置卡)";//
	//		mCardTransactionList[27] = "开启防盗布防状态(密码)";//
	//		mCardTransactionList[28] = "撤销防盗布防状态(密码)";//
	//		mCardTransactionList[29] = "互锁时(读卡)或(读卡加密码)开门";//
	//		mCardTransactionList[30] = "互锁时(密码开门)";//
	//		mCardTransactionList[31] = "全卡开门";//
	//		mCardTransactionList[32] = "多卡开门--等待下张卡";//
	//		mCardTransactionList[33] = "多卡开门--组合错误";//
	//		mCardTransactionList[34] = "非首卡时段刷卡无效";//
	//		mCardTransactionList[35] = "非首卡时段密码无效";//
	//		mCardTransactionList[36] = "禁止刷卡开门";// -- 【开门认证方式】验证模式中禁用了刷卡开门时
	//		mCardTransactionList[37] = "禁止密码开门";// -- 【开门认证方式】验证模式中禁用了密码开门时
	//		mCardTransactionList[38] = "门内已刷卡，等待门外刷卡。";// （门内外刷卡验证）
	//		mCardTransactionList[39] = "门外已刷卡，等待门内刷卡。";// （门内外刷卡验证）
	//		mCardTransactionList[40] = "请刷管理卡";// (在开启管理卡功能后提示)(电梯板)
	//		mCardTransactionList[41] = "请刷普通卡";// (在开启管理卡功能后提示)(电梯板)
	//		mCardTransactionList[42] = "首卡未读卡时禁止密码开门。";//
	//		mCardTransactionList[43] = "控制器已过期_刷卡";//
	//		mCardTransactionList[44] = "控制器已过期_密码";//
	//		mCardTransactionList[45] = "合法卡开门—有效期即将过期";//
	//		mCardTransactionList[46] = "拒绝开门--区域反潜回失去主机连接。";//
	//		mCardTransactionList[47] = "拒绝开门--区域互锁，失去主机连接";//
	//		mCardTransactionList[48] = "区域防潜回--拒绝开门";//
	//		mCardTransactionList[49] = "区域互锁--有门未关好，拒绝开门";//
	//		mButtonTransactionList[1] = "按钮开门";//
	//		mButtonTransactionList[2] = "开门时段过期";//
	//		mButtonTransactionList[3] = "锁定时按钮";//
	//		mButtonTransactionList[4] = "控制器已过期";//
	//		mButtonTransactionList[5] = "互锁时按钮(不开门)";//
	//		mDoorSensorTransactionList[1] = "开门";//
	//		mDoorSensorTransactionList[2] = "关门";//
	//		mDoorSensorTransactionList[3] = "进入门磁报警状态";//
	//		mDoorSensorTransactionList[4] = "退出门磁报警状态";//
	//		mDoorSensorTransactionList[5] = "门未关好";//
	//		mSoftwareTransactionList[1] = "软件开门";//
	//		mSoftwareTransactionList[2] = "软件关门";//
	//		mSoftwareTransactionList[3] = "软件常开";//
	//		mSoftwareTransactionList[4] = "控制器自动进入常开";//
	//		mSoftwareTransactionList[5] = "控制器自动关闭门";//
	//		mSoftwareTransactionList[6] = "长按出门按钮常开";//
	//		mSoftwareTransactionList[7] = "长按出门按钮常闭";//
	//		mSoftwareTransactionList[8] = "软件锁定";//
	//		mSoftwareTransactionList[9] = "软件解除锁定";//
	//		mSoftwareTransactionList[10] = "控制器定时锁定";// --到时间自动锁定
	//		mSoftwareTransactionList[11] = "控制器定时解除锁定";// --到时间自动解除锁定
	//		mSoftwareTransactionList[12] = "报警--锁定";//
	//		mSoftwareTransactionList[13] = "报警--解除锁定";//
	//		mSoftwareTransactionList[14] = "互锁时远程开门";//
	//		mAlarmTransactionList[1] = "门磁报警";//
	//		mAlarmTransactionList[2] = "匪警报警";//
	//		mAlarmTransactionList[3] = "消防报警";//
	//		mAlarmTransactionList[4] = "非法卡刷报警";//
	//		mAlarmTransactionList[5] = "胁迫报警";//
	//		mAlarmTransactionList[6] = "消防报警(命令通知)";//
	//		mAlarmTransactionList[7] = "烟雾报警";//
	//		mAlarmTransactionList[8] = "防盗报警";//
	//		mAlarmTransactionList[9] = "黑名单报警";//
	//		mAlarmTransactionList[10] = "开门超时报警";//
	//		mAlarmTransactionList[0x11] = "门磁报警撤销";//
	//		mAlarmTransactionList[0x12] = "匪警报警撤销";//
	//		mAlarmTransactionList[0x13] = "消防报警撤销";//
	//		mAlarmTransactionList[0x14] = "非法卡刷报警撤销";//
	//		mAlarmTransactionList[0x15] = "胁迫报警撤销";//
	//		mAlarmTransactionList[0x17] = "撤销烟雾报警";//
	//		mAlarmTransactionList[0x18] = "关闭防盗报警";//
	//		mAlarmTransactionList[0x19] = "关闭黑名单报警";//
	//		mAlarmTransactionList[0x1A] = "关闭开门超时报警";//
	//		mAlarmTransactionList[0x21] = "门磁报警撤销(命令通知)";//
	//		mAlarmTransactionList[0x22] = "匪警报警撤销(命令通知)";//
	//		mAlarmTransactionList[0x23] = "消防报警撤销(命令通知)";//
	//		mAlarmTransactionList[0x24] = "非法卡刷报警撤销(命令通知)";//
	//		mAlarmTransactionList[0x25] = "胁迫报警撤销(命令通知)";//
	//		mAlarmTransactionList[0x27] = "撤销烟雾报警(命令通知)";//
	//		mAlarmTransactionList[0x28] = "关闭防盗报警(软件关闭)";//
	//		mAlarmTransactionList[0x29] = "关闭黑名单报警(软件关闭)";//
	//		mAlarmTransactionList[0x2A] = "关闭开门超时报警";//
	//		mSystemTransactionList[1] = "系统加电";//
	//		mSystemTransactionList[2] = "系统错误复位（看门狗）";//
	//		mSystemTransactionList[3] = "设备格式化记录";//
	//		mSystemTransactionList[4] = "系统高温记录，温度大于>75";//
	//		mSystemTransactionList[5] = "系统UPS供电记录";//
	//		mSystemTransactionList[6] = "温度传感器损坏，温度大于>100";//
	//		mSystemTransactionList[7] = "电压过低，小于<09V";//
	//		mSystemTransactionList[8] = "电压过高，大于>14V";//
	//		mSystemTransactionList[9] = "读卡器接反。";//
	//		mSystemTransactionList[10] = "读卡器线路未接好。";//
	//		mSystemTransactionList[11] = "无法识别的读卡器";//
	//		mSystemTransactionList[12] = "电压恢复正常，小于14V，大于9V";//
	//		mSystemTransactionList[13] = "网线已断开";//
	//		mSystemTransactionList[14] = "网线已插入";//
	//	}
	/**
	 * 记录事件的描述到StringBuilder中
	 */
	private static void logTransaction(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf, String[] sTransactionList) {
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
