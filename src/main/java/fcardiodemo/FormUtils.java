package fcardiodemo;
import Net.PC15.FC8800.Command.Card.*;
import Net.PC15.FC8800.Command.DateTime.ReadTime;
import Net.PC15.FC8800.Command.DateTime.WriteTime;
import Net.PC15.FC8800.Command.DateTime.WriteTimeBroadcast;
import Net.PC15.FC8800.Command.DateTime.WriteTimeDefine;
import Net.PC15.FC8800.Command.Door.*;
import Net.PC15.FC8800.Command.System.*;
import Net.PC15.FC8800.Command.Transaction.*;
import fcardutils.stringutil.StringUtil;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
/***
 * @class FormUtils
 * @description main窗口工具类
 * @author zch
 * @date 2019/9/10
 * @version V0.0.1.201909102004.01
 * @modfiyDate 201909102004
 * @createDate 201909102004
 * @package fcardutils
 */
class FormUtils {
	private final static Object LOG_LOCK = new Object();
	private final static String NEXT_LINE = "\n";
	static ConcurrentHashMap<String, String> CommandName = new ConcurrentHashMap<>();
	// 在txt窗口中添加日志
	static void addTxtLog(String log) {
		synchronized (LOG_LOCK) {
			Logger.getRootLogger().info(log);
			log = StringUtil.getNowTimeFortest() + ":" + log + NEXT_LINE + FormMain.txtLog.getText();
			FormMain.txtLog.setText(log);
		}
	}
	// 初始化组件
	static void iniCommandName() {
		if (CommandName.size() > 0) {
			return;
		}
		CommandName.put(WriteSN.class.getName(), "写控制器SN");
		CommandName.put(WriteSN_Broadcast.class.getName(), "通过广播写SN");
		CommandName.put(ReadSN.class.getName(), "从控制器中读取SN");
		CommandName.put(WriteConnectPassword.class.getName(), "修改控制器通讯密码");
		CommandName.put(ReadConnectPassword.class.getName(), "从控制器中获取通讯密码");
		CommandName.put(ResetConnectPassword.class.getName(), "重置控制器通讯密码");
		CommandName.put(ReadTCPSetting.class.getName(), "读取控制器TCP网络参数");
		CommandName.put(WriteTCPSetting.class.getName(), "修改控制器TCP网络参数");
		CommandName.put(ReadDeadline.class.getName(), "读取控制器有效期剩余天数");
		CommandName.put(WriteDeadline.class.getName(), "写入控制器有效期");
		CommandName.put(ReadVersion.class.getName(), "获取控制器版本号");
		CommandName.put(ReadSystemStatus.class.getName(), "读取设备运行信息");
		CommandName.put(ReadAllSystemSetting.class.getName(), "获取所有系统参数");
		CommandName.put(ReadRecordMode.class.getName(), "读取记录存储方式");
		CommandName.put(WriteRecordMode.class.getName(), "写入记录存储方式");
		CommandName.put(ReadKeyboard.class.getName(), "读取读卡器密码键盘启用功能开关");
		CommandName.put(WriteKeyboard.class.getName(), "写入读卡器密码键盘启用功能开关");
		CommandName.put(ReadLockInteraction.class.getName(), "读取互锁功能开关");
		CommandName.put(WriteLockInteraction.class.getName(), "写入互锁功能开关");
		CommandName.put(ReadFireAlarmOption.class.getName(), "读取消防报警功能参数");
		CommandName.put(WriteFireAlarmOption.class.getName(), "写入消防报警功能参数");
		CommandName.put(ReadOpenAlarmOption.class.getName(), "读取匪警报警功能参数");
		CommandName.put(WriteOpenAlarmOption.class.getName(), "写入匪警报警功能参数");
		CommandName.put(ReadReaderIntervalTime.class.getName(), "读取读卡间隔时间参数");
		CommandName.put(WriteReaderIntervalTime.class.getName(), "写入读卡间隔时间参数");
		CommandName.put(ReadBroadcast.class.getName(), "读取语音段开关参数");
		CommandName.put(WriteBroadcast.class.getName(), "写入语音段开关参数");
		CommandName.put(ReadReaderCheckMode.class.getName(), "读取读卡器校验参数");
		CommandName.put(WriteReaderCheckMode.class.getName(), "写入读卡器校验参数");
		CommandName.put(ReadBuzzer.class.getName(), "读取主板蜂鸣器参数");
		CommandName.put(WriteBuzzer.class.getName(), "写入主板蜂鸣器参数");
		CommandName.put(ReadSmogAlarmOption.class.getName(), "读取烟雾报警功能参数");
		CommandName.put(WriteSmogAlarmOption.class.getName(), "写入烟雾报警功能参数");
		CommandName.put(ReadEnterDoorLimit.class.getName(), "读取门内人数上限参数");
		CommandName.put(WriteEnterDoorLimit.class.getName(), "写入门内人数上限参数");
		CommandName.put(ReadTheftAlarmSetting.class.getName(), "读取智能防盗主机参数");
		CommandName.put(WriteTheftAlarmSetting.class.getName(), "写入智能防盗主机参数");
		CommandName.put(ReadCheckInOut.class.getName(), "读取防潜回参数");
		CommandName.put(WriteCheckInOut.class.getName(), "写入防潜回参数");
		CommandName.put(ReadCardPeriodSpeak.class.getName(), "读取卡片到期提示参数");
		CommandName.put(WriteCardPeriodSpeak.class.getName(), "写入卡片到期提示参数");
		CommandName.put(ReadReadCardSpeak.class.getName(), "读取定时读卡播报语音参数");
		CommandName.put(WriteReadCardSpeak.class.getName(), "写入定时读卡播报语音参数");
		CommandName.put(BeginWatch.class.getName(), "开始数据监控");
		CommandName.put(BeginWatch_Broadcast.class.getName(), "广播开启监控");
		CommandName.put(CloseWatch.class.getName(), "关闭数据监控");
		CommandName.put(CloseWatch_Broadcast.class.getName(), "广播关闭监控");
		CommandName.put(SendFireAlarm.class.getName(), "通知设备触发消防报警");
		CommandName.put(CloseFireAlarm.class.getName(), "解除消防报警");
		CommandName.put(ReadFireAlarmState.class.getName(), "读取消防报警状态");
		CommandName.put(SendSmogAlarm.class.getName(), "通知设备触发烟雾报警");
		CommandName.put(CloseSmogAlarm.class.getName(), "解除烟雾报警");
		CommandName.put(ReadSmogAlarmState.class.getName(), "获取烟雾报警状态");
		CommandName.put(CloseAlarm.class.getName(), "解除报警");
		CommandName.put(ReadWorkStatus.class.getName(), "获取控制器各端口工作状态信息");
		CommandName.put(ReadTheftAlarmState.class.getName(), "获取防盗主机布防状态");
		CommandName.put(FormatController.class.getName(), "初始化控制器");
		CommandName.put(SearchEquptOnNetNum.class.getName(), "搜索控制器");
		CommandName.put(WriteEquptNetNum.class.getName(), "根据SN设置网络标记");
		CommandName.put(WriteKeepAliveInterval.class.getName(), "写入保活间隔时间");
		CommandName.put(ReadKeepAliveInterval.class.getName(), "读取保活间隔时间");
		CommandName.put(SetTheftDisarming.class.getName(), "防盗报警撤防");
		CommandName.put(SetTheftFortify.class.getName(), "防盗报警布防");
		CommandName.put(WriteBalcklistAlarmOption.class.getName(), "写入黑名单报警功能开关");
		CommandName.put(ReadBalcklistAlarmOption.class.getName(), "读取黑名单报警功能开关");
		CommandName.put(ReadExploreLockMode.class.getName(), "读取防探测功能开关");
		CommandName.put(WriteExploreLockMode.class.getName(), "写入防探测功能开关");
		CommandName.put(ReadCheck485Line.class.getName(), "读取485线路反接检测开关");
		CommandName.put(WriteCheck485Line.class.getName(), "写入485线路反接检测开关");
		CommandName.put(ReadCardDeadlineTipDay.class.getName(), "读取有效期即将过期提醒时间");
		CommandName.put(WriteCardDeadlineTipDay.class.getName(), "写入有效期即将过期提醒时间");
		CommandName.put(ReadTime.class.getName(), "从控制器中读取控制器时间");
		CommandName.put(WriteTime.class.getName(), "将电脑的最新时间写入到控制器中");
		CommandName.put(WriteTimeBroadcast.class.getName(), "校时广播");
		CommandName.put(WriteTimeDefine.class.getName(), "写入自定义时间到控制器");
		CommandName.put(WriteReaderOption.class.getName(), "写入控制器4个门的读卡器字节数");
		CommandName.put(ReadReaderOption.class.getName(), "读取控制器4个门的读卡器字节数");
		CommandName.put(ReadRelayOption.class.getName(), "读取控制器继电器参数");
		CommandName.put(WriteRelayOption.class.getName(), "写入控制器继电器参数");
		CommandName.put(OpenDoor.class.getName(), "远程开门指令");
		CommandName.put(CloseDoor.class.getName(), "远程关门指令");
		CommandName.put(HoldDoor.class.getName(), "远程保持门常开");
		CommandName.put(LockDoor.class.getName(), "远程锁定门");
		CommandName.put(UnlockDoor.class.getName(), "远程解除门锁定状态");
		CommandName.put(ReadReaderWorkSetting.class.getName(), "读取门的读卡器验证方式参数");
		CommandName.put(WriteReaderWorkSetting.class.getName(), "设置门的读卡器验证方式参数");
		CommandName.put(ReadDoorWorkSetting.class.getName(), "读取门的工作模式");
		CommandName.put(WriteDoorWorkSetting.class.getName(), "写入门的工作模式");
		CommandName.put(ReadAutoLockedSetting.class.getName(), "读取定时锁定门");
		CommandName.put(WriteAutoLockedSetting.class.getName(), "写入定时锁定门");
		CommandName.put(ReadRelayReleaseTime.class.getName(), "读取开门保持时间");
		CommandName.put(WriteRelayReleaseTime.class.getName(), "写入开门保持时间");
		CommandName.put(ReadReaderInterval.class.getName(), "读取重复读卡间隔");
		CommandName.put(WriteReaderInterval.class.getName(), "写入重复读卡间隔");
		CommandName.put(ReadInvalidCardAlarmOption.class.getName(), "读取未注册卡读卡时报警功能");
		CommandName.put(WriteInvalidCardAlarmOption.class.getName(), "写入未注册卡读卡时报警功能");
		CommandName.put(ReadAlarmPassword.class.getName(), "读取胁迫报警功能");
		CommandName.put(WriteAlarmPassword.class.getName(), "写入胁迫报警功能");
		CommandName.put(ReadAntiPassback.class.getName(), "读取防潜返");
		CommandName.put(WriteAntiPassback.class.getName(), "写入防潜返");
		CommandName.put(WriteOvertimeAlarmSetting.class.getName(), "写入开门超时报警功能");
		CommandName.put(ReadOvertimeAlarmSetting.class.getName(), "读取开门超时报警功能");
		CommandName.put(WritePushButtonSetting.class.getName(), "写入出门按钮功能");
		CommandName.put(ReadPushButtonSetting.class.getName(), "读取出门按钮功能");
		CommandName.put(ReadSensorAlarmSetting.class.getName(), "读取门磁报警功能");
		CommandName.put(WriteSensorAlarmSetting.class.getName(), "写入门磁报警功能");
		CommandName.put(ReadAnyCardSetting.class.getName(), "读取全卡开门功能");
		CommandName.put(WriteAnyCardSetting.class.getName(), "写入全卡开门功能");
		CommandName.put(ReadReadingBroadcast.class.getName(), "");
		CommandName.put(WriteReadingBroadcast.class.getName(), "");
		CommandName.put(ReadCardDatabaseDetail.class.getName(), "读取控制器中的卡片数据库信息");
		CommandName.put(ClearCardDataBase.class.getName(), "清空卡片数据库");
		CommandName.put(ReadCardDataBase.class.getName(), "从控制器中读取卡片数据");
		CommandName.put(ReadCardDetail.class.getName(), "读取单个卡片在控制器中的信息");
		CommandName.put(WriteCardListBySequence.class.getName(), "将卡片列表写入到控制器非排序区");
		CommandName.put(Net.PC15.FC89H.Command.Card.WriteCardListBySequence.class.getName(), "将卡片列表写入到控制器非排序区");
		CommandName.put(WriteCardListBySort.class.getName(), "将卡片列表写入到控制器排序区");
		CommandName.put(Net.PC15.FC89H.Command.Card.WriteCardListBySort.class.getName(), "将卡片列表写入到控制器排序区");
		CommandName.put(DeleteCard.class.getName(), "删除卡片");
		CommandName.put(Net.PC15.FC89H.Command.Card.DeleteCard.class.getName(), "删除卡片");
		CommandName.put(ReadTransactionDatabaseDetail.class.getName(), "读取控制器中的卡片数据库信息");
		CommandName.put(TransactionDatabaseEmpty.class.getName(), "清空所有类型的记录数据库");
		CommandName.put(ClearTransactionDatabase.class.getName(), "清空指定类型的记录数据库");
		CommandName.put(WriteTransactionDatabaseReadIndex.class.getName(), "修改指定记录数据库的读索引");
		CommandName.put(WriteTransactionDatabaseWriteIndex.class.getName(), "修改指定记录数据库的写索引");
		CommandName.put(ReadTransactionDatabaseByIndex.class.getName(), "读取记录");
		CommandName.put(ReadTransactionDatabase.class.getName(), "读取新记录");
		CommandName.put(Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabaseByIndex.class.getName(), "读取记录");
		CommandName.put(Net.PC15.FC89H.Command.Transaction.ReadTransactionDatabase.class.getName(), "读取新记录");
	}
	/**
	 * 初始化监控消息处理的相关操作
	 */
	static void iniWatchEvent() {
		FormMain.mWatchTypeNameList = new String[]{"", "读卡信息", "出门开关信息", "门磁信息", "远程开门信息", "报警信息", "系统信息", "连接保活消息", "连接确认信息"};
		FormMain.mCardTransactionList = new String[256];
		FormMain.mButtonTransactionList = new String[256];
		FormMain.mDoorSensorTransactionList = new String[256];
		FormMain.mSoftwareTransactionList = new String[256];
		FormMain.mAlarmTransactionList = new String[256];
		FormMain.mSystemTransactionList = new String[256];
		FormMain.mCardTransactionList[1] = "合法开门";
		// ------------卡号为密码
		FormMain.mCardTransactionList[2] = "密码开门";
		FormMain.mCardTransactionList[3] = "卡加密码";
		FormMain.mCardTransactionList[4] = "手动输入卡加密码";
		FormMain.mCardTransactionList[5] = "首卡开门";
		// --- 常开工作方式中，刷卡进入常开状态
		FormMain.mCardTransactionList[6] = "门常开";
		// -- 多卡验证组合完毕后触发
		FormMain.mCardTransactionList[7] = "多卡开门";
		FormMain.mCardTransactionList[8] = "重复读卡";
		FormMain.mCardTransactionList[9] = "有效期过期";
		FormMain.mCardTransactionList[10] = "开门时段过期";
		FormMain.mCardTransactionList[11] = "节假日无效";
		FormMain.mCardTransactionList[12] = "未注册卡";
		// -- 不开门
		FormMain.mCardTransactionList[13] = "巡更卡";
		FormMain.mCardTransactionList[14] = "探测锁定";
		FormMain.mCardTransactionList[15] = "无有效次数";
		FormMain.mCardTransactionList[16] = "防潜回";
		// ------------卡号为错误密码
		FormMain.mCardTransactionList[17] = "密码错误";
		// ----卡号为卡号。
		FormMain.mCardTransactionList[18] = "密码加卡模式密码错误";
		FormMain.mCardTransactionList[19] = "锁定时(读卡)或(读卡加密码)开门";
		FormMain.mCardTransactionList[20] = "锁定时(密码开门)";
		FormMain.mCardTransactionList[21] = "首卡未开门";
		FormMain.mCardTransactionList[22] = "挂失卡";
		FormMain.mCardTransactionList[23] = "黑名单卡";
		FormMain.mCardTransactionList[24] = "门内上限已满，禁止入门。";
		FormMain.mCardTransactionList[25] = "开启防盗布防状态(设置卡)";
		FormMain.mCardTransactionList[26] = "撤销防盗布防状态(设置卡)";
		FormMain.mCardTransactionList[27] = "开启防盗布防状态(密码)";
		FormMain.mCardTransactionList[28] = "撤销防盗布防状态(密码)";
		FormMain.mCardTransactionList[29] = "互锁时(读卡)或(读卡加密码)开门";
		FormMain.mCardTransactionList[30] = "互锁时(密码开门)";
		FormMain.mCardTransactionList[31] = "全卡开门";
		FormMain.mCardTransactionList[32] = "多卡开门--等待下张卡";
		FormMain.mCardTransactionList[33] = "多卡开门--组合错误";
		FormMain.mCardTransactionList[34] = "非首卡时段刷卡无效";
		FormMain.mCardTransactionList[35] = "非首卡时段密码无效";
		// -- 【开门认证方式】验证模式中禁用了刷卡开门时
		FormMain.mCardTransactionList[36] = "禁止刷卡开门";
		// -- 【开门认证方式】验证模式中禁用了密码开门时
		FormMain.mCardTransactionList[37] = "禁止密码开门";
		// （门内外刷卡验证）
		FormMain.mCardTransactionList[38] = "门内已刷卡，等待门外刷卡。";
		// （门内外刷卡验证）
		FormMain.mCardTransactionList[39] = "门外已刷卡，等待门内刷卡。";
		// (在开启管理卡功能后提示)(电梯板)
		FormMain.mCardTransactionList[40] = "请刷管理卡";
		// (在开启管理卡功能后提示)(电梯板)
		FormMain.mCardTransactionList[41] = "请刷普通卡";
		FormMain.mCardTransactionList[42] = "首卡未读卡时禁止密码开门。";
		FormMain.mCardTransactionList[43] = "控制器已过期_刷卡";
		FormMain.mCardTransactionList[44] = "控制器已过期_密码";
		FormMain.mCardTransactionList[45] = "合法卡开门—有效期即将过期";
		FormMain.mCardTransactionList[46] = "拒绝开门--区域反潜回失去主机连接。";
		FormMain.mCardTransactionList[47] = "拒绝开门--区域互锁，失去主机连接";
		FormMain.mCardTransactionList[48] = "区域防潜回--拒绝开门";
		FormMain.mCardTransactionList[49] = "区域互锁--有门未关好，拒绝开门";
		FormMain.mButtonTransactionList[1] = "按钮开门";
		FormMain.mButtonTransactionList[2] = "开门时段过期";
		FormMain.mButtonTransactionList[3] = "锁定时按钮";
		FormMain.mButtonTransactionList[4] = "控制器已过期";
		FormMain.mButtonTransactionList[5] = "互锁时按钮(不开门)";
		FormMain.mDoorSensorTransactionList[1] = "开门";
		FormMain.mDoorSensorTransactionList[2] = "关门";
		FormMain.mDoorSensorTransactionList[3] = "进入门磁报警状态";
		FormMain.mDoorSensorTransactionList[4] = "退出门磁报警状态";
		FormMain.mDoorSensorTransactionList[5] = "门未关好";
		FormMain.mSoftwareTransactionList[1] = "软件开门";
		FormMain.mSoftwareTransactionList[2] = "软件关门";
		FormMain.mSoftwareTransactionList[3] = "软件常开";
		FormMain.mSoftwareTransactionList[4] = "控制器自动进入常开";
		FormMain.mSoftwareTransactionList[5] = "控制器自动关闭门";
		FormMain.mSoftwareTransactionList[6] = "长按出门按钮常开";
		FormMain.mSoftwareTransactionList[7] = "长按出门按钮常闭";
		FormMain.mSoftwareTransactionList[8] = "软件锁定";
		FormMain.mSoftwareTransactionList[9] = "软件解除锁定";
		// --到时间自动锁定
		FormMain.mSoftwareTransactionList[10] = "控制器定时锁定";
		// --到时间自动解除锁定
		FormMain.mSoftwareTransactionList[11] = "控制器定时解除锁定";
		FormMain.mSoftwareTransactionList[12] = "报警--锁定";
		FormMain.mSoftwareTransactionList[13] = "报警--解除锁定";
		FormMain.mSoftwareTransactionList[14] = "互锁时远程开门";
		FormMain.mAlarmTransactionList[1] = "门磁报警";
		FormMain.mAlarmTransactionList[2] = "匪警报警";
		FormMain.mAlarmTransactionList[3] = "消防报警";
		FormMain.mAlarmTransactionList[4] = "非法卡刷报警";
		FormMain.mAlarmTransactionList[5] = "胁迫报警";
		FormMain.mAlarmTransactionList[6] = "消防报警(命令通知)";
		FormMain.mAlarmTransactionList[7] = "烟雾报警";
		FormMain.mAlarmTransactionList[8] = "防盗报警";
		FormMain.mAlarmTransactionList[9] = "黑名单报警";
		FormMain.mAlarmTransactionList[10] = "开门超时报警";
		FormMain.mAlarmTransactionList[0x11] = "门磁报警撤销";
		FormMain.mAlarmTransactionList[0x12] = "匪警报警撤销";
		FormMain.mAlarmTransactionList[0x13] = "消防报警撤销";
		FormMain.mAlarmTransactionList[0x14] = "非法卡刷报警撤销";
		FormMain.mAlarmTransactionList[0x15] = "胁迫报警撤销";
		FormMain.mAlarmTransactionList[0x17] = "撤销烟雾报警";
		FormMain.mAlarmTransactionList[0x18] = "关闭防盗报警";
		FormMain.mAlarmTransactionList[0x19] = "关闭黑名单报警";
		FormMain.mAlarmTransactionList[0x1A] = "关闭开门超时报警";
		FormMain.mAlarmTransactionList[0x21] = "门磁报警撤销(命令通知)";
		FormMain.mAlarmTransactionList[0x22] = "匪警报警撤销(命令通知)";
		FormMain.mAlarmTransactionList[0x23] = "消防报警撤销(命令通知)";
		FormMain.mAlarmTransactionList[0x24] = "非法卡刷报警撤销(命令通知)";
		FormMain.mAlarmTransactionList[0x25] = "胁迫报警撤销(命令通知)";
		FormMain.mAlarmTransactionList[0x27] = "撤销烟雾报警(命令通知)";
		FormMain.mAlarmTransactionList[0x28] = "关闭防盗报警(软件关闭)";
		FormMain.mAlarmTransactionList[0x29] = "关闭黑名单报警(软件关闭)";
		FormMain.mAlarmTransactionList[0x2A] = "关闭开门超时报警";
		FormMain.mSystemTransactionList[1] = "系统加电";
		FormMain.mSystemTransactionList[2] = "系统错误复位（看门狗）";
		FormMain.mSystemTransactionList[3] = "设备格式化记录";
		FormMain.mSystemTransactionList[4] = "系统高温记录，温度大于>75";
		FormMain.mSystemTransactionList[5] = "系统UPS供电记录";
		FormMain.mSystemTransactionList[6] = "温度传感器损坏，温度大于>100";
		FormMain.mSystemTransactionList[7] = "电压过低，小于<09V";
		FormMain.mSystemTransactionList[8] = "电压过高，大于>14V";
		FormMain.mSystemTransactionList[9] = "读卡器接反。";
		FormMain.mSystemTransactionList[10] = "读卡器线路未接好。";
		FormMain.mSystemTransactionList[11] = "无法识别的读卡器";
		FormMain.mSystemTransactionList[12] = "电压恢复正常，小于14V，大于9V";
		FormMain.mSystemTransactionList[13] = "网线已断开";
		FormMain.mSystemTransactionList[14] = "网线已插入";
	}
}
