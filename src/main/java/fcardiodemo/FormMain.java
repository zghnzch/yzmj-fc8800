/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcardiodemo;
import Net.PC15.Command.INCommand;
import Net.PC15.Command.INCommandResult;
import Net.PC15.Connector.ConnectorAllocator;
import Net.PC15.Connector.ConnectorDetail;
import Net.PC15.Connector.INConnectorEvent;
import Net.PC15.Connector.TCPServer.TCPServerClientDetail;
import Net.PC15.Data.INData;
import Net.PC15.FC8800.Command.Data.CardDetail;
import Net.PC15.FC8800.Command.Data.FC8800WatchTransaction;
import Net.PC15.FC8800.Command.Data.TCPDetail;
import Net.PC15.FC8800.Command.System.SearchEquptOnNetNum;
import fcardutils.stringutil.StringUtil;
import fcardutils.threadutil.ThreadPoolUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

import static fcardiodemo.FormMainFunctionUtils.txtLogMouseClicked;
import static fcardiodemo.FormMainFunctionUtils2.*;
/**
 * @author 赖金杰
 */
public class FormMain extends javax.swing.JFrame implements INConnectorEvent, ActionListener {
	// region GUI组件定义
	private static javax.swing.JButton ButReadDeadline;
	private static javax.swing.JButton ButWriteDeadline;
	private static javax.swing.JLabel LblDate;
	static javax.swing.JTextField TxtCardExpiry;
	private static javax.swing.ButtonGroup bgConnectType;
	private static javax.swing.JButton butAddCardToList;
	private static javax.swing.JButton butAutoSearchDoor;
	private static javax.swing.JButton butBeginWatch;
	private static javax.swing.JButton butCardListAutoCreate;
	private static javax.swing.JButton butCardListClear;
	private static javax.swing.JButton butClearCardDataBase;
	private static javax.swing.JButton butClearTransactionDatabase;
	private static javax.swing.JButton butCloseAlarm;
	private static javax.swing.JButton butCloseDoor;
	private static javax.swing.JButton butCloseWatch;
	private static javax.swing.JButton butDeleteCard;
	private static javax.swing.JButton butDeleteCardByList;
	private static javax.swing.JButton butHoldDoor;
	private static javax.swing.JButton butLockDoor;
	private static javax.swing.JButton butOpenDoor;
	private static javax.swing.JButton butReadCardDataBase;
	private static javax.swing.JButton butReadCardDatabaseDetail;
	private static javax.swing.JButton butReadCardDetail;
	private static javax.swing.JButton butReadConnectPassword;
	private static javax.swing.JButton butReadSN;
	private static javax.swing.JButton butReadTCPSetting;
	private static javax.swing.JButton butReadTransactionDatabase;
	private static javax.swing.JButton butReadTransactionDatabaseByIndex;
	private static javax.swing.JButton butReadTransactionDatabaseDetail;
	private static javax.swing.JButton butReadVersion;
	private static javax.swing.JButton butResetConnectPassword;
	private static javax.swing.JButton butTransactionDatabaseEmpty;
	private static javax.swing.JButton butUnlockDoor;
	private static javax.swing.JButton butUploadCard;
	private static javax.swing.JButton butWriteCardListBySequence;
	private static javax.swing.JButton butWriteCardListBySort;
	private static javax.swing.JButton butWriteConnectPassword;
	private static javax.swing.JButton butWriteSN;
	private static javax.swing.JButton butWriteTCPSetting;
	private static javax.swing.JButton butWriteTransactionDatabaseReadIndex;
	private static javax.swing.JButton butWriteTransactionDatabaseWriteIndex;
	static javax.swing.JCheckBox chkCardDoor1;
	static javax.swing.JCheckBox chkCardDoor2;
	static javax.swing.JCheckBox chkCardDoor3;
	static javax.swing.JCheckBox chkCardDoor4;
	static javax.swing.JCheckBox chkTransactionIsCircle;
	static javax.swing.JComboBox<String> cmbCardDataBaseType;
	static javax.swing.JComboBox<String> cmbCardStatus;
	static javax.swing.JComboBox<String> cmbTransactionType;
	private static javax.swing.JButton jButton1;
	private static javax.swing.JButton jButton2;
	private static javax.swing.JLabel jLabel10;
	private static javax.swing.JLabel jLabel11;
	private static javax.swing.JLabel jLabel12;
	private static javax.swing.JLabel jLabel13;
	private static javax.swing.JLabel jLabel14;
	private static javax.swing.JLabel jLabel15;
	private static javax.swing.JLabel jLabel16;
	private static javax.swing.JLabel jLabel17;
	private static javax.swing.JLabel jLabel18;
	private static javax.swing.JLabel jLabel19;
	private static javax.swing.JLabel jLabel20;
	private static javax.swing.JLabel jLabel21;
	private static javax.swing.JLabel jLabel22;
	private static javax.swing.JLabel jLabel23;
	private static javax.swing.JLabel jLabel24;
	private static javax.swing.JLabel jLabel25;
	private static javax.swing.JLabel jLabel6;
	private static javax.swing.JLabel jLabel7;
	private static javax.swing.JLabel jLabel8;
	private static javax.swing.JLabel jLabel9;
	private static javax.swing.JPanel jPanel1;
	private static javax.swing.JPanel jPanel2;
	private static javax.swing.JPanel jPanel3;
	private static javax.swing.JPanel jPanel4;
	private static javax.swing.JPanel jPanel5;
	private static javax.swing.JPanel jPanel6;
	private static javax.swing.JPanel jPanel8;
	private static javax.swing.JPanel jPanel9;
	private static javax.swing.JScrollPane jScrollPane1;
	private static javax.swing.JScrollPane jScrollPane2;
	private static javax.swing.JTabbedPane jTabSetting;
	private static javax.swing.JTextArea jTextArea1;
	private static javax.swing.JPanel jpConnectSetting;
	private static javax.swing.JScrollPane jpLog;
	private static javax.swing.JLabel lblCommandName;
	private static javax.swing.JLabel lblTime;
	private static javax.swing.JPanel pnlTCPClient;
	private static javax.swing.JPanel pnlUDPClient;
	private static javax.swing.JPanel pnlTCPServer;
	private static javax.swing.JProgressBar prCommand;
	static javax.swing.JTable tblCard;
	static javax.swing.JTextField txtCardAutoCreateSzie;
	static javax.swing.JTextField txtCardData;
	static javax.swing.JTextField txtCardPassword;
	static javax.swing.JTextArea txtLog;
	static javax.swing.JTextField txtOpenTimes;
	static javax.swing.JTextField txtPassword;
	static javax.swing.JTextField txtReadTransactionDatabaseByIndex;
	static javax.swing.JTextField txtReadTransactionDatabaseByQuantity;
	static javax.swing.JTextField txtReadTransactionDatabasePacketSize;
	static javax.swing.JTextField txtReadTransactionDatabaseQuantity;
	static javax.swing.JTextField txtSN;
	static javax.swing.JTextField txtTCPClienpIP;
	static javax.swing.JTextField txtTCPClienpPort;
	static JTextField txtUdpIp;
	static JTextField txtUdpPort;
	private static JLabel label_2;
	private static JLabel label_3;
	static JTextField txtTcpServerPort;
	static JTextField txtTcpServerIp;
	static javax.swing.JTextField txtTransactionDatabaseReadIndex;
	static javax.swing.JTextField txtTransactionDatabaseWriteIndex;
	static javax.swing.JTextField txtWriteSN;
	private JTextField textDeviceVersion;
	private JLabel label_4;
	// endregion
	// region 变量定义
	final static Logger myLog = Logger.getLogger(FormMain.class);
	static ConnectorAllocator _Allocator;
	// public static ConcurrentHashMap<String, String> CommandName
	static ConcurrentHashMap<String, CommandResultCallback> CommandResult;
	// public Timer timer = new Timer()
	private static boolean mIsClose;
	static StringBuilder strLog;
	static TCPDetail mReadTCPDetail;
	static String[] CardStatusList = new String[]{"正常", "挂失卡", "黑名单"};
	static String[] PrivilegeList = new String[]{"无", "首卡特权卡", "常开特权卡", "巡更卡", "防盗设置卡"};
	static String OpenTimesUnlimited = "无限制";
	static ArrayList<CardDetail> mCardList;
	static int SearchTimes = 0;
	static int SearchNetFlag;
	static String[] mWatchTypeNameList;
	static String[] mCardTransactionList, mButtonTransactionList, mDoorSensorTransactionList, mSoftwareTransactionList, mAlarmTransactionList, mSystemTransactionList;
	// endregion
	/**
	 * 主窗口
	 */
	FormMain() {
		initComponents();
		setTitle("FC8800 控制器调试器 V1.0");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		mIsClose = false;
		JFrame.setDefaultLookAndFeelDecorated(true);
		// 移动位置
		setSize(1040, 800);
		Dimension screenSize = getToolkit().getScreenSize();
		Rectangle oldRectangle = getBounds();
		setBounds((screenSize.width - oldRectangle.width) / 2, (screenSize.height - oldRectangle.height) / 2, oldRectangle.width, oldRectangle.height);
		// 初始化timer
		// timer = new Timer();
		// timer.schedule(new TimeTask(), 200, 1000);
		showConnectPanel();
		_Allocator = ConnectorAllocator.GetAllocator();
		_Allocator.AddListener(this);
		strLog = new StringBuilder(50000);
		FormMain frm = this;
		// 一定要监听退出事件
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frm.setVisible(false);
				if (_Allocator != null) {
					_Allocator.DeleteListener(frm);
					// 这里一定要释放，否则无法退出程序
					_Allocator.Release();
					_Allocator = null;
				}
				mIsClose = true;
				// timer.cancel();
				// timer = null;
				myLog.info("关闭窗口,程序即将退出！");
				System.exit(0);
			}
		});
		CommandResult = new ConcurrentHashMap<>();
		// CommandName = new ConcurrentHashMap<>();
		FormUtils.iniCommandName();
		FormMainFunctionUtils2.IniCardDataBase();
		FormUtils.iniWatchEvent();
	}
	/**
	 * 初始化界面内容
	 */
	private void initComponents() {
		bgConnectType = new javax.swing.ButtonGroup();
		jpConnectSetting = new javax.swing.JPanel();
		jpConnectSetting.setBounds(2, 10, 713, 143);
		jpConnectSetting.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "通讯参数", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
		jpConnectSetting.setLayout(null);
		jPanel9 = new javax.swing.JPanel();
		jPanel9.setBounds(10, 21, 699, 117);
		/* ============= */
		pnlTCPClient = new javax.swing.JPanel();
		pnlTCPClient.setBounds(10, 10, 200, 95);
		pnlUDPClient = new javax.swing.JPanel();
		pnlUDPClient.setBounds(224, 10, 200, 95);
		pnlTCPServer = new javax.swing.JPanel();
		pnlTCPServer.setBounds(438, 10, 200, 95);
		/* ============= */
		jLabel24 = new javax.swing.JLabel();
		jLabel24.setBounds(16, 20, 75, 20);
		txtTCPClienpIP = new javax.swing.JTextField();
		txtTCPClienpIP.setBounds(101, 20, 90, 20);
		jLabel25 = new javax.swing.JLabel();
		jLabel25.setBounds(16, 50, 75, 20);
		txtTCPClienpPort = new javax.swing.JTextField();
		txtTCPClienpPort.setBounds(101, 50, 90, 20);
		jPanel3 = new javax.swing.JPanel();
		jPanel3.setBounds(725, 13, 224, 140);
		LblDate = new javax.swing.JLabel();
		LblDate.setBounds(10, 10, 225, 25);
		LblDate.setFont(new java.awt.Font("宋体", Font.PLAIN, 14));
		LblDate.setForeground(new java.awt.Color(0, 0, 255));
		LblDate.setHorizontalAlignment(SwingConstants.LEFT);
		LblDate.setText("年月日：");
		jPanel3.add(LblDate);
		// NOI18N
		lblTime = new javax.swing.JLabel();
		lblTime.setBounds(92, 8, 75, 25);
		lblTime.setFont(new java.awt.Font("宋体", Font.PLAIN, 14));
		lblTime.setForeground(new java.awt.Color(0, 0, 255));
		lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblTime.setText("时分秒");
		// jPanel3.add(lblTime);
		prCommand = new javax.swing.JProgressBar();
		prCommand.setBounds(10, 116, 204, 14);
		jPanel3.add(prCommand);
		lblCommandName = new javax.swing.JLabel();
		lblCommandName.setFont(new Font("宋体", Font.PLAIN, 14));
		lblCommandName.setBounds(10, 45, 194, 61);
		lblCommandName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lblCommandName.setText("当前命令：");
		lblCommandName.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		jPanel3.add(lblCommandName);
		jpLog = new javax.swing.JScrollPane();
		jpLog.setBounds(0, 163, 949, 689);
		txtLog = new javax.swing.JTextArea();
		jTabSetting = new javax.swing.JTabbedPane();
		jTabSetting.setBounds(959, 10, 515, 842);
		jPanel6 = new javax.swing.JPanel();
		jPanel6.setBorder(null);
		butWriteConnectPassword = new javax.swing.JButton();
		butWriteConnectPassword.setBounds(130, 299, 110, 23);
		butResetConnectPassword = new javax.swing.JButton();
		butResetConnectPassword.setBounds(10, 299, 109, 23);
		butReadTCPSetting = new javax.swing.JButton();
		butReadTCPSetting.setBounds(10, 266, 109, 23);
		butWriteTCPSetting = new javax.swing.JButton();
		butWriteTCPSetting.setBounds(130, 266, 110, 23);
		ButReadDeadline = new javax.swing.JButton();
		ButReadDeadline.setBounds(10, 398, 230, 23);
		ButWriteDeadline = new javax.swing.JButton();
		ButWriteDeadline.setBounds(10, 431, 230, 23);
		jPanel1 = new javax.swing.JPanel();
		jPanel1.setBounds(10, 10, 490, 216);
		jLabel8 = new javax.swing.JLabel();
		jLabel8.setBounds(20, 34, 75, 25);
		txtWriteSN = new javax.swing.JTextField();
		txtWriteSN.setBounds(113, 34, 110, 25);
		butWriteSN = new javax.swing.JButton();
		butWriteSN.setBounds(233, 66, 142, 25);
		butReadSN = new javax.swing.JButton();
		butReadSN.setBounds(233, 34, 142, 25);
		butBeginWatch = new javax.swing.JButton();
		butBeginWatch.setBounds(10, 332, 109, 23);
		butCloseWatch = new javax.swing.JButton();
		butCloseWatch.setBounds(130, 332, 110, 23);
		butCloseAlarm = new javax.swing.JButton();
		butCloseAlarm.setBounds(10, 365, 230, 23);
		butOpenDoor = new javax.swing.JButton();
		butOpenDoor.setBounds(10, 497, 109, 23);
		butCloseDoor = new javax.swing.JButton();
		butCloseDoor.setBounds(130, 497, 110, 23);
		butLockDoor = new javax.swing.JButton();
		butLockDoor.setBounds(10, 565, 109, 23);
		butUnlockDoor = new javax.swing.JButton();
		butUnlockDoor.setBounds(130, 565, 110, 23);
		butHoldDoor = new javax.swing.JButton();
		butHoldDoor.setBounds(10, 530, 230, 23);
		butAutoSearchDoor = new javax.swing.JButton();
		butAutoSearchDoor.setBounds(10, 464, 230, 23);
		jScrollPane2 = new javax.swing.JScrollPane();
		jScrollPane2.setBounds(20, 620, 480, 168);
		jPanel8 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		butReadCardDataBase = new javax.swing.JButton();
		butReadCardDatabaseDetail = new javax.swing.JButton();
		cmbCardDataBaseType = new javax.swing.JComboBox<>();
		butClearCardDataBase = new javax.swing.JButton();
		jLabel9 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		tblCard = new javax.swing.JTable();
		butReadCardDetail = new javax.swing.JButton();
		jLabel10 = new javax.swing.JLabel();
		txtCardData = new javax.swing.JTextField();
		jLabel11 = new javax.swing.JLabel();
		txtCardPassword = new javax.swing.JTextField();
		jLabel12 = new javax.swing.JLabel();
		TxtCardExpiry = new javax.swing.JTextField();
		jLabel13 = new javax.swing.JLabel();
		txtOpenTimes = new javax.swing.JTextField();
		jLabel14 = new javax.swing.JLabel();
		cmbCardStatus = new javax.swing.JComboBox<>();
		chkCardDoor4 = new javax.swing.JCheckBox();
		chkCardDoor1 = new javax.swing.JCheckBox();
		chkCardDoor2 = new javax.swing.JCheckBox();
		chkCardDoor3 = new javax.swing.JCheckBox();
		jLabel15 = new javax.swing.JLabel();
		butAddCardToList = new javax.swing.JButton();
		butCardListClear = new javax.swing.JButton();
		butUploadCard = new javax.swing.JButton();
		butDeleteCard = new javax.swing.JButton();
		butCardListAutoCreate = new javax.swing.JButton();
		butWriteCardListBySequence = new javax.swing.JButton();
		butWriteCardListBySort = new javax.swing.JButton();
		butDeleteCardByList = new javax.swing.JButton();
		jLabel16 = new javax.swing.JLabel();
		txtCardAutoCreateSzie = new javax.swing.JTextField();
		jPanel4 = new javax.swing.JPanel();
		butReadTransactionDatabaseDetail = new javax.swing.JButton();
		butTransactionDatabaseEmpty = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		butClearTransactionDatabase = new javax.swing.JButton();
		jLabel17 = new javax.swing.JLabel();
		cmbTransactionType = new javax.swing.JComboBox<>();
		butReadTransactionDatabaseByIndex = new javax.swing.JButton();
		butWriteTransactionDatabaseReadIndex = new javax.swing.JButton();
		butWriteTransactionDatabaseWriteIndex = new javax.swing.JButton();
		jLabel18 = new javax.swing.JLabel();
		txtTransactionDatabaseReadIndex = new javax.swing.JTextField();
		jLabel19 = new javax.swing.JLabel();
		txtTransactionDatabaseWriteIndex = new javax.swing.JTextField();
		jLabel20 = new javax.swing.JLabel();
		txtReadTransactionDatabaseByIndex = new javax.swing.JTextField();
		txtReadTransactionDatabaseByQuantity = new javax.swing.JTextField();
		jLabel21 = new javax.swing.JLabel();
		chkTransactionIsCircle = new javax.swing.JCheckBox();
		butReadTransactionDatabase = new javax.swing.JButton();
		jLabel22 = new javax.swing.JLabel();
		txtReadTransactionDatabasePacketSize = new javax.swing.JTextField();
		jLabel23 = new javax.swing.JLabel();
		txtReadTransactionDatabaseQuantity = new javax.swing.JTextField();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1500, 900));
		setSize(new Dimension(1500, 850));
		pnlTCPClient.setBorder(javax.swing.BorderFactory.createTitledBorder("TCP客户端"));
		pnlTCPClient.setLayout(null);
		pnlUDPClient.setBorder(javax.swing.BorderFactory.createTitledBorder("UDP端"));
		pnlUDPClient.setLayout(null);
		pnlTCPServer.setBorder(javax.swing.BorderFactory.createTitledBorder("TCP服务器"));
		pnlTCPServer.setLayout(null);
		jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel24.setText("服务器地址：");
		pnlTCPClient.add(jLabel24);
		txtTCPClienpIP.setText("192.168.1.150");
		pnlTCPClient.add(txtTCPClienpIP);
		jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel25.setText("服务器端口：");
		pnlTCPClient.add(jLabel25);
		txtTCPClienpPort.setText("8000");
		pnlTCPClient.add(txtTCPClienpPort);
		getContentPane().setLayout(null);
		jpConnectSetting.add(jPanel9);
		jPanel9.setLayout(null);
		jPanel9.add(pnlTCPClient);
		jPanel9.add(pnlUDPClient);
		JLabel label = new JLabel();
		label.setText("服务器地址：");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(14, 21, 75, 20);
		pnlUDPClient.add(label);
		txtUdpIp = new JTextField();
		txtUdpIp.setText("255.255.255.255");
		txtUdpIp.setBounds(95, 21, 90, 20);
		pnlUDPClient.add(txtUdpIp);
		JLabel lbeUdp = new JLabel();
		lbeUdp.setText("服务器端口：");
		lbeUdp.setHorizontalAlignment(SwingConstants.RIGHT);
		lbeUdp.setBounds(10, 51, 79, 20);
		pnlUDPClient.add(lbeUdp);
		txtUdpPort = new JTextField();
		txtUdpPort.setText("8101");
		txtUdpPort.setBounds(95, 51, 90, 20);
		pnlUDPClient.add(txtUdpPort);
		jPanel9.add(pnlTCPServer);
		label_2 = new JLabel();
		label_2.setText("服务器地址：");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(14, 20, 75, 20);
		pnlTCPServer.add(label_2);
		label_3 = new JLabel();
		label_3.setText("服务器端口：");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(10, 50, 79, 20);
		pnlTCPServer.add(label_3);
		txtTcpServerPort = new JTextField();
		txtTcpServerPort.setText("10006");
		txtTcpServerPort.setBounds(95, 50, 90, 20);
		pnlTCPServer.add(txtTcpServerPort);
		txtTcpServerIp = new JTextField();
		txtTcpServerIp.setText("192.168.1.2");
		txtTcpServerIp.setBounds(95, 20, 90, 20);
		pnlTCPServer.add(txtTcpServerIp);
		getContentPane().add(jpConnectSetting);
		jPanel3.setLayout(null);
		getContentPane().add(jPanel3);
		txtLog.setColumns(20);
		txtLog.setRows(5);
		txtLog.setLineWrap(true);
		txtLog.setWrapStyleWord(true);
		txtLog.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				txtLogMouseClicked(evt);
			}
		});
		jpLog.setViewportView(txtLog);
		getContentPane().add(jpLog);
		// NOI18N
		jTabSetting.setName("");
		butWriteConnectPassword.setText("写通讯密码");
		butWriteConnectPassword.setEnabled(false);
		butWriteConnectPassword.addActionListener(FormMainFunctionUtils2::butWriteConnectPasswordActionPerformed);
		jPanel6.setLayout(null);
		jPanel6.add(butWriteConnectPassword);
		butResetConnectPassword.setText("复位通讯密码");
		butResetConnectPassword.addActionListener(FormMainFunctionUtils2::butResetConnectPasswordActionPerformed);
		jPanel6.add(butResetConnectPassword);
		butReadTCPSetting.setText("读TCP参数");
		butReadTCPSetting.addActionListener(FormMainFunctionUtils2::butReadTCPSettingActionPerformed);
		jPanel6.add(butReadTCPSetting);
		butWriteTCPSetting.setText("写TCP参数");
		butWriteTCPSetting.setEnabled(false);
		butWriteTCPSetting.addActionListener(FormMainFunctionUtils2::butWriteTCPSettingActionPerformed);
		jPanel6.add(butWriteTCPSetting);
		ButReadDeadline.setText("读取控制器有效天数");
		ButReadDeadline.addActionListener(FormMainFunctionUtils2::ButReadDeadlineActionPerformed);
		jPanel6.add(ButReadDeadline);
		ButWriteDeadline.setText("修改控制器有效天数");
		ButWriteDeadline.addActionListener(FormMainFunctionUtils2::ButWriteDeadlineActionPerformed);
		jPanel6.add(ButWriteDeadline);
		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("SN"));
		jPanel1.setLayout(null);
		jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel8.setText("SN：");
		jPanel1.add(jLabel8);
		txtWriteSN.setText("MC-5912T19060002");
		jPanel1.add(txtWriteSN);
		butWriteSN.setText("写");
		butWriteSN.setEnabled(false);
		butWriteSN.addActionListener(FormMainFunctionUtils2::butWriteSNActionPerformed);
		jPanel1.add(butWriteSN);
		butReadSN.setText("读SN");
		// 读取SN
		butReadSN.addActionListener(FormMainFunctionUtils2::butReadSNActionPerformed);
		jPanel1.add(butReadSN);
		jPanel6.add(jPanel1);
		jLabel7 = new javax.swing.JLabel();
		jLabel7.setBounds(20, 101, 75, 25);
		jPanel1.add(jLabel7);
		jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel7.setText("通讯密码：");
		txtPassword = new javax.swing.JTextField();
		txtPassword.setBounds(113, 101, 110, 25);
		jPanel1.add(txtPassword);
		txtPassword.setText("123456FF");
		butReadConnectPassword = new javax.swing.JButton();
		butReadConnectPassword.setBounds(233, 101, 142, 25);
		jPanel1.add(butReadConnectPassword);
		butReadConnectPassword.setText("读通讯密码");
		jLabel6 = new javax.swing.JLabel();
		jLabel6.setBounds(20, 69, 75, 25);
		jPanel1.add(jLabel6);
		jLabel6.setHorizontalAlignment(SwingConstants.RIGHT);
		jLabel6.setText("SN：");
		txtSN = new javax.swing.JTextField();
		txtSN.setBounds(113, 69, 110, 25);
		jPanel1.add(txtSN);
		txtSN.setText("MC-5912T19060002");
		butReadVersion = new javax.swing.JButton();
		butReadVersion.setBounds(233, 136, 142, 23);
		jPanel1.add(butReadVersion);
		butReadVersion.setText("读取版本号");
		textDeviceVersion = new JTextField();
		textDeviceVersion.setText("07.18");
		textDeviceVersion.setBounds(113, 136, 110, 25);
		jPanel1.add(textDeviceVersion);
		label_4 = new JLabel();
		label_4.setText("版本号：");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setBounds(20, 136, 75, 25);
		jPanel1.add(label_4);
		butReadVersion.addActionListener(FormMainFunctionUtils2::butReadVersionActionPerformed);
		butReadConnectPassword.addActionListener(FormMainFunctionUtils2::butReadConnectPasswordActionPerformed);
		butBeginWatch.setText("打开数据监控");
		butBeginWatch.addActionListener(FormMainFunctionUtils::butBeginWatchActionPerformed);
		jPanel6.add(butBeginWatch);
		butCloseWatch.setText("关闭数据监控");
		butCloseWatch.addActionListener(FormMainFunctionUtils::butCloseWatchActionPerformed);
		jPanel6.add(butCloseWatch);
		butCloseAlarm.setText("解除所有报警");
		butCloseAlarm.addActionListener(FormMainFunctionUtils::butCloseAlarmActionPerformed);
		jPanel6.add(butCloseAlarm);
		butOpenDoor.setText("远程开门");
		butOpenDoor.addActionListener(FormMainFunctionUtils::butOpenDoorActionPerformed);
		jPanel6.add(butOpenDoor);
		butCloseDoor.setText("远程关门");
		butCloseDoor.addActionListener(FormMainFunctionUtils::butCloseDoorActionPerformed);
		jPanel6.add(butCloseDoor);
		butLockDoor.setText("远程锁定");
		butLockDoor.addActionListener(FormMainFunctionUtils::butLockDoorActionPerformed);
		jPanel6.add(butLockDoor);
		butUnlockDoor.setText("远程解锁");
		butUnlockDoor.addActionListener(FormMainFunctionUtils::butUnlockDoorActionPerformed);
		jPanel6.add(butUnlockDoor);
		butHoldDoor.setText("远程常开");
		butHoldDoor.addActionListener(FormMainFunctionUtils::butHoldDoorActionPerformed);
		jPanel6.add(butHoldDoor);
		butAutoSearchDoor.setText("自动搜索控制器");
		butAutoSearchDoor.addActionListener(FormMainFunctionUtils::butAutoSearchDoorActionPerformed);
		jPanel6.add(butAutoSearchDoor);
		jPanel6.add(jScrollPane2);
		jTextArea1 = new javax.swing.JTextArea();
		jTextArea1.setFont(new Font("Monospaced", Font.PLAIN, 14));
		jTextArea1.setWrapStyleWord(true);
		jTextArea1.setTabSize(0);
		jTextArea1.setLineWrap(true);
		jScrollPane2.setViewportView(jTextArea1);
		jTextArea1.setEditable(false);
		jTextArea1.setColumns(10);
		jTextArea1.setRows(2);
		jTextArea1.setText("UDP通讯只支持如下指令【搜索不同网络标识设备】、【设置设备网络标识】【读TCP参数】、【写TCP参数】");
		jTabSetting.addTab("系统设置", jPanel6);
		jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jButton1.setText("读时间");
		jButton1.addActionListener(FormMainFunctionUtils::jButton1ActionPerformed);
		jPanel8.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, -1, -1));
		jButton2.setText("写时间");
		jButton2.setActionCommand("");
		jButton2.addActionListener(FormMainFunctionUtils::jButton2ActionPerformed);
		jPanel8.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));
		jTabSetting.addTab("时间参数", jPanel8);
		jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		butReadCardDataBase.setText("从控制器中读取卡片数据");
		butReadCardDataBase.addActionListener(FormMainFunctionUtils::butReadCardDataBaseActionPerformed);
		jPanel2.add(butReadCardDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, -1, -1));
		butReadCardDatabaseDetail.setText("读卡数据库信息");
		butReadCardDatabaseDetail.addActionListener(FormMainFunctionUtils::butReadCardDatabaseDetailActionPerformed);
		jPanel2.add(butReadCardDatabaseDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, -1));
		cmbCardDataBaseType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
		jPanel2.add(cmbCardDataBaseType, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 130, -1));
		butClearCardDataBase.setBackground(new java.awt.Color(255, 51, 51));
		butClearCardDataBase.setForeground(new java.awt.Color(255, 0, 51));
		butClearCardDataBase.setText("清空卡片数据库");
		butClearCardDataBase.addActionListener(FormMainFunctionUtils::butClearCardDataBaseActionPerformed);
		jPanel2.add(butClearCardDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));
		jLabel9.setText("卡区域：");
		jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 52, -1, -1));
		tblCard.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{}));
		jScrollPane1.setViewportView(tblCard);
		jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 480, 400));
		butReadCardDetail.setText("读取单个卡片在控制器中的信息");
		butReadCardDetail.addActionListener(FormMainFunctionUtils::butReadCardDetailActionPerformed);
		jPanel2.add(butReadCardDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 490, 230, -1));
		jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel10.setText("卡号：");
		jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 543, 36, -1));
		txtCardData.setText("123456");
		jPanel2.add(txtCardData, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 540, 70, -1));
		jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel11.setText("密码：");
		jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 540, 36, -1));
		txtCardPassword.setText("8888");
		jPanel2.add(txtCardPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 540, 70, -1));
		jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel12.setText("截止日期：");
		jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 543, 70, -1));
		jLabel12.getAccessibleContext().setAccessibleName("有效期：");
		TxtCardExpiry.setText("2099-12-30 12:30");
		jPanel2.add(TxtCardExpiry, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 540, 110, -1));
		jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel13.setText("开门次数：");
		jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 70, -1));
		txtOpenTimes.setText("65535");
		jPanel2.add(txtOpenTimes, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 570, 70, -1));
		jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel14.setText("状态：");
		jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 570, 50, -1));
		cmbCardStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"正常", "挂失卡", "黑名单"}));
		jPanel2.add(cmbCardStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 570, 70, -1));
		chkCardDoor4.setSelected(true);
		chkCardDoor4.setText("门4");
		jPanel2.add(chkCardDoor4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 600, -1, -1));
		chkCardDoor1.setSelected(true);
		chkCardDoor1.setText("门1");
		jPanel2.add(chkCardDoor1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 600, -1, -1));
		chkCardDoor2.setSelected(true);
		chkCardDoor2.setText("门2");
		jPanel2.add(chkCardDoor2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 600, -1, -1));
		chkCardDoor3.setSelected(true);
		chkCardDoor3.setText("门3");
		jPanel2.add(chkCardDoor3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 600, -1, -1));
		jLabel15.setText("门权限：");
		jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, -1, -1));
		butAddCardToList.setText("添加到列表");
		butAddCardToList.addActionListener(FormMainFunctionUtils::butAddCardToListActionPerformed);
		jPanel2.add(butAddCardToList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 160, -1));
		butCardListClear.setText("清空列表");
		butCardListClear.addActionListener(FormMainFunctionUtils::butCardListClearActionPerformed);
		jPanel2.add(butCardListClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 490, 110, -1));
		butUploadCard.setText("将卡号上传至非排序区");
		butUploadCard.addActionListener(FormMainFunctionUtils::butUploadCardActionPerformed);
		jPanel2.add(butUploadCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 630, -1, -1));
		butDeleteCard.setText("将卡号从控制器删除");
		butDeleteCard.addActionListener(FormMainFunctionUtils::butDeleteCardActionPerformed);
		jPanel2.add(butDeleteCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 630, -1, -1));
		butCardListAutoCreate.setText("自动生成卡号");
		butCardListAutoCreate.addActionListener(FormMainFunctionUtils::butCardListAutoCreateActionPerformed);
		jPanel2.add(butCardListAutoCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 160, -1));
		butWriteCardListBySequence.setText("将列表卡号上传至非排序区");
		butWriteCardListBySequence.addActionListener(FormMainFunctionUtils::butWriteCardListBySequenceActionPerformed);
		jPanel2.add(butWriteCardListBySequence, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 690, -1, -1));
		butWriteCardListBySort.setText("将列表卡号上传至排序区");
		butWriteCardListBySort.addActionListener(FormMainFunctionUtils::butWriteCardListBySortActionPerformed);
		jPanel2.add(butWriteCardListBySort, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 690, -1, -1));
		butDeleteCardByList.setText("删除列表中的卡片");
		butDeleteCardByList.addActionListener(FormMainFunctionUtils::butDeleteCardByListActionPerformed);
		jPanel2.add(butDeleteCardByList, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 690, -1, -1));
		jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel16.setText("自动生成数量：");
		jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 663, 90, -1));
		txtCardAutoCreateSzie.setText("2000");
		jPanel2.add(txtCardAutoCreateSzie, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 660, 70, -1));
		jTabSetting.addTab("卡片管理", jPanel2);
		jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		butReadTransactionDatabaseDetail.setText("读取记录数据库详情");
		butReadTransactionDatabaseDetail.addActionListener(FormMainFunctionUtils::butReadTransactionDatabaseDetailActionPerformed);
		jPanel4.add(butReadTransactionDatabaseDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));
		butTransactionDatabaseEmpty.setBackground(new java.awt.Color(255, 0, 0));
		butTransactionDatabaseEmpty.setForeground(new java.awt.Color(255, 0, 0));
		butTransactionDatabaseEmpty.setText("清空所有记录");
		butTransactionDatabaseEmpty.addActionListener(FormMainFunctionUtils::butTransactionDatabaseEmptyActionPerformed);
		jPanel4.add(butTransactionDatabaseEmpty, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));
		jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("指定记录类型的操作"));
		jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		butClearTransactionDatabase.setBackground(new java.awt.Color(255, 0, 0));
		butClearTransactionDatabase.setForeground(new java.awt.Color(255, 0, 0));
		butClearTransactionDatabase.setText("清空记录");
		butClearTransactionDatabase.addActionListener(FormMainFunctionUtils::butClearTransactionDatabaseActionPerformed);
		jPanel5.add(butClearTransactionDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));
		jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel17.setText("记录类型：");
		jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, -1));
		cmbTransactionType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"读卡记录", "出门开关记录", "门磁记录", "软件操作记录", "报警记录", "系统记录"}));
		jPanel5.add(cmbTransactionType, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 110, -1));
		butReadTransactionDatabaseByIndex.setText("按序号读记录");
		butReadTransactionDatabaseByIndex.addActionListener(FormMainFunctionUtils::butReadTransactionDatabaseByIndexActionPerformed);
		jPanel5.add(butReadTransactionDatabaseByIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 120, -1));
		butWriteTransactionDatabaseReadIndex.setText("修改记录读索引");
		butWriteTransactionDatabaseReadIndex.addActionListener(FormMainFunctionUtils::butWriteTransactionDatabaseReadIndexActionPerformed);
		jPanel5.add(butWriteTransactionDatabaseReadIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));
		butWriteTransactionDatabaseWriteIndex.setText("修改记录写索引");
		butWriteTransactionDatabaseWriteIndex.addActionListener(FormMainFunctionUtils::butWriteTransactionDatabaseWriteIndexActionPerformed);
		jPanel5.add(butWriteTransactionDatabaseWriteIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));
		jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel18.setText("读索引号：");
		jPanel5.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 73, 79, -1));
		txtTransactionDatabaseReadIndex.setText("0");
		jPanel5.add(txtTransactionDatabaseReadIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 70, 60, -1));
		jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel19.setText("写索引号：");
		jPanel5.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 103, 79, -1));
		txtTransactionDatabaseWriteIndex.setText("0");
		jPanel5.add(txtTransactionDatabaseWriteIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 100, 60, -1));
		jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel20.setText("开始索引号：");
		jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 133, 79, -1));
		txtReadTransactionDatabaseByIndex.setText("1");
		jPanel5.add(txtReadTransactionDatabaseByIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 60, -1));
		txtReadTransactionDatabaseByQuantity.setText("10");
		jPanel5.add(txtReadTransactionDatabaseByQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 130, 60, -1));
		jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel21.setText("读数量：");
		jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 133, 60, -1));
		chkTransactionIsCircle.setText("循环");
		jPanel5.add(chkTransactionIsCircle, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 70, -1, -1));
		butReadTransactionDatabase.setText("读新记录");
		butReadTransactionDatabase.addActionListener(FormMainFunctionUtils::butReadTransactionDatabaseActionPerformed);
		jPanel5.add(butReadTransactionDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 120, -1));
		jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel22.setText("单次读取数量：");
		jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 170, 90, -1));
		txtReadTransactionDatabasePacketSize.setText("200");
		jPanel5.add(txtReadTransactionDatabasePacketSize, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 60, -1));
		jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel23.setText("读数量：");
		jPanel5.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 170, 60, -1));
		txtReadTransactionDatabaseQuantity.setText("0");
		jPanel5.add(txtReadTransactionDatabaseQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 170, 60, -1));
		jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 470, 290));
		jTabSetting.addTab("记录管理", jPanel4);
		getContentPane().add(jTabSetting);
		pack();
		setTimeThread();
	}
	@Override
	public void ClientOnline(TCPServerClientDetail client) {
		// To change body of generated methods, choose Tools | Templates.
		throw new UnsupportedOperationException("Not supported yet.");
	}
	@Override
	public void ClientOffline(TCPServerClientDetail client) {
		// To change body of generated methods, choose Tools | Templates.
		throw new UnsupportedOperationException("Not supported yet.");
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	}
	@Override
	public void CommandCompleteEvent(INCommand cmd, INCommandResult result) {
		try {
			StringBuilder strBuf = new StringBuilder(100);
			GetCommandDetail(strBuf, cmd);
			String sKey = cmd.getClass().getName();
			if (CommandResult.containsKey(sKey)) {
				strBuf.append("命令处理完毕，返回结果数据：");
				CommandResult.get(sKey).resultToLog(strBuf, result);
			}
			else {
				strBuf.append("命令处理完毕!");
			}
			// FormUtils.AddLog(strBuf.toString(),strLog,txtLog)
			FormUtils.addTxtLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			// myLog.error("fcardiodemo.FormMain.CommandCompleteEvent() -- 发生错误：" + e.toString())
			Logger.getRootLogger().error("fcardiodemo.FormMain.CommandCompleteEvent() -- 发生错误：" + e);
		}
	}
	@Override
	public void CommandProcessEvent(INCommand cmd) {
		try {
			// lblCommandName.setText("当前命令：" + GetCommandName(cmd) + "正在处理" )
			StringBuilder strBuf = new StringBuilder(100);
			strBuf.append("<html>");
			strBuf.append("当前命令：");
			strBuf.append(GetCommandName(cmd));
			strBuf.append("<br/>正在处理： ");
			strBuf.append(cmd.getProcessStep());
			strBuf.append(" / ");
			strBuf.append(cmd.getProcessMax());
			strBuf.append("</html>");
			lblCommandName.setText(strBuf.toString());
			if (prCommand.getMaximum() != cmd.getProcessMax()) {
				// prCommand.setValue(0)
				prCommand.setMaximum(cmd.getProcessMax());
			}
			prCommand.setValue(cmd.getProcessStep());
			// AddLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			myLog.error("fcardiodemo.FormMain.CommandProcessEvent() -- 发生错误：" + e.toString());
		}
	}
	@Override
	public void ConnectorErrorEvent(INCommand cmd, boolean isStop) {
		try {
			StringBuilder strBuf = new StringBuilder(100);
			GetCommandDetail(strBuf, cmd);
			if (isStop) {
				strBuf.append("命令已手动停止!");
			}
			else {
				strBuf.append("网络连接失败!");
			}
			FormUtils.addTxtLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			myLog.error("fcardiodemo.FormMain.ConnectorErrorEvent() --- " + e.toString());
		}
	}
	@Override
	public void ConnectorErrorEvent(ConnectorDetail detail) {
		try {
			StringBuilder strBuf = new StringBuilder(100);
			strBuf.append("网络通道故障，IP信息：");
			GetConnectorDetail(strBuf, detail);
			FormUtils.addTxtLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			myLog.error("fcardiodemo.FormMain.ConnectorErrorEvent() -- " + e.toString());
		}
	}
	@Override
	public void CommandTimeout(INCommand cmd) {
		try {
			if (cmd instanceof SearchEquptOnNetNum) {
				CommandCompleteEvent(cmd, cmd.getCommandResult());
				return;
			}
			StringBuilder strBuf = new StringBuilder(100);
			GetCommandDetail(strBuf, cmd);
			strBuf.append("命令超时，已失败！");
			FormUtils.addTxtLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			myLog.error("fcardiodemo.FormMain.CommandTimeout() -- " + e.toString());
		}
	}
	@Override
	public void PasswordErrorEvent(INCommand cmd) {
		try {
			StringBuilder strBuf = new StringBuilder(100);
			GetCommandDetail(strBuf, cmd);
			strBuf.append("通讯密码错误，已失败！");
			FormUtils.addTxtLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			myLog.error("fcardiodemo.FormMain.PasswordErrorEvent() -- " + e.toString());
		}
	}
	@Override
	public void ChecksumErrorEvent(INCommand cmd) {
		try {
			StringBuilder strBuf = new StringBuilder(100);
			GetCommandDetail(strBuf, cmd);
			strBuf.append("命令返回的校验和错误，已失败！");
			FormUtils.addTxtLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			myLog.error("fcardiodemo.FormMain.ChecksumErrorEvent() -- " + e.toString());
		}
	}
	@Override
	public void WatchEvent(ConnectorDetail detail, INData event) {
		try {
			StringBuilder strBuf = new StringBuilder(100);
			strBuf.append("数据监控:");
			GetConnectorDetail(strBuf, detail);
			if (event instanceof FC8800WatchTransaction) {
				FC8800WatchTransaction WatchTransaction = (FC8800WatchTransaction) event;
				strBuf.append("，SN：");
				strBuf.append(WatchTransaction.SN);
				PrintWatchEvent(WatchTransaction, strBuf);
			}
			else {
				strBuf.append("，未知事件：");
				strBuf.append(event.getClass().getName());
			}
			FormUtils.addTxtLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			myLog.error("fcardiodemo.FormMain.WatchEvent() -- " + e.toString());
		}
	}

	private void showConnectPanel() {
		pnlTCPClient.setVisible(true);
		pnlUDPClient.setVisible(true);
		pnlTCPServer.setVisible(true);
	}
	private void setTimeThread() {
		ExecutorService executorService = ThreadPoolUtils.newDaemonMultipleThreadExecutor("set-time-to-label");
		if (executorService != null) {
			executorService.execute(() -> {
				while (true) {
					try {
						LblDate.setText(StringUtil.getNowTimeFortest());
						Thread.sleep(1000);
					}
					catch (Exception e) {
						myLog.error(e);
						break;
					}
				}
			});
		}
	}
}
