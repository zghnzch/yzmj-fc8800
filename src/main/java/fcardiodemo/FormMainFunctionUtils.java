package fcardiodemo;
import Net.PC15.Command.CommandDetail;
import Net.PC15.Command.CommandParameter;
import Net.PC15.Connector.E_ControllerType;
import Net.PC15.Connector.UDP.UDPDetail;
import Net.PC15.FC8800.Command.Card.*;
import Net.PC15.FC8800.Command.Card.Parameter.*;
import Net.PC15.FC8800.Command.Card.Result.*;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.DateTime.ReadTime;
import Net.PC15.FC8800.Command.DateTime.Result.ReadTime_Result;
import Net.PC15.FC8800.Command.DateTime.WriteTime;
import Net.PC15.FC8800.Command.Door.*;
import Net.PC15.FC8800.Command.Door.Parameter.OpenDoor_Parameter;
import Net.PC15.FC8800.Command.Door.Parameter.RemoteDoor_Parameter;
import Net.PC15.FC8800.Command.FC8800Command;
import Net.PC15.FC8800.Command.System.BeginWatch;
import Net.PC15.FC8800.Command.System.CloseAlarm;
import Net.PC15.FC8800.Command.System.CloseWatch;
import Net.PC15.FC8800.Command.System.Parameter.CloseAlarm_Parameter;
import Net.PC15.FC8800.Command.System.Parameter.SearchEquptOnNetNum_Parameter;
import Net.PC15.FC8800.Command.System.Result.SearchEquptOnNetNum_Result;
import Net.PC15.FC8800.Command.System.SearchEquptOnNetNum;
import Net.PC15.FC8800.Command.Transaction.*;
import Net.PC15.FC8800.Command.Transaction.Parameter.*;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseByIndex_Result;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabaseDetail_Result;
import Net.PC15.FC8800.Command.Transaction.Result.ReadTransactionDatabase_Result;
import Net.PC15.FC8800.FC8800Identity;
import Net.PC15.Util.StringUtil;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static fcardiodemo.FormMain.*;
import static fcardiodemo.FormMainFunctionUtils2.*;
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
	/**
	 * 读取控制器中的卡片数据库信息
	 * @param evt evt
	 */
	static void butReadCardDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {
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
	}
}
