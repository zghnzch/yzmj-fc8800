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
import Net.PC15.FC8800.Command.Data.*;
import Net.PC15.FC8800.Command.System.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static fcardiodemo.FormMainFunctionUtils.*;
/**
 * @author 赖金杰
 */
public class FormMain extends javax.swing.JFrame implements INConnectorEvent, ActionListener {
	//region GUI组件定义
	static javax.swing.JButton ButReadDeadline;
	static javax.swing.JButton ButWriteDeadline;
	static javax.swing.JLabel LblDate;
	static javax.swing.JPanel PnlUDP;
	static javax.swing.JRadioButton RadTCPClient;
	static javax.swing.JRadioButton RadTCPServer;
	static javax.swing.JRadioButton RadUDP;
	static javax.swing.JTextField TxtCardExpiry;
	static javax.swing.ButtonGroup bgConnectType;
	static javax.swing.JButton butAddCardToList;
	static javax.swing.JButton butAutoSearchDoor;
	static javax.swing.JButton butBeginServer;
	static javax.swing.JButton butBeginWatch;
	static javax.swing.JButton butCardListAutoCreate;
	static javax.swing.JButton butCardListClear;
	static javax.swing.JButton butClearCardDataBase;
	static javax.swing.JButton butClearTransactionDatabase;
	static javax.swing.JButton butCloseAlarm;
	static javax.swing.JButton butCloseDoor;
	static javax.swing.JButton butCloseWatch;
	static javax.swing.JButton butDeleteCard;
	static javax.swing.JButton butDeleteCardByList;
	static javax.swing.JButton butHoldDoor;
	static javax.swing.JButton butLockDoor;
	static javax.swing.JButton butOpenDoor;
	static javax.swing.JButton butReadCardDataBase;
	static javax.swing.JButton butReadCardDatabaseDetail;
	static javax.swing.JButton butReadCardDetail;
	static javax.swing.JButton butReadConnectPassword;
	static javax.swing.JButton butReadSN;
	static javax.swing.JButton butReadTCPSetting;
	static javax.swing.JButton butReadTransactionDatabase;
	static javax.swing.JButton butReadTransactionDatabaseByIndex;
	static javax.swing.JButton butReadTransactionDatabaseDetail;
	static javax.swing.JButton butReadVersion;
	static javax.swing.JButton butResetConnectPassword;
	static javax.swing.JButton butTransactionDatabaseEmpty;
	static javax.swing.JButton butUnlockDoor;
	static javax.swing.JButton butUploadCard;
	static javax.swing.JButton butWriteCardListBySequence;
	static javax.swing.JButton butWriteCardListBySort;
	static javax.swing.JButton butWriteConnectPassword;
	static javax.swing.JButton butWriteSN;
	static javax.swing.JButton butWriteTCPSetting;
	static javax.swing.JButton butWriteTransactionDatabaseReadIndex;
	static javax.swing.JButton butWriteTransactionDatabaseWriteIndex;
	static javax.swing.JCheckBox chkCardDoor1;
	static javax.swing.JCheckBox chkCardDoor2;
	static javax.swing.JCheckBox chkCardDoor3;
	static javax.swing.JCheckBox chkCardDoor4;
	static javax.swing.JCheckBox chkTransactionIsCircle;
	static javax.swing.JComboBox<String> cmbCardDataBaseType;
	static javax.swing.JComboBox<String> cmbCardStatus;
	static javax.swing.JComboBox<String> cmbTransactionType;
	static javax.swing.JButton jButton1;
	static javax.swing.JButton jButton2;
	static javax.swing.JComboBox<String> jComboBox1;
	static javax.swing.JLabel jLabel1;
	static javax.swing.JLabel jLabel10;
	static javax.swing.JLabel jLabel11;
	static javax.swing.JLabel jLabel12;
	static javax.swing.JLabel jLabel13;
	static javax.swing.JLabel jLabel14;
	static javax.swing.JLabel jLabel15;
	static javax.swing.JLabel jLabel16;
	static javax.swing.JLabel jLabel17;
	static javax.swing.JLabel jLabel18;
	static javax.swing.JLabel jLabel19;
	static javax.swing.JLabel jLabel2;
	static javax.swing.JLabel jLabel20;
	static javax.swing.JLabel jLabel21;
	static javax.swing.JLabel jLabel22;
	static javax.swing.JLabel jLabel23;
	static javax.swing.JLabel jLabel24;
	static javax.swing.JLabel jLabel25;
	static javax.swing.JLabel jLabel3;
	static javax.swing.JLabel jLabel4;
	static javax.swing.JLabel jLabel5;
	static javax.swing.JLabel jLabel6;
	static javax.swing.JLabel jLabel7;
	static javax.swing.JLabel jLabel8;
	static javax.swing.JLabel jLabel9;
	static javax.swing.JPanel jPanel1;
	static javax.swing.JPanel jPanel2;
	static javax.swing.JPanel jPanel3;
	static javax.swing.JPanel jPanel4;
	static javax.swing.JPanel jPanel5;
	static javax.swing.JPanel jPanel6;
	static javax.swing.JPanel jPanel8;
	static javax.swing.JPanel jPanel9;
	static javax.swing.JScrollPane jScrollPane1;
	static javax.swing.JScrollPane jScrollPane2;
	static javax.swing.JTabbedPane jTabSetting;
	static javax.swing.JTextArea jTextArea1;
	static javax.swing.JPanel jpConnectSetting;
	static javax.swing.JScrollPane jpLog;
	static javax.swing.JPanel jpSNDetail;
	static javax.swing.JLabel lblCommandName;
	static javax.swing.JLabel lblTime;
	static javax.swing.JPanel pnlTCPClient;
	static javax.swing.JPanel pnlTCPServer;
	static javax.swing.JProgressBar prCommand;
	static javax.swing.JTable tblCard;
	static javax.swing.JTextField txtCardAutoCreateSzie;
	static javax.swing.JTextField txtCardData;
	static javax.swing.JTextField txtCardPassword;
	static javax.swing.JTextField txtLocalPort;
	public static javax.swing.JTextArea txtLog;
	static javax.swing.JTextField txtOpenTimes;
	static javax.swing.JTextField txtPassword;
	static javax.swing.JTextField txtReadTransactionDatabaseByIndex;
	static javax.swing.JTextField txtReadTransactionDatabaseByQuantity;
	static javax.swing.JTextField txtReadTransactionDatabasePacketSize;
	static javax.swing.JTextField txtReadTransactionDatabaseQuantity;
	static javax.swing.JTextField txtSN;
	static javax.swing.JTextField txtTCPServerIP;
	static javax.swing.JTextField txtTCPServerPort;
	static javax.swing.JTextField txtTransactionDatabaseReadIndex;
	static javax.swing.JTextField txtTransactionDatabaseWriteIndex;
	static javax.swing.JTextField txtUDPRemoteIP;
	static javax.swing.JTextField txtUDPRemotePort;
	static javax.swing.JTextField txtWriteSN;
	// endregion
	// region 变量定义
	public final static Logger myLog = Logger.getLogger(FormMain.class);
	public static ConnectorAllocator _Allocator;
	// public static ConcurrentHashMap<String, String> CommandName
	public static ConcurrentHashMap<String, CommandResultCallback> CommandResult;
	// public Timer timer = new Timer()
	public static boolean mIsClose;
	public static StringBuilder strLog;
	public static TCPDetail mReadTCPDetail;
	public static String[] CardStatusList = new String[]{"正常", "挂失卡", "黑名单"};
	public static String[] PrivilegeList = new String[]{"无", "首卡特权卡", "常开特权卡", "巡更卡", "防盗设置卡"};
	public static String OpenTimesUnlimited = "无限制";
	public static ArrayList<CardDetail> mCardList;
	public static int SearchTimes = 0;
	public static int SearchNetFlag;
	public static String[] mWatchTypeNameList;
	public static String[] mCardTransactionList, mButtonTransactionList, mDoorSensorTransactionList, mSoftwareTransactionList, mAlarmTransactionList, mSystemTransactionList;
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
		// 加入按钮组
		bgConnectType.add(RadUDP);
		bgConnectType.add(RadTCPClient);
		bgConnectType.add(RadTCPServer);
		RadTCPClient.setSelected(true);
		// 初始化timer
		// timer = new Timer();
		// timer.schedule(new TimeTask(), 200, 1000);
		ShowConnectPanel();
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
		FormMainFunctionUtils.IniCardDataBase();
		FormUtils.iniWatchEvent();
	}
	/**
	 * 初始化界面内容
	 */
	public void initComponents() {
		bgConnectType = new javax.swing.ButtonGroup();
		jpConnectSetting = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		RadTCPClient = new javax.swing.JRadioButton();
		RadUDP = new javax.swing.JRadioButton();
		RadTCPServer = new javax.swing.JRadioButton();
		jPanel9 = new javax.swing.JPanel();
		pnlTCPClient = new javax.swing.JPanel();
		jLabel24 = new javax.swing.JLabel();
		txtTCPServerIP = new javax.swing.JTextField();
		jLabel25 = new javax.swing.JLabel();
		txtTCPServerPort = new javax.swing.JTextField();
		PnlUDP = new javax.swing.JPanel();
		jLabel4 = new javax.swing.JLabel();
		txtUDPRemoteIP = new javax.swing.JTextField();
		jLabel5 = new javax.swing.JLabel();
		txtUDPRemotePort = new javax.swing.JTextField();
		pnlTCPServer = new javax.swing.JPanel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		txtLocalPort = new javax.swing.JTextField();
		butBeginServer = new javax.swing.JButton();
		jComboBox1 = new javax.swing.JComboBox<>();
		jpSNDetail = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		txtSN = new javax.swing.JTextField();
		txtPassword = new javax.swing.JTextField();
		jLabel7 = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		LblDate = new javax.swing.JLabel();
		lblTime = new javax.swing.JLabel();
		prCommand = new javax.swing.JProgressBar();
		lblCommandName = new javax.swing.JLabel();
		jpLog = new javax.swing.JScrollPane();
		txtLog = new javax.swing.JTextArea();
		jTabSetting = new javax.swing.JTabbedPane();
		jPanel6 = new javax.swing.JPanel();
		butReadConnectPassword = new javax.swing.JButton();
		butWriteConnectPassword = new javax.swing.JButton();
		butResetConnectPassword = new javax.swing.JButton();
		butReadTCPSetting = new javax.swing.JButton();
		butWriteTCPSetting = new javax.swing.JButton();
		ButReadDeadline = new javax.swing.JButton();
		ButWriteDeadline = new javax.swing.JButton();
		butReadVersion = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		jLabel8 = new javax.swing.JLabel();
		txtWriteSN = new javax.swing.JTextField();
		butWriteSN = new javax.swing.JButton();
		butReadSN = new javax.swing.JButton();
		butBeginWatch = new javax.swing.JButton();
		butCloseWatch = new javax.swing.JButton();
		butCloseAlarm = new javax.swing.JButton();
		butOpenDoor = new javax.swing.JButton();
		butCloseDoor = new javax.swing.JButton();
		butLockDoor = new javax.swing.JButton();
		butUnlockDoor = new javax.swing.JButton();
		butHoldDoor = new javax.swing.JButton();
		butAutoSearchDoor = new javax.swing.JButton();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
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
		setMinimumSize(new java.awt.Dimension(1020, 850));
		setSize(new java.awt.Dimension(1024, 768));
		getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jpConnectSetting.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "通讯参数", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));
		jpConnectSetting.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jLabel1.setText("通讯方式：");
		jpConnectSetting.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 21, -1, -1));
		RadTCPClient.setText("TCP客户端");
		RadTCPClient.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RadTCPClientActionPerformed(evt);
			}
		});
		jpConnectSetting.add(RadTCPClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(76, 17, -1, -1));
		RadUDP.setText("UDP");
		RadUDP.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RadUDPActionPerformed(evt);
			}
		});
		jpConnectSetting.add(RadUDP, new org.netbeans.lib.awtextra.AbsoluteConstraints(155, 17, -1, -1));
		RadTCPServer.setText("TCP服务器");
		RadTCPServer.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				RadTCPServerActionPerformed(evt);
			}
		});
		jpConnectSetting.add(RadTCPServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 17, -1, -1));
		pnlTCPClient.setBorder(javax.swing.BorderFactory.createTitledBorder("TCP 客户端"));
		pnlTCPClient.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel24.setText("IP地址：");
		pnlTCPClient.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 20, 79, -1));
		txtTCPServerIP.setText("192.168.1.150");
		pnlTCPClient.add(txtTCPServerIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 17, 223, -1));
		jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel25.setText("端口号：");
		pnlTCPClient.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 47, 79, -1));
		txtTCPServerPort.setText("8000");
		pnlTCPClient.add(txtTCPServerPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 44, 72, -1));
		PnlUDP.setBorder(javax.swing.BorderFactory.createTitledBorder("UDP"));
		PnlUDP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel4.setText("IP地址：");
		PnlUDP.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 20, 79, -1));
		txtUDPRemoteIP.setText("192.168.1.169");
		PnlUDP.add(txtUDPRemoteIP, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 17, 223, 20));
		jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel5.setText("端口号：");
		PnlUDP.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 47, 79, -1));
		txtUDPRemotePort.setText("8101");
		PnlUDP.add(txtUDPRemotePort, new org.netbeans.lib.awtextra.AbsoluteConstraints(99, 44, 72, -1));
		pnlTCPServer.setBorder(javax.swing.BorderFactory.createTitledBorder("TCP 客户端"));
		pnlTCPServer.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel2.setText("客户端列表：");
		pnlTCPServer.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 48, 79, -1));
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel3.setText("监听端口：");
		pnlTCPServer.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 79, -1));
		txtLocalPort.setText("8000");
		pnlTCPServer.add(txtLocalPort, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 72, -1));
		butBeginServer.setText("开始监听");
		pnlTCPServer.add(butBeginServer, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, -1, -1));
		jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
		pnlTCPServer.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 45, 240, -1));
		javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
		jPanel9.setLayout(jPanel9Layout);
		jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(pnlTCPClient, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(PnlUDP, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(pnlTCPServer, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(0, 0, Short.MAX_VALUE)));
		jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addComponent(pnlTCPClient, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(2, 2, 2).addComponent(PnlUDP, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(pnlTCPServer, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlTCPServer.getAccessibleContext().setAccessibleName("TCP 服务器");
		jpConnectSetting.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(4, 35, 340, 75));
		getContentPane().add(jpConnectSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 10, 350, 115));
		jpSNDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
		jpSNDetail.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel6.setText("SN：");
		jpSNDetail.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(13, 3, 36, -1));
		txtSN.setText("MC-5912T19060002");
		jpSNDetail.add(txtSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 0, 131, -1));
		txtPassword.setText("123456FF");
		jpSNDetail.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 0, 88, -1));
		jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel7.setText("通讯密码：");
		jpSNDetail.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 3, 69, -1));
		getContentPane().add(jpSNDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 130, 349, 23));
		jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		// NOI18N
		LblDate.setFont(new java.awt.Font("宋体", Font.PLAIN, 14));
		LblDate.setForeground(new java.awt.Color(0, 0, 255));
		LblDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		LblDate.setText("获取日期");
		jPanel3.add(LblDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 121, -1));
		// NOI18N
		lblTime.setFont(new java.awt.Font("宋体", Font.PLAIN, 14));
		lblTime.setForeground(new java.awt.Color(0, 0, 255));
		lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblTime.setText("18:09:07");
		jPanel3.add(lblTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 121, 20));
		jPanel3.add(prCommand, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, -1, -1));
		lblCommandName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		lblCommandName.setText("当前命令：");
		lblCommandName.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		jPanel3.add(lblCommandName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 146, 80));
		getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 14, -1, 140));
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
		getContentPane().add(jpLog, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 163, 501, 590));
		// NOI18N
		jTabSetting.setName("");
		jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel6.setLayout(null);
		butReadConnectPassword.setText("读通讯密码");
		butReadConnectPassword.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadConnectPasswordActionPerformed(evt);
			}
		});
		jPanel6.add(butReadConnectPassword);
		butReadConnectPassword.setBounds(20, 170, 93, 23);
		butWriteConnectPassword.setText("写通讯密码");
		butWriteConnectPassword.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butWriteConnectPasswordActionPerformed(evt);
			}
		});
		jPanel6.add(butWriteConnectPassword);
		butWriteConnectPassword.setBounds(250, 170, 93, 23);
		butResetConnectPassword.setText("复位通讯密码");
		butResetConnectPassword.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butResetConnectPasswordActionPerformed(evt);
			}
		});
		jPanel6.add(butResetConnectPassword);
		butResetConnectPassword.setBounds(130, 170, 105, 23);
		butReadTCPSetting.setText("读TCP参数");
		butReadTCPSetting.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadTCPSettingActionPerformed(evt);
			}
		});
		jPanel6.add(butReadTCPSetting);
		butReadTCPSetting.setBounds(20, 130, 109, 23);
		butWriteTCPSetting.setText("写TCP参数");
		butWriteTCPSetting.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butWriteTCPSettingActionPerformed(evt);
			}
		});
		jPanel6.add(butWriteTCPSetting);
		butWriteTCPSetting.setBounds(140, 130, 116, 23);
		ButReadDeadline.setText("读取控制器有效天数");
		ButReadDeadline.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ButReadDeadlineActionPerformed(evt);
			}
		});
		jPanel6.add(ButReadDeadline);
		ButReadDeadline.setBounds(310, 90, 190, 23);
		ButWriteDeadline.setText("修改控制器有效天数");
		ButWriteDeadline.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ButWriteDeadlineActionPerformed(evt);
			}
		});
		jPanel6.add(ButWriteDeadline);
		ButWriteDeadline.setBounds(350, 120, 151, 23);
		butReadVersion.setText("读取版本号");
		butReadVersion.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadVersionActionPerformed(evt);
			}
		});
		jPanel6.add(butReadVersion);
		butReadVersion.setBounds(10, 80, 138, 23);
		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("SN"));
		jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel8.setText("SN：");
		jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 36, -1));
		txtWriteSN.setText("FC-8940A46060007");
		jPanel1.add(txtWriteSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 131, -1));
		butWriteSN.setText("写");
		butWriteSN.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butWriteSNActionPerformed(evt);
			}
		});
		jPanel1.add(butWriteSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 20, -1, -1));
		butReadSN.setText("读");
		butReadSN.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadSNActionPerformed(evt);
			}
		});
		jPanel1.add(butReadSN, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, -1, -1));
		jPanel6.add(jPanel1);
		jPanel1.setBounds(10, 10, 310, 50);
		butBeginWatch.setText("打开数据监控");
		butBeginWatch.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butBeginWatchActionPerformed(evt);
			}
		});
		jPanel6.add(butBeginWatch);
		butBeginWatch.setBounds(30, 220, 140, 23);
		butCloseWatch.setText("关闭数据监控");
		butCloseWatch.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butCloseWatchActionPerformed(evt);
			}
		});
		jPanel6.add(butCloseWatch);
		butCloseWatch.setBounds(180, 220, 130, 23);
		butCloseAlarm.setText("解除所有报警");
		butCloseAlarm.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butCloseAlarmActionPerformed(evt);
			}
		});
		jPanel6.add(butCloseAlarm);
		butCloseAlarm.setBounds(30, 270, 160, 23);
		butOpenDoor.setText("远程开门");
		butOpenDoor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butOpenDoorActionPerformed(evt);
			}
		});
		jPanel6.add(butOpenDoor);
		butOpenDoor.setBounds(30, 330, 81, 23);
		butCloseDoor.setText("远程关门");
		butCloseDoor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butCloseDoorActionPerformed(evt);
			}
		});
		jPanel6.add(butCloseDoor);
		butCloseDoor.setBounds(120, 330, 81, 23);
		butLockDoor.setText("远程锁定");
		butLockDoor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butLockDoorActionPerformed(evt);
			}
		});
		jPanel6.add(butLockDoor);
		butLockDoor.setBounds(300, 330, 81, 23);
		butUnlockDoor.setText("远程解锁");
		butUnlockDoor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butUnlockDoorActionPerformed(evt);
			}
		});
		jPanel6.add(butUnlockDoor);
		butUnlockDoor.setBounds(390, 330, 81, 23);
		butHoldDoor.setText("远程常开");
		butHoldDoor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butHoldDoorActionPerformed(evt);
			}
		});
		jPanel6.add(butHoldDoor);
		butHoldDoor.setBounds(210, 330, 81, 23);
		butAutoSearchDoor.setText("自动搜索控制器");
		butAutoSearchDoor.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butAutoSearchDoorActionPerformed(evt);
			}
		});
		jPanel6.add(butAutoSearchDoor);
		butAutoSearchDoor.setBounds(20, 390, 130, 23);
		jTextArea1.setEditable(false);
		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jTextArea1.setText("UDP通讯只支持如下指令【搜索不同网络标识设备】、【设置设备网络标识】【读TCP参数】、【写TCP参数】");
		jScrollPane2.setViewportView(jTextArea1);
		jPanel6.add(jScrollPane2);
		jScrollPane2.setBounds(20, 500, 390, 110);
		jTabSetting.addTab("系统设置", jPanel6);
		jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		jButton1.setText("读时间");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		jPanel8.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, -1, -1));
		jButton2.setText("写时间");
		jButton2.setActionCommand("");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});
		jPanel8.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, -1, -1));
		jTabSetting.addTab("时间参数", jPanel8);
		jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		butReadCardDataBase.setText("从控制器中读取卡片数据");
		butReadCardDataBase.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadCardDataBaseActionPerformed(evt);
			}
		});
		jPanel2.add(butReadCardDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, -1, -1));
		butReadCardDatabaseDetail.setText("读卡数据库信息");
		butReadCardDatabaseDetail.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadCardDatabaseDetailActionPerformed(evt);
			}
		});
		jPanel2.add(butReadCardDatabaseDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 130, -1));
		cmbCardDataBaseType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
		jPanel2.add(cmbCardDataBaseType, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 130, -1));
		butClearCardDataBase.setBackground(new java.awt.Color(255, 51, 51));
		butClearCardDataBase.setForeground(new java.awt.Color(255, 0, 51));
		butClearCardDataBase.setText("清空卡片数据库");
		butClearCardDataBase.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butClearCardDataBaseActionPerformed(evt);
			}
		});
		jPanel2.add(butClearCardDataBase, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 50, -1, -1));
		jLabel9.setText("卡区域：");
		jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 52, -1, -1));
		tblCard.setModel(new javax.swing.table.DefaultTableModel(new Object[][]{}, new String[]{}));
		jScrollPane1.setViewportView(tblCard);
		jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 480, 400));
		butReadCardDetail.setText("读取单个卡片在控制器中的信息");
		butReadCardDetail.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadCardDetailActionPerformed(evt);
			}
		});
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
		TxtCardExpiry.setText("2030-12-30 12:30");
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
		butAddCardToList.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butAddCardToListActionPerformed(evt);
			}
		});
		jPanel2.add(butAddCardToList, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 630, 160, -1));
		butCardListClear.setText("清空列表");
		butCardListClear.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butCardListClearActionPerformed(evt);
			}
		});
		jPanel2.add(butCardListClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 490, 110, -1));
		butUploadCard.setText("将卡号上传至非排序区");
		butUploadCard.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butUploadCardActionPerformed(evt);
			}
		});
		jPanel2.add(butUploadCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 630, -1, -1));
		butDeleteCard.setText("将卡号从控制器删除");
		butDeleteCard.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butDeleteCardActionPerformed(evt);
			}
		});
		jPanel2.add(butDeleteCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 630, -1, -1));
		butCardListAutoCreate.setText("自动生成卡号");
		butCardListAutoCreate.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butCardListAutoCreateActionPerformed(evt);
			}
		});
		jPanel2.add(butCardListAutoCreate, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 160, -1));
		butWriteCardListBySequence.setText("将列表卡号上传至非排序区");
		butWriteCardListBySequence.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butWriteCardListBySequenceActionPerformed(evt);
			}
		});
		jPanel2.add(butWriteCardListBySequence, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 690, -1, -1));
		butWriteCardListBySort.setText("将列表卡号上传至排序区");
		butWriteCardListBySort.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butWriteCardListBySortActionPerformed(evt);
			}
		});
		jPanel2.add(butWriteCardListBySort, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 690, -1, -1));
		butDeleteCardByList.setText("删除列表中的卡片");
		butDeleteCardByList.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butDeleteCardByListActionPerformed(evt);
			}
		});
		jPanel2.add(butDeleteCardByList, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 690, -1, -1));
		jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel16.setText("自动生成数量：");
		jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 663, 90, -1));
		txtCardAutoCreateSzie.setText("2000");
		jPanel2.add(txtCardAutoCreateSzie, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 660, 70, -1));
		jTabSetting.addTab("卡片管理", jPanel2);
		jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		butReadTransactionDatabaseDetail.setText("读取记录数据库详情");
		butReadTransactionDatabaseDetail.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadTransactionDatabaseDetailActionPerformed(evt);
			}
		});
		jPanel4.add(butReadTransactionDatabaseDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));
		butTransactionDatabaseEmpty.setBackground(new java.awt.Color(255, 0, 0));
		butTransactionDatabaseEmpty.setForeground(new java.awt.Color(255, 0, 0));
		butTransactionDatabaseEmpty.setText("清空所有记录");
		butTransactionDatabaseEmpty.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butTransactionDatabaseEmptyActionPerformed(evt);
			}
		});
		jPanel4.add(butTransactionDatabaseEmpty, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 10, -1, -1));
		jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("指定记录类型的操作"));
		jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
		butClearTransactionDatabase.setBackground(new java.awt.Color(255, 0, 0));
		butClearTransactionDatabase.setForeground(new java.awt.Color(255, 0, 0));
		butClearTransactionDatabase.setText("清空记录");
		butClearTransactionDatabase.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butClearTransactionDatabaseActionPerformed(evt);
			}
		});
		jPanel5.add(butClearTransactionDatabase, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));
		jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel17.setText("记录类型：");
		jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 80, -1));
		cmbTransactionType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"读卡记录", "出门开关记录", "门磁记录", "软件操作记录", "报警记录", "系统记录"}));
		jPanel5.add(cmbTransactionType, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 20, 110, -1));
		butReadTransactionDatabaseByIndex.setText("按序号读记录");
		butReadTransactionDatabaseByIndex.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadTransactionDatabaseByIndexActionPerformed(evt);
			}
		});
		jPanel5.add(butReadTransactionDatabaseByIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 120, -1));
		butWriteTransactionDatabaseReadIndex.setText("修改记录读索引");
		butWriteTransactionDatabaseReadIndex.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butWriteTransactionDatabaseReadIndexActionPerformed(evt);
			}
		});
		jPanel5.add(butWriteTransactionDatabaseReadIndex, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));
		butWriteTransactionDatabaseWriteIndex.setText("修改记录写索引");
		butWriteTransactionDatabaseWriteIndex.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butWriteTransactionDatabaseWriteIndexActionPerformed(evt);
			}
		});
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
		butReadTransactionDatabase.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				butReadTransactionDatabaseActionPerformed(evt);
			}
		});
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
		getContentPane().add(jTabSetting, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 10, 510, 750));
		pack();
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
				CommandResult.get(sKey).ResultToLog(strBuf, result);
			}
			else {
				strBuf.append("命令处理完毕!");
			}
			// FormUtils.AddLog(strBuf.toString(),strLog,txtLog);
			FormUtils.addTxtLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			// System.out.println("fcardiodemo.FormMain.CommandCompleteEvent() -- 发生错误：" + e.toString());
			Logger.getRootLogger().error("fcardiodemo.FormMain.CommandCompleteEvent() -- 发生错误：" + e);
		}
	}
	@Override
	public void CommandProcessEvent(INCommand cmd) {
		try {
			// lblCommandName.setText("当前命令：" + GetCommandName(cmd) + "正在处理" );
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
				// prCommand.setValue(0);
				prCommand.setMaximum(cmd.getProcessMax());
			}
			prCommand.setValue(cmd.getProcessStep());
			// AddLog(strBuf.toString());
			strBuf = null;
		}
		catch (Exception e) {
			System.out.println("fcardiodemo.FormMain.CommandProcessEvent() -- 发生错误：" + e.toString());
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
			System.out.println("fcardiodemo.FormMain.ConnectorErrorEvent() --- " + e.toString());
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
			System.out.println("fcardiodemo.FormMain.ConnectorErrorEvent() -- " + e.toString());
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
			System.out.println("fcardiodemo.FormMain.CommandTimeout() -- " + e.toString());
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
			System.out.println("fcardiodemo.FormMain.PasswordErrorEvent() -- " + e.toString());
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
			System.out.println("fcardiodemo.FormMain.ChecksumErrorEvent() -- " + e.toString());
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
			System.out.println("fcardiodemo.FormMain.WatchEvent() -- " + e.toString());
		}
	}
	/* ============================================================================ */
	public interface CommandResultCallback {
		void ResultToLog(StringBuilder strBuf, INCommandResult result);
	}
	void RadTCPClientActionPerformed(java.awt.event.ActionEvent evt) {
		ShowConnectPanel();
	}
	void RadUDPActionPerformed(java.awt.event.ActionEvent evt) {
		ShowConnectPanel();
	}
	void RadTCPServerActionPerformed(java.awt.event.ActionEvent evt) {
		ShowConnectPanel();
	}
	public void ShowConnectPanel() {
		pnlTCPServer.setVisible(false);
		PnlUDP.setVisible(false);
		pnlTCPClient.setVisible(false);
		if (RadTCPClient.isSelected()) {
			pnlTCPClient.setVisible(true);
		}
		if (RadUDP.isSelected()) {
			PnlUDP.setVisible(true);
		}
		if (RadTCPServer.isSelected()) {
			pnlTCPServer.setVisible(true);
		}
	}
	/* ============================================================================ */
//	void RadTCPClientActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		ShowConnectPanel();
//	}
//	void RadUDPActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		ShowConnectPanel();
//	}
//	void RadTCPServerActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		ShowConnectPanel();
//	}
//	void txtLogMouseClicked(java.awt.event.MouseEvent evt) {
//		if (evt.getClickCount() == 3) {
//			strLog.delete(0, strLog.length());
//			txtLog.setText("");
//		}
//	}
//	/**
//	 * 读取新记录
//	 * @param evt evt
//	 */
//	void butReadTransactionDatabaseActionPerformed(java.awt.event.ActionEvent evt) {
//		// 读取新记录
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		String strPacketSize = txtReadTransactionDatabasePacketSize.getText();
//		if (!StringUtil.IsNum(strPacketSize) || strPacketSize.length() > 3) {
//			JOptionPane.showMessageDialog(null, "单次读取数量必须为数字，取值范围1-300！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int packetSize = Integer.parseInt(strPacketSize);
//		if (packetSize > 300 || packetSize < 0) {
//			JOptionPane.showMessageDialog(null, "单次读取数量必须为数字，取值范围1-300！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		String strQuantity = txtReadTransactionDatabaseQuantity.getText();
//		if (!StringUtil.IsNum(strQuantity) || strQuantity.length() > 6) {
//			JOptionPane.showMessageDialog(null, "读新记录数量必须是数字，取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int quantity = Integer.parseInt(strQuantity);
//		if (quantity < 0) {
//			JOptionPane.showMessageDialog(null, "读新记录数量必须大于0！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		ReadTransactionDatabase_Parameter par = new ReadTransactionDatabase_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		par.PacketSize = packetSize;
//		par.Quantity = quantity;
//		// 徐铭康修改
//		// ReadTransactionDatabase cmd = new ReadTransactionDatabase(par);
//		ReadTransactionDatabase cmd = new ReadTransactionDatabase(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadTransactionDatabase_Result result = (ReadTransactionDatabase_Result) y;
//			// x.append("记录类型：");
//			x.append("记录类型：");
//			x.append(mWatchTypeNameList[result.DatabaseType.getValue()]);
//			x.append("，本次读取数量：");
//			x.append(result.Quantity);
//			x.append("，未读取新记录数量：");
//			x.append(result.readable);
//			if (result.Quantity > 0) {
//				// 开始写出记录日志
//				StringBuilder log = new StringBuilder(result.Quantity * 100);
//				String[] sTransactionList = null;
//				switch (result.DatabaseType) {
//					case OnCardTransaction:
//						sTransactionList = mCardTransactionList;
//						break;
//					case OnButtonTransaction:
//						sTransactionList = mButtonTransactionList;
//						break;
//					case OnDoorSensorTransaction:
//						sTransactionList = mDoorSensorTransactionList;
//						break;
//					case OnSoftwareTransaction:
//						sTransactionList = mSoftwareTransactionList;
//						break;
//					case OnAlarmTransaction:
//						sTransactionList = mAlarmTransactionList;
//						break;
//					case OnSystemTransaction:
//						sTransactionList = mSystemTransactionList;
//						break;
//					default:
//						Logger.getRootLogger().info("");
//				}
//				PrintTransactionDatabase(result.TransactionList, log, sTransactionList);
//				String path = WriteFile("门禁调试器记录数据库", log, false);
//				if (path == null) {
//					x.append("写日志失败！");
//				}
//				else {
//					x.append("以保存到日志文件，");
//					x.append(path);
//				}
//				if (result.Quantity < 1000) {
//					x.append(log);
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butWriteTransactionDatabaseWriteIndexActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		String strWriteIndex = txtTransactionDatabaseWriteIndex.getText();
//		if (!StringUtil.IsNum(strWriteIndex) || strWriteIndex.length() > 6) {
//			JOptionPane.showMessageDialog(null, "写索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int index = Integer.parseInt(strWriteIndex);
//		if (index > 160000 || index < 0) {
//			JOptionPane.showMessageDialog(null, "写索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		WriteTransactionDatabaseWriteIndex_Parameter par = new WriteTransactionDatabaseWriteIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		par.WriteIndex = index;
//		WriteTransactionDatabaseWriteIndex cmd = new WriteTransactionDatabaseWriteIndex(par);
//		_Allocator.AddCommand(cmd);
//	}
//	void butWriteTransactionDatabaseReadIndexActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		String strReadIndex = txtTransactionDatabaseReadIndex.getText();
//		if (!StringUtil.IsNum(strReadIndex) || strReadIndex.length() > 6) {
//			JOptionPane.showMessageDialog(null, "读索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int index = Integer.parseInt(strReadIndex);
//		if (index > 160000 || index < 0) {
//			JOptionPane.showMessageDialog(null, "读索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		WriteTransactionDatabaseReadIndex_Parameter par = new WriteTransactionDatabaseReadIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		par.ReadIndex = index;
//		par.IsCircle = chkTransactionIsCircle.isSelected();
//		WriteTransactionDatabaseReadIndex cmd = new WriteTransactionDatabaseReadIndex(par);
//		_Allocator.AddCommand(cmd);
//	}
//	void butReadTransactionDatabaseByIndexActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		String strReadIndex = txtReadTransactionDatabaseByIndex.getText();
//		if (!StringUtil.IsNum(strReadIndex) || strReadIndex.length() > 6) {
//			JOptionPane.showMessageDialog(null, "读记录起始索引号必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int index = Integer.parseInt(strReadIndex);
//		if (index > 160000 || index < 0) {
//			JOptionPane.showMessageDialog(null, "读记录起始索引号取值范围0-160000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		String strQuantity = txtReadTransactionDatabaseByQuantity.getText();
//		if (!StringUtil.IsNum(strQuantity) || strQuantity.length() > 3) {
//			JOptionPane.showMessageDialog(null, "读记录数量必须是数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		int Quantity = Integer.parseInt(strQuantity);
//		if (Quantity > 400 || Quantity <= 0) {
//			JOptionPane.showMessageDialog(null, "读记录数量取值范围1-400！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		ReadTransactionDatabaseByIndex_Parameter par = new ReadTransactionDatabaseByIndex_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		par.ReadIndex = index;
//		par.Quantity = Quantity;
//		// ReadTransactionDatabaseByIndex cmd = new ReadTransactionDatabaseByIndex(par);
//		ReadTransactionDatabaseByIndex cmd = new ReadTransactionDatabaseByIndex(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadTransactionDatabaseByIndex_Result result = (ReadTransactionDatabaseByIndex_Result) y;
//			x.append("记录类型：");
//			x.append(mWatchTypeNameList[result.DatabaseType.getValue()]);
//			x.append("，起始号：");
//			x.append(result.ReadIndex);
//			x.append("，读取数量：");
//			x.append(result.Quantity);
//			if (result.Quantity > 0) {
//				// 开始写出记录日志
//				StringBuilder log = new StringBuilder(result.Quantity * 100);
//				String[] sTransactionList = null;
//				switch (result.DatabaseType) {
//					case OnCardTransaction:
//						sTransactionList = mCardTransactionList;
//						break;
//					case OnButtonTransaction:
//						sTransactionList = mButtonTransactionList;
//						break;
//					case OnDoorSensorTransaction:
//						sTransactionList = mDoorSensorTransactionList;
//						break;
//					case OnSoftwareTransaction:
//						sTransactionList = mSoftwareTransactionList;
//						break;
//					case OnAlarmTransaction:
//						sTransactionList = mAlarmTransactionList;
//						break;
//					case OnSystemTransaction:
//						sTransactionList = mSystemTransactionList;
//						break;
//				}
//				PrintTransactionDatabase(result.TransactionList, log, sTransactionList);
//				x.append(log);
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butClearTransactionDatabaseActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		int type = cmbTransactionType.getSelectedIndex() + 1;
//		ClearTransactionDatabase_Parameter par = new ClearTransactionDatabase_Parameter(dt, e_TransactionDatabaseType.valueOf(type));
//		ClearTransactionDatabase cmd = new ClearTransactionDatabase(par);
//		_Allocator.AddCommand(cmd);
//	}
//	void butTransactionDatabaseEmptyActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		TransactionDatabaseEmpty cmd = new TransactionDatabaseEmpty(par);
//		_Allocator.AddCommand(cmd);
//	}
//	/**
//	 * 读取记录数据库详情
//	 * @param evt evt
//	 */
//	void butReadTransactionDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		ReadTransactionDatabaseDetail cmd = new ReadTransactionDatabaseDetail(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadTransactionDatabaseDetail_Result result = (ReadTransactionDatabaseDetail_Result) y;
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[1], result.DatabaseDetail.CardTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[2], result.DatabaseDetail.ButtonTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[3], result.DatabaseDetail.DoorSensorTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[4], result.DatabaseDetail.SoftwareTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[5], result.DatabaseDetail.AlarmTransactionDetail, x);
//			PrintTransactionDatabaseDetail(mWatchTypeNameList[6], result.DatabaseDetail.SystemTransactionDetail, x);
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butDeleteCardByListActionPerformed(java.awt.event.ActionEvent evt) {
//		// 删除列表中的卡片
//		if (mCardList == null) {
//			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;
//		// 此函数超时时间设定长一些
//		int ilstLen = mCardList.size();
//		// 徐铭康修改
//		// DeleteCard cmd = new DeleteCard(par);
//		FC8800Command cmd = null;
//		String[] lst = new String[ilstLen];
//		for (int i = 0; i < ilstLen; i++) {
//			lst[i] = mCardList.get(i).GetCardData();
//		}
//		DeleteCard_Parameter par = new DeleteCard_Parameter(dt, lst);
//		cmd = new DeleteCard(par);
//		_Allocator.AddCommand(cmd);
//	}
//	void butWriteCardListBySortActionPerformed(java.awt.event.ActionEvent evt) {
//		// 将列表中的卡片上传至排序区
//		if (mCardList == null) {
//			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;// 此函数超时时间设定长一些
//		WriteCardListBySort_Parameter par = new WriteCardListBySort_Parameter(dt, mCardList);
//		// 徐铭康修改
//		// WriteCardListBySort cmd = new WriteCardListBySort(par);
//		WriteCardListBySort cmd = new WriteCardListBySort(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			WriteCardListBySort_Result result = (WriteCardListBySort_Result) y;
//			ArrayList<? extends CardDetail> _list = result.CardList;
//			x.append("上传完毕");
//			if (result.FailTotal > 0) {
//				x.append("失败数量：");
//				x.append(result.FailTotal);
//				x.append("，卡号列表：");
//				for (CardDetail c : _list) {
//					x.append(c.GetCardData());
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butWriteCardListBySequenceActionPerformed(java.awt.event.ActionEvent evt) {
//		// 将列表中的卡片上传至非排序区
//		if (mCardList == null) {
//			JOptionPane.showMessageDialog(null, "卡片列表为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 8000;// 此函数超时时间设定长一些
//		// 徐铭康 修改
//		WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(dt, mCardList);
//		// WriteCardListBySequence cmd = new WriteCardListBySequence(par);
//		FC8800Command cmd = new WriteCardListBySequence(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			WriteCardListBySequence_Result result = (WriteCardListBySequence_Result) y;
//			ArrayList<? extends CardDetail> _list = result.CardList;
//			x.append("上传完毕");
//			if (result.FailTotal > 0) {
//				x.append("失败数量：");
//				x.append(result.FailTotal);
//				x.append("，卡号列表：");
//				for (CardDetail c : _list) {
//					x.append(c.GetCardData());
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butCardListAutoCreateActionPerformed(java.awt.event.ActionEvent evt) {
//		// 自动创建测试卡列表
//		int maxSize = 1000;
//		String strSize = txtCardAutoCreateSzie.getText();
//		if (!StringUtil.IsNum(strSize) || strSize.length() > 6) {
//			JOptionPane.showMessageDialog(null, "待生成的数量为数字，取值范围1-120000！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		maxSize = Integer.parseInt(strSize);
//		if (mCardList == null) {
//			mCardList = new ArrayList<>(10000);
//		}
//		if ((maxSize + mCardList.size()) > 120000) {
//			JOptionPane.showMessageDialog(null, "待生成的数量和列表中的卡数相加超过12万！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		Random rnd = new Random();
//		int max = 90000000;
//		int min = 10000000;
//		int iSearch = 0;
//		CardDetail surCard = GetCardDetail();
//		if (surCard == null) {
//			return;
//		}
//		Collections.sort(mCardList);
//		// 徐铭康修改
//		ArrayList<CardDetail> tmplst = new ArrayList<>(1500);
//		// Calendar time=Calendar.getInstance();
//		while (maxSize > 0) {
//			long card = rnd.nextInt(max) % (max - min + 1) + min;
//			// 徐铭康修改
//			// CardDetail cd = new CardDetail(card);
//			CardDetail cd = new CardDetail();
//			try {
//				cd.SetCardData(String.valueOf(card));
//			}
//			catch (Exception e) {
//				JOptionPane.showMessageDialog(null, "生成随机卡号异常", "卡片管理", JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//			iSearch = CardDetail.SearchCardDetail(mCardList, cd);
//			if (iSearch == -1) {
//				if (tmplst.indexOf(cd) == -1) {
//					cd.Password = surCard.Password;// 设定密码
//					cd.Expiry = surCard.Expiry;// 设定有效期
//					cd.OpenTimes = surCard.OpenTimes;// 开门次数
//					cd.CardStatus = surCard.CardStatus;
//					// 设定4个门的开门时段
//					for (int i = 1; i <= 4; i++) {
//						cd.SetTimeGroup(i, 1);// 每个门都设定为1门
//						cd.SetDoor(i, surCard.GetDoor(i));// 设定每个门权限
//					}
//					cd.SetNormal();// 设定卡片没有特权
//					cd.HolidayUse = true;// 设定受节假日限制。
//					tmplst.add(cd);
//					if (tmplst.size() >= 1000) {
//						mCardList.addAll(tmplst);
//						Collections.sort(mCardList);
//						tmplst.clear();
//					}
//					maxSize--;
//				}
//			}
//		}
//		if (tmplst.size() > 0) {
//			mCardList.addAll(tmplst);
//			Collections.sort(mCardList);
//			tmplst.clear();
//		}
//		// Calendar endtime=Calendar.getInstance();
//		// int hs=(int)(endtime.getTimeInMillis()- time.getTimeInMillis());
//		// System.out.println("耗时：" + hs);
//		FillCardToList();
//	}
//	void butDeleteCardActionPerformed(java.awt.event.ActionEvent evt) {
//		// 删除卡片
//		CardDetail cd = GetCardDetail();
//		if (cd == null) {
//			return;
//		}
//		// 读取控制器中的卡片数据库信息
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		// 此函数超时时间设定长一些
//		dt.Timeout = 5000;
//		// 徐铭康修改
//		// DeleteCard cmd = new DeleteCard(par);
//		String[] lst = new String[1];
//		lst[0] = cd.GetCardData();
//		DeleteCard_Parameter par = new DeleteCard_Parameter(dt, lst);
//		DeleteCard cmd = new DeleteCard(par);
//		_Allocator.AddCommand(cmd);
//	}
//	void butUploadCardActionPerformed(java.awt.event.ActionEvent evt) {
//		// 上传卡片至非排序区
//		CardDetail cd = GetCardDetail();
//		if (cd == null) {
//			return;
//		}
//		ArrayList<CardDetail> lst = new ArrayList<>(1);
//		lst.add(cd);
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;// 此函数超时时间设定长一些
//		// 徐铭康修改
//		WriteCardListBySequence_Parameter par = new WriteCardListBySequence_Parameter(dt, lst);
//		// WriteCardListBySequence cmd = new WriteCardListBySequence(par);
//		FC8800Command cmd = new WriteCardListBySequence(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			WriteCardListBySequence_Result result = (WriteCardListBySequence_Result) y;
//			x.append("上传完毕");
//			ArrayList<? extends CardDetail> _list = result.CardList;
//			if (result.FailTotal > 0) {
//				x.append("失败数量：");
//				x.append(result.FailTotal);
//				x.append("，卡号列表：");
//				for (CardDetail c : _list) {
//					x.append(c.GetCardData());
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butCardListClearActionPerformed(java.awt.event.ActionEvent evt) {
//		ClearCardList();
//		mCardList.clear();
//		mCardList = null;
//	}
//	void butAddCardToListActionPerformed(java.awt.event.ActionEvent evt) {
//		CardDetail cd = GetCardDetail();
//		if (cd == null) {
//			return;
//		}
//		if (mCardList == null) {
//			mCardList = new ArrayList<>(1000);
//		}
//		// 检查重复
//		int iIndex = mCardList.indexOf(cd);
//		if (iIndex > -1) {
//			mCardList.remove(iIndex);
//		}
//		mCardList.add(cd);
//		if (iIndex > -1) {
//			FillCardToList();// 刷新列表
//		}
//		else {
//			Object[] row = CardDetailToRow(cd, mCardList.size());
//			DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//			tableModel.addRow(row);
//		}
//	}
//	void butReadCardDetailActionPerformed(java.awt.event.ActionEvent evt) {
//		CardDetail cd = GetCardDetail();
//		if (cd == null) {
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;// 此函数超时时间设定长一些
//		ReadCardDetail_Parameter par = new ReadCardDetail_Parameter(dt, cd.GetCardData());
//		// ReadCardDetail cmd = new ReadCardDetail(par);
//		FC8800Command cmd = new ReadCardDetail(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadCardDetail_Result result = (ReadCardDetail_Result) y;
//			if (result.IsReady) {
//				CardDetail card = (CardDetail) result.Card;
//				x.append("卡片在数据库中存储，卡片信息：");
//				Object[] arr = CardDetailToRow(card, 0);
//				StringBuilder builder = new StringBuilder(200);
//				builder.append("卡号：");
//				builder.append(arr[1]);// cd.CardData
//				builder.append("，密码：");
//				builder.append(arr[2]);// cd.Password
//				builder.append("，有效期：");
//				builder.append(arr[3]);// cd.Expiry
//				builder.append("有效次数：");
//				builder.append(arr[5]);// OpenTimes
//				builder.append("，特权：");
//				builder.append(arr[6]);// Privilege
//				builder.append("，卡状态：");
//				builder.append(arr[4]);// cd.CardStatus
//				builder.append("权限和时段：");
//				int arrIndex = 7;
//				for (int i = 1; i <= 4; i++) {
//					builder.append("门");
//					builder.append(i);
//					builder.append("：");
//					builder.append(arr[arrIndex]);
//					arrIndex++;
//					builder.append("权限");
//					builder.append(",开门时段:");
//					builder.append(arr[arrIndex]);
//					arrIndex++;
//					builder.append("；");
//				}
//				builder.append("节假日限制:");
//				builder.append(arr[arrIndex]);
//				x.append(builder);
//				builder = null;
//			}
//			else {
//				x.append("卡片未在数据库中存储！");
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butClearCardDataBaseActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 5000;
//		int iType = cmbCardDataBaseType.getSelectedIndex() + 1;
//		ClearCardDataBase_Parameter par = new ClearCardDataBase_Parameter(dt, iType);
//		ClearCardDataBase cmd = new ClearCardDataBase(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) y;
//			x.append("清空区域：");
//			x.append(cmbCardDataBaseType.getSelectedItem());
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butReadCardDatabaseDetailActionPerformed(java.awt.event.ActionEvent evt) {
//		// 读取控制器中的卡片数据库信息
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		ReadCardDatabaseDetail cmd = new ReadCardDatabaseDetail(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadCardDatabaseDetail_Result result = (ReadCardDatabaseDetail_Result) y;
//			x.append("排序区卡容量：");
//			x.append(result.SortDataBaseSize);
//			x.append("；排序区卡数量：");
//			x.append(result.SortCardSize);
//			x.append("非排序区卡容量：");
//			x.append(result.SequenceDataBaseSize);
//			x.append("；非排序区卡数量：");
//			x.append(result.SequenceCardSize);
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butReadCardDataBaseActionPerformed(java.awt.event.ActionEvent evt) {
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 2000;
//		int iType = cmbCardDataBaseType.getSelectedIndex() + 1;
//		ReadCardDataBase_Parameter par = new ReadCardDataBase_Parameter(dt, iType);
//		// 徐铭康修改
//		// ReadCardDataBase cmd = new ReadCardDataBase(par);
//		ReadCardDataBase cmd = new ReadCardDataBase(par);
//		String[] CardTypeList = new String[]{"", "排序区", "非排序区", "所有区域"};
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadCardDataBase_Result result = (ReadCardDataBase_Result) y;
//			x.append("读取卡片数量：");
//			x.append(result.DataBaseSize);
//			x.append("；读取卡存储区域：");
//			x.append(CardTypeList[result.CardType]);
//			if (result.DataBaseSize > 0) {
//				mCardList = result.CardList;
//				String[] CardStatusList = new String[]{"正常", "挂失卡", "黑名单"};
//				DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//				tableModel.setRowCount(0);// 清除原有行
//				int Index = 1;
//				StringBuilder builder = new StringBuilder(result.DataBaseSize * 140);
//				for (CardDetail cd : mCardList) {
//					Object[] arr = CardDetailToRow(cd, Index);
//					builder.append("卡号：");
//					builder.append(arr[1]);// cd.CardData
//					builder.append("，密码：");
//					builder.append(arr[2]);// cd.Password
//					builder.append("，有效期：");
//					builder.append(arr[3]);// cd.Expiry
//					builder.append("，有效次数：");
//					builder.append(arr[5]);// OpenTimes
//					builder.append("，特权：");
//					builder.append(arr[6]);// Privilege
//					builder.append("，卡状态：");
//					builder.append(arr[4]);// cd.CardStatus
//					builder.append("权限和时段：");
//					int arrIndex = 7;
//					for (int i = 1; i <= 4; i++) {
//						builder.append("门");
//						builder.append(i);
//						builder.append("：");
//						builder.append(arr[arrIndex]);
//						arrIndex++;
//						builder.append("权限");
//						builder.append(",开门时段:");
//						builder.append(arr[arrIndex]);
//						arrIndex++;
//						builder.append("；");
//					}
//					builder.append("节假日限制:");
//					builder.append(arr[arrIndex]);
//					// 添加数据到表格
//					tableModel.addRow(arr);
//					Index += 1;
//				}
//				// 更新表格
//				tblCard.invalidate();
//				try {
//					String file = WriteFile("CardDatabase", builder, false);
//					if (file == null) {
//						x.append("卡数据导出失败！");
//					}
//					else {
//						x.append("卡数据导出成功，地址：");
//						x.append(file);
//					}
//				}
//				catch (Exception e) {
//				}
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		CommandParameter par = new CommandParameter(dt);
//		WriteTime cmd = new WriteTime(par);
//		_Allocator.AddCommand(cmd);
//	}
//	void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
//		// TODO add your handling code here:
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		CommandParameter par = new CommandParameter(dt);
//		ReadTime cmd = new ReadTime(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			x.append("控制器时间：");
//			ReadTime_Result result = (ReadTime_Result) y;
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			x.append(sdf.format(result.ControllerDate.getTime()));
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	/**
//	 * 自动搜索控制器
//	 * @param evt
//	 */
//	void butAutoSearchDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butAutoSearchDoorActionPerformed
//		// 自动搜索门禁控制器
//		CommandDetail dt = new CommandDetail();
//		UDPDetail udp = new UDPDetail("255.255.255.255", 8101);
//		udp.LocalIP = "192.168.1.138";
//		udp.LocalPort = 8886;
//		udp.Broadcast = true;
//		dt.Connector = udp;
//		dt.Identity = new FC8800Identity("0000000000000000", FC8800Command.NULLPassword, E_ControllerType.FC8900);
//		Random rnd = new Random();
//		int max = 65535;
//		int min = 10000;
//		// 搜索时，不需要设定重发
//		dt.RestartCount = 0;
//		dt.Timeout = 5000;// 每隔5秒发送一次，所以这里设定5秒超时
//		// 网络标记就是一个随机数
//		SearchNetFlag = rnd.nextInt(max) % (max - min + 1) + min;// 网络标记
//		SearchTimes = 1;// 搜索次数;
//		SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
//		SearchEquptOnNetNum cmd = new SearchEquptOnNetNum(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			SearchEquptOnNetNum_Result result = (SearchEquptOnNetNum_Result) y;
//			x.append("第");
//			x.append(SearchTimes);
//			x.append("次搜索，本次搜索到控制器：");
//			x.append(result.SearchTotal);
//			x.append("个。");
//			if (result.SearchTotal > 0) {
//				PrintSearchDoor(result.ResultList, x);
//				// 将已搜索到的控制器设定网络标志，防止下次再搜索到
//				SetDoorNetFlag(result.ResultList);
//			}
//			if (SearchTimes < 4) {
//				SearchEquptOnNetNum_Parameter par1 = new SearchEquptOnNetNum_Parameter(dt, SearchNetFlag);
//				SearchEquptOnNetNum cmd1 = new SearchEquptOnNetNum(par1);
//				_Allocator.AddCommand(cmd1);
//			}
//			else {
//				x.append("*************搜索结束！*******************");
//				return;
//			}
//			SearchTimes++;
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butAutoSearchDoorActionPerformed
//	/**
//	 * 远程常开
//	 * @param evt
//	 */
//	void butHoldDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butHoldDoorActionPerformed
//		// 远程常开
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		HoldDoor cmd = new HoldDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butHoldDoorActionPerformed
//	void butUnlockDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butUnlockDoorActionPerformed
//		// 远程解除锁定
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		UnlockDoor cmd = new UnlockDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butUnlockDoorActionPerformed
//	void butLockDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butLockDoorActionPerformed
//		// 远程锁定
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		LockDoor cmd = new LockDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butLockDoorActionPerformed
//	/**
//	 * 远程关门
//	 * @param evt evt
//	 */
//	void butCloseDoorActionPerformed(java.awt.event.ActionEvent evt) {
//		// 远程关门
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		RemoteDoor_Parameter par = new RemoteDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		CloseDoor cmd = new CloseDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butCloseDoorActionPerformed
//	/**
//	 * 远程开门
//	 * @param evt
//	 */
//	void butOpenDoorActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butOpenDoorActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		dt.Timeout = 1000;
//		OpenDoor_Parameter par = new OpenDoor_Parameter(dt);
//		par.Door.SetDoor(1, 1);// 设定1号门执行操作
//		par.Door.SetDoor(2, 0);// 设定2号门不执行操作
//		OpenDoor cmd = new OpenDoor(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butOpenDoorActionPerformed
//	/**
//	 * 解除报警，可解除所有报警
//	 * @param evt
//	 */
//	void butCloseAlarmActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butCloseAlarmActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CloseAlarm_Parameter par = new CloseAlarm_Parameter(dt);
//		par.Alarm.Alarm = 65535;// 解除所有报警
//		CloseAlarm cmd = new CloseAlarm(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butCloseAlarmActionPerformed
//	void butCloseWatchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butCloseWatchActionPerformed
//		// 关闭数据监控
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		CloseWatch cmd = new CloseWatch(par);
//		_Allocator.CloseForciblyConnect(dt.Connector);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butCloseWatchActionPerformed
//	void butBeginWatchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butBeginWatchActionPerformed
//		// 打开数据监控
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		BeginWatch cmd = new BeginWatch(par);
//		_Allocator.OpenForciblyConnect(dt.Connector);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butBeginWatchActionPerformed
//	void butReadSNActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadSNActionPerformed
//		// TODO add your handling code here:
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadSN(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			x.append("控制器SN：");
//			ReadSN_Result result = (ReadSN_Result) y;
//			x.append(result.SN);
//			txtWriteSN.setText(result.SN);
//			txtSN.setText(result.SN);
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butReadSNActionPerformed
//	void butWriteSNActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteSNActionPerformed
//		String sSN = txtWriteSN.getText();
//		if (sSN.length() != 16) {
//			JOptionPane.showMessageDialog(null, "SN必须是16位！", "错误", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		WriteSN_Parameter par = new WriteSN_Parameter(dt, sSN);
//		INCommand cmd = new WriteSN(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			x.append("控制器SN：");
//			String sSN1 = txtWriteSN.getText();
//			x.append(sSN1);
//			txtSN.setText(sSN1);
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butWriteSNActionPerformed
//	void butReadVersionActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadVersionActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadVersion(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadVersion_Result result = (ReadVersion_Result) y;
//			x.append("控制板固件版本号：");
//			x.append(result.Version);
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butReadVersionActionPerformed
//	void ButWriteDeadlineActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButWriteDeadlineActionPerformed
//		CommandDetail dt = getCommandDetail();
//		WriteDeadline_Parameter par = new WriteDeadline_Parameter(dt, 1000);
//		INCommand cmd = new WriteDeadline(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_ButWriteDeadlineActionPerformed
//	void ButReadDeadlineActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButReadDeadlineActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadDeadline(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadDeadline_Result result = (ReadDeadline_Result) y;
//			if (result.Deadline == 0) {
//				x.append("控制板已失效！");
//			}
//			else if (result.Deadline == 65535) {
//				x.append("控制板永久有效！");
//			}
//			else {
//				x.append("控制板剩余有效天数：");
//				x.append(result.Deadline);
//				x.append("天.");
//			}
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butWriteTCPSettingActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteTCPSettingActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		if (RadUDP.isSelected()) {
//			dt.Timeout = 15000;// UDP延迟增加
//		}
//		if (mReadTCPDetail == null) {
//			JOptionPane.showMessageDialog(null, "请先读取TCP参数！", "错误", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		mReadTCPDetail.SetServerIP("192.168.1.2");
//		mReadTCPDetail.SetServerAddr("192.168.1.2");
//		WriteTCPSetting_Parameter par = new WriteTCPSetting_Parameter(dt, mReadTCPDetail);
//		INCommand cmd = new WriteTCPSetting(par);
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butWriteTCPSettingActionPerformed
//	void butReadTCPSettingActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadTCPSettingActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		if (RadUDP.isSelected()) {
//			// UDP延迟增加
//			dt.Timeout = 15000;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadTCPSetting(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			ReadTCPSetting_Result result = (ReadTCPSetting_Result) y;
//			TCPDetail tcp = result.TCP;
//			PirntTCPDetail(tcp, x);
//		});
//		_Allocator.AddCommand(cmd);
//	}
//	void butResetConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butResetConnectPasswordActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ResetConnectPassword(par);
//		_Allocator.AddCommand(cmd);
//	}
//	void butWriteConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butWriteConnectPasswordActionPerformed
//		// String pwd = txtPassword.getText();
//		String pwd = "12345678";
//		if (pwd.length() != 8) {
//			JOptionPane.showMessageDialog(null, "通讯密码必须是8位！", "错误", JOptionPane.ERROR_MESSAGE);
//			return;
//		}
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		WriteConnectPassword_Parameter par = new WriteConnectPassword_Parameter(dt, pwd);
//		INCommand cmd = new WriteConnectPassword(par);
//		_Allocator.AddCommand(cmd);
//	}
//	void butReadConnectPasswordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_butReadConnectPasswordActionPerformed
//		CommandDetail dt = getCommandDetail();
//		if (dt == null) {
//			return;
//		}
//		CommandParameter par = new CommandParameter(dt);
//		INCommand cmd = new ReadConnectPassword(par);
//		AddCommandResultCallback(cmd.getClass().getName(), (x, y) -> {
//			x.append("通讯密码：");
//			ReadConnectPassword_Result result = (ReadConnectPassword_Result) y;
//			x.append(result.Password);
//			txtPassword.setText(result.Password);
//		});
//		_Allocator.AddCommand(cmd);
//	}// GEN-LAST:event_butReadConnectPasswordActionPerformed
//	public void PirntTCPDetail(TCPDetail tcp, StringBuilder x) {
//		x.append("MAC：");
//		x.append(tcp.GetMAC());
//		x.append("IP：");
//		x.append(tcp.GetIP());
//		x.append(",子网掩码：");
//		x.append(tcp.GetIPMask());
//		x.append(",网关：");
//		x.append(tcp.GetIPGateway());
//		x.append("DNS1：");
//		x.append(tcp.GetDNS());
//		x.append(",DNS2：");
//		x.append(tcp.GetDNSBackup());
//		x.append("TCP端口：");
//		x.append(tcp.GetTCPPort());
//		x.append(",UDP端口：");
//		x.append(tcp.GetUDPPort());
//		x.append("服务器IP：");
//		x.append(tcp.GetServerIP());
//		x.append(",端口：");
//		x.append(tcp.GetServerPort());
//		x.append("服务器域名：");
//		x.append(tcp.GetServerAddr());
//	}
//	public void IniCardDataBase() {
//		cmbCardDataBaseType.removeAllItems();
//		cmbCardDataBaseType.addItem("排序卡区域");
//		cmbCardDataBaseType.addItem("非排序卡区域");
//		cmbCardDataBaseType.addItem("所有区域");
//		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//		String[] cols = "序号，卡号，密码，有效期，卡片状态，有效次数，特权，门1权限，门1时段，门2权限，门2时段，门3权限，门3时段，门4权限，门4时段，节假日限制".split("，");
//		// tblCard.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		tblCard.setColumnSelectionAllowed(true);
//		int[] colWidth = new int[]{40, 70, 60, 110, 60, 60, 70, 55, 55, 55, 55, 55, 55, 55, 55, 70};
//		for (String col : cols) {
//			tableModel.addColumn(col);
//		}
//		for (int i = 0; i < colWidth.length; i++) {
//			TableColumn Column = tblCard.getColumnModel().getColumn(i);
//			Column.setMinWidth(colWidth[i]);
//			Column.setMaxWidth(150);
//			Column.setPreferredWidth(colWidth[i]);
//		}
//		// 设置table内容居中
//		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
//		tcr.setHorizontalAlignment(SwingConstants.CENTER);
//		tblCard.setDefaultRenderer(Object.class, tcr);
//		// 设置table表头居中
//		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tblCard.getTableHeader().getDefaultRenderer();
//		hr.setHorizontalAlignment(SwingConstants.CENTER);
//		//
//		tblCard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//		// 不可整列移动
//		tblCard.getTableHeader().setReorderingAllowed(false);
//		// 不可拉动表格
//		tblCard.getTableHeader().setResizingAllowed(false);
//		tableModel.addRow(new Object[]{1, "0000000000", "00000000", "2017-11-14 12:30 ", " 黑名单 ", " 无限制 ", " 防盗设置卡 ", " 有 ", " 64 ", " 有 ", " 64 ", " 有 ", " 64 ", " 有 ", " 64 ", "无"});
//		tblCard.invalidate();
//		tblCard.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				tblCard_mouseClicked();
//			}
//		});
//	}
//	public void tblCard_mouseClicked() {
//		int numRow = tblCard.getSelectedRow();
//		javax.swing.table.TableModel model = tblCard.getModel();
//		long Card = Long.parseLong(model.getValueAt(numRow, 1).toString());
//		CardDetail cd = new CardDetail();
//		try {
//			cd.SetCardData(String.valueOf(Card));
//		}
//		catch (Exception e) {
//		}
//		int Index = mCardList.indexOf(cd);
//		if (Index == -1) {
//			return;
//		}
//		cd = mCardList.get(Index);
//		// 序号，卡号，密码，有效期，卡片状态，有效次数，特权，门1权限，门1时段，门2权限，门2时段，门3权限，门3时段，门4权限，门4时段，节假日限制
//		txtCardData.setText(model.getValueAt(numRow, 1).toString());
//		txtCardPassword.setText(model.getValueAt(numRow, 2).toString());
//		TxtCardExpiry.setText(model.getValueAt(numRow, 3).toString());
//		txtOpenTimes.setText(model.getValueAt(numRow, 5).toString());
//		cmbCardStatus.setSelectedIndex(cd.CardStatus);
//		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
//		for (int i = 1; i <= 4; i++) {
//			doors[i - 1].setSelected(cd.GetDoor(i));
//		}
//	}
//	public String WriteFile(String sFileName, StringBuilder strBuf, boolean append) {
//		File[] roots = File.listRoots();
//		String Path = "d:\\";
//		for (int i = 1; i < roots.length; i++) {
//			if (roots[i].getFreeSpace() > 0) {
//				Path = roots[i].getPath();
//				break;
//			}
//		}
//		Path += sFileName + ".txt";
//		try {
//			File file = new File(Path);
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			else {
//				if (!append) {
//					file.delete();
//					file.createNewFile();
//				}
//			}
//			FileOutputStream out = new FileOutputStream(file, true);
//			out.write(strBuf.toString().getBytes("utf-8"));
//			out.close();
//		}
//		catch (Exception e) {
//			return null;
//		}
//		return Path;
//	}
//	public String GetBooleanStr(boolean v) {
//		if (v) {
//			return "有";
//		}
//		else {
//			return "无";
//		}
//	}
//	public void ClearCardList() {
//		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//		tableModel.setRowCount(0);// 清除原有行
//	}
//	public void FillCardToList() {
//		if (mCardList == null) {
//			return;
//		}
//		if (mCardList.size() == 0) {
//			return;
//		}
//		DefaultTableModel tableModel = (DefaultTableModel) tblCard.getModel();
//		ClearCardList();
//		int Index = 1;
//		for (CardDetail cd : mCardList) {
//			Object[] row = CardDetailToRow(cd, Index);
//			// 添加数据到表格
//			tableModel.addRow(row);
//			Index += 1;
//		}
//		// 更新表格
//		tblCard.invalidate();
//	}
//	public Object[] CardDetailToRow(CardDetail cd, int Index) {
//		String OpenTimes;// 有效期
//		String Privilege = PrivilegeList[0];// "无";//特权
//		if (cd.OpenTimes == 65535) {
//			OpenTimes = OpenTimesUnlimited;// "无限制";
//		}
//		else {
//			OpenTimes = String.valueOf(cd.OpenTimes);
//		}
//		if (cd.IsPrivilege()) {
//			Privilege = PrivilegeList[1];// "首卡特权卡";
//		}
//		else if (cd.IsTiming()) {
//			Privilege = PrivilegeList[2];// "常开特权卡";
//		}
//		else if (cd.IsGuardTour()) {
//			Privilege = PrivilegeList[3];// "巡更卡";
//		}
//		else if (cd.IsAlarmSetting()) {
//			Privilege = PrivilegeList[4];// "防盗设置卡";
//		}
//		Object[] row = new Object[]{Index, cd.GetCardData(), cd.Password.replaceAll("F", ""), TimeUtil.FormatTimeHHmm(cd.Expiry), CardStatusList[cd.CardStatus], OpenTimes, Privilege, GetBooleanStr(cd.GetDoor(1)), cd.GetTimeGroup(1), GetBooleanStr(cd.GetDoor(2)), cd.GetTimeGroup(2), GetBooleanStr(cd.GetDoor(3)), cd.GetTimeGroup(3), GetBooleanStr(cd.GetDoor(4)), cd.GetTimeGroup(4), GetBooleanStr(cd.HolidayUse)};
//		return row;
//	}
//	public CardDetail GetCardDetail() {
//		String Card = txtCardData.getText();
//		String Password = txtCardPassword.getText();
//		String Expiry = TxtCardExpiry.getText();
//		String OpenTimes = txtOpenTimes.getText();
//		int CardStatus = cmbCardStatus.getSelectedIndex();
//		int iOpenTimes = 0;
//		Calendar CardExpiry = Calendar.getInstance();
//		long CardData = 0;
//		if (StringUtil.IsNullOrEmpty(Card)) {
//			JOptionPane.showMessageDialog(null, "卡号不能为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (!StringUtil.IsNum(Card) || Card.length() > 10) {
//			JOptionPane.showMessageDialog(null, "卡号为1-10位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		CardData = Long.parseLong(Card);
//		if (CardData > UInt32Util.UINT32_MAX || CardData == 0) {
//			JOptionPane.showMessageDialog(null, "卡号不能大于4294967295！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (!StringUtil.IsNullOrEmpty(Password)) {
//			if (Password.length() > 8 || Password.length() < 4 || !StringUtil.IsNum(Password)) {
//				JOptionPane.showMessageDialog(null, "密码为4-8位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//				return null;
//			}
//		}
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		try {
//			Date dt1 = df.parse(Expiry);
//			CardExpiry.setTime(dt1);
//			dt1 = CardExpiry.getTime();
//		}
//		catch (Exception exception) {
//			JOptionPane.showMessageDialog(null, "截止日期格式不正确（yyyy-MM-dd hh:mm）！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (OpenTimes.equals(OpenTimesUnlimited)) {
//			OpenTimes = "65535";
//		}
//		if (!StringUtil.IsNum(OpenTimes) || OpenTimes.length() > 5) {
//			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		iOpenTimes = Integer.parseInt(OpenTimes);
//		if (iOpenTimes > 65535) {
//			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
//		CardDetail cd = new CardDetail();// 设定卡号
//		try {
//			cd.SetCardData(Card);
//		}
//		catch (Exception e) {
//		}
//		cd.Password = Password;// 设定密码
//		cd.Expiry = CardExpiry;// 设定有效期
//		cd.OpenTimes = iOpenTimes;// 开门次数
//		cd.CardStatus = (byte) CardStatus;
//		// 设定4个门的开门时段
//		for (int i = 1; i <= 4; i++) {
//			cd.SetTimeGroup(i, 1);// 每个门都设定为1门
//			cd.SetDoor(i, doors[i - 1].isSelected());// 设定每个门权限
//		}
//		cd.SetNormal();// 设定卡片没有特权
//		cd.HolidayUse = true;// 设定受节假日限制。
//		return cd;
//	}
//	public Net.PC15.FC89H.Command.Data.CardDetail GetFC89HCardDetail() {
//		String Card = txtCardData.getText();
//		String Password = txtCardPassword.getText();
//		String Expiry = TxtCardExpiry.getText();
//		String OpenTimes = txtOpenTimes.getText();
//		int CardStatus = cmbCardStatus.getSelectedIndex();
//		int iOpenTimes = 0;
//		Calendar CardExpiry = Calendar.getInstance();
//		long CardData = 0;
//		if (StringUtil.IsNullOrEmpty(Card)) {
//			JOptionPane.showMessageDialog(null, "卡号不能为空！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (!StringUtil.IsNum(Card) || Card.length() > 10) {
//			JOptionPane.showMessageDialog(null, "卡号为1-10位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		CardData = Long.parseLong(Card);
//		if (CardData > UInt32Util.UINT32_MAX || CardData == 0) {
//			JOptionPane.showMessageDialog(null, "卡号不能大于4294967295！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (!StringUtil.IsNullOrEmpty(Password)) {
//			if (Password.length() > 8 || Password.length() < 4 || !StringUtil.IsNum(Password)) {
//				JOptionPane.showMessageDialog(null, "密码为4-8位数字！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//				return null;
//			}
//		}
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
//		try {
//			Date dt1 = df.parse(Expiry);
//			CardExpiry.setTime(dt1);
//			dt1 = CardExpiry.getTime();
//		}
//		catch (Exception exception) {
//			JOptionPane.showMessageDialog(null, "截止日期格式不正确（yyyy-MM-dd hh:mm）！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (OpenTimes.equals(OpenTimesUnlimited)) {
//			OpenTimes = "65535";
//		}
//		if (!StringUtil.IsNum(OpenTimes) || OpenTimes.length() > 5) {
//			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		iOpenTimes = Integer.parseInt(OpenTimes);
//		if (iOpenTimes > 65535) {
//			JOptionPane.showMessageDialog(null, "有效次数必须是数字，取值范围0-65535！", "卡片管理", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		JCheckBox[] doors = new JCheckBox[]{chkCardDoor1, chkCardDoor2, chkCardDoor3, chkCardDoor4};
//		Net.PC15.FC89H.Command.Data.CardDetail cd = new Net.PC15.FC89H.Command.Data.CardDetail();// 设定卡号
//		cd.Password = Password;// 设定密码
//		cd.Expiry = CardExpiry;// 设定有效期
//		cd.OpenTimes = iOpenTimes;// 开门次数
//		cd.CardStatus = (byte) CardStatus;
//		// 设定4个门的开门时段
//		for (int i = 1; i <= 4; i++) {
//			cd.SetTimeGroup(i, 1);// 每个门都设定为1门
//			cd.SetDoor(i, doors[i - 1].isSelected());// 设定每个门权限
//		}
//		cd.SetNormal();// 设定卡片没有特权
//		cd.HolidayUse = true;// 设定受节假日限制。
//		return cd;
//	}
//	/**
//	 * 打印记录详情
//	 * @param dt
//	 * @param buf
//	 */
//	public void PrintTransactionDatabaseDetail(String TransactionName, TransactionDetail dt, StringBuilder buf) {
//		buf.append(TransactionName);
//		buf.append("：容量：");
//		buf.append(dt.DataBaseMaxSize);
//		buf.append("：可读数量：");
//		buf.append(dt.readable());
//		buf.append("：写索引：");
//		buf.append(dt.WriteIndex);
//		buf.append("：读索引：");
//		buf.append(dt.ReadIndex);
//		buf.append("：循环存储：");
//		buf.append(dt.IsCircle);
//	}
//	/**
//	 * 打印记录日志
//	 * @param TransactionList ArrayList<AbstractTransaction>
//	 * @param log StringBuilder
//	 */
//	public void PrintTransactionDatabase(ArrayList<AbstractTransaction> TransactionList, StringBuilder log, String[] sTransactionList) {
//		int Quantity = TransactionList.size();
//		int code = 0;
//		String sCode = null;
//		for (int i = 0; i < Quantity; i++) {
//			AbstractTransaction Transaction = TransactionList.get(i);
//			log.append("序号：");
//			log.append(Transaction.SerialNumber);
//			if (!Transaction.IsNull()) {
//				code = Transaction.TransactionCode();
//				sCode = null;
//				log.append("，时间：");
//				log.append(TimeUtil.FormatTime(Transaction.TransactionDate()));
//				log.append("，事件：");// 类型
//				log.append(code);
//				if (code < 255) {
//					sCode = sTransactionList[code];
//					if (StringUtil.IsNullOrEmpty(sCode)) {
//						sCode = "未知类型";
//					}
//				}
//				else {
//					sCode = "未知类型";
//				}
//				log.append("--");
//				log.append(sCode);
//				if (Transaction instanceof AbstractDoorTransaction) {
//					AbstractDoorTransaction dt = (AbstractDoorTransaction) Transaction;
//					if (dt.Door != 255) {
//						log.append("，门号：");
//						log.append(dt.Door);
//					}
//				}
//				if (Transaction instanceof CardTransaction) {
//					CardTransaction cardTrn = (CardTransaction) Transaction;
//					log.append("，卡号：");
//					log.append(cardTrn.CardData);
//					log.append("，门号：");
//					log.append(cardTrn.DoorNum());
//					if (cardTrn.IsEnter()) {
//						log.append("，进门读卡");
//					}
//					else {
//						log.append("，出门读卡");
//					}
//				}
//			}
//		}
//	}
//	public void PrintSearchDoor(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst, StringBuilder log) {
//		ByteBuf tmpBuf = ByteUtil.ALLOCATOR.buffer(256, 300);
//		TCPDetail td = new TCPDetail();
//		for (SearchEquptOnNetNum_Result.SearchResult r : lst) {
//			log.append("SN：");
//			log.append(r.SN);
//			if (r.ResultData != null) {
//				log.append("，附带数据：");
//				log.append(ByteUtil.ByteToHex(r.ResultData));
//				if (r.ResultData.length == 0x89) {
//					tmpBuf.readerIndex(0);
//					tmpBuf.writerIndex(0);
//					tmpBuf.writeBytes(r.ResultData);
//					td.SetBytes(tmpBuf);
//					;
//					PirntTCPDetail(td, log);
//				}
//			}
//		}
//	}
//	/**
//	 * 设定控制器的网络标志
//	 * @param lst lst
//	 */
//	public void SetDoorNetFlag(ArrayList<SearchEquptOnNetNum_Result.SearchResult> lst) {
//		// ByteBuf tmpBuf = ByteUtil.ALLOCATOR.buffer(256, 300);
//		// TCPDetail td = new TCPDetail();
//		for (SearchEquptOnNetNum_Result.SearchResult r : lst) {
//			/*
//			 * if (r.ResultData != null) {
//			 *
//			 * if (r.ResultData.length == 0x89) { tmpBuf.readerIndex(0);
//			 * tmpBuf.writerIndex(0); tmpBuf.writeBytes(r.ResultData); td.SetBytes(tmpBuf);
//			 *
//			 *
//			 *
//			 *
//			 * } }
//			 */
//			CommandDetail detail = new CommandDetail();
//			UDPDetail udp = new UDPDetail("255.255.255.255", 8101);
//			udp.Broadcast = true;
//			detail.Connector = udp;
//			detail.Identity = new FC8800Identity(r.SN, FC8800Command.NULLPassword, E_ControllerType.FC8800);
//			SearchEquptOnNetNum_Parameter par = new SearchEquptOnNetNum_Parameter(detail, SearchNetFlag);
//			WriteEquptNetNum cmd = new WriteEquptNetNum(par);
//			_Allocator.AddCommand(cmd);
//		}
//	}
//	public CommandDetail getCommandDetail() {
//		CommandDetail detail = new CommandDetail();
//		String ip = "0", strPort = "0";
//		if (RadTCPClient.isSelected()) {
//			ip = txtTCPServerIP.getText();
//			strPort = txtLocalPort.getText();
//		}
//		if (RadUDP.isSelected()) {
//			ip = txtUDPRemoteIP.getText();
//			strPort = txtUDPRemotePort.getText();
//		}
//		int iPort = Integer.parseInt(strPort);
//		if (ip.length() == 0) {
//			JOptionPane.showMessageDialog(null, "必须输入IP地址！", "错误", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		if (iPort <= 0 || iPort > 65535) {
//			JOptionPane.showMessageDialog(null, "TCP端口取值范围：1-65535！", "错误", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		detail.Connector = new TCPClientDetail(ip, iPort);
//		if (RadUDP.isSelected()) {
//			UDPDetail udpd = new UDPDetail(ip, iPort);
//			udpd.Broadcast = true;
//			// udpd.LocalPort = 10088;//设定本地绑定端口号
//			detail.Connector = udpd;
//		}
//		String sn, pwd;
//		sn = txtSN.getText();
//		if (sn.length() != 16) {
//			JOptionPane.showMessageDialog(null, "SN必须是16位！", "错误", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		pwd = txtPassword.getText();
//		if (pwd.length() != 8) {
//			JOptionPane.showMessageDialog(null, "通讯密码必须是8位！", "错误", JOptionPane.ERROR_MESSAGE);
//			return null;
//		}
//		detail.Identity = new FC8800Identity(sn, pwd, E_ControllerType.FC8800);
//		return detail;
//	}
//	public void AddCommandResultCallback(String sCommand, CommandResultCallback callback) {
//		if (!CommandResult.containsKey(sCommand)) {
//			CommandResult.put(sCommand, callback);
//		}
//		/*
//		 * else { CommandResult.remove(sCommand); CommandResult.put(sCommand, callback);
//		 * }
//		 */
//	}
//	/**
//	 * 初始化监控消息处理的相关操作
//	 */
//	public void IniWatchEvent() {
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
//	/**
//	 * 记录事件的描述到StringBuilder中
//	 */
//	public void logTransaction(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf, String[] sTransactionList) {
//		AbstractTransaction Transaction = WatchTransaction.EventData;
//		// 打印消息类型
//		if (WatchTransaction.CmdIndex >= 1 && WatchTransaction.CmdIndex <= 6) {
//			strBuf.append(mWatchTypeNameList[WatchTransaction.CmdIndex]);// 类型
//		}
//		else if (WatchTransaction.CmdIndex == 0x23 && WatchTransaction.CmdIndex == 0x22) {
//			int tmpType = WatchTransaction.CmdIndex - 27;
//			strBuf.append(mWatchTypeNameList[tmpType]);// 类型
//		}
//		else {
//			strBuf.append("未知消息类型：");// 类型
//			strBuf.append(WatchTransaction.CmdIndex);
//		}
//		strBuf.append("，消息时间：");
//		strBuf.append(TimeUtil.FormatTime(Transaction.TransactionDate()));
//		if (sTransactionList == null) {
//			return;
//		}
//		int code = Transaction.TransactionCode();
//		String sCode = null;
//		strBuf.append("消息代码：");// 类型
//		strBuf.append(code);
//		if (code < 255) {
//			sCode = sTransactionList[code];
//			if (StringUtil.IsNullOrEmpty(sCode)) {
//				sCode = "未知类型";
//			}
//		}
//		else {
//			sCode = "未知类型";
//		}
//		strBuf.append("--");
//		strBuf.append(sCode);
//		if (Transaction instanceof AbstractDoorTransaction) {
//			AbstractDoorTransaction dt = (AbstractDoorTransaction) Transaction;
//			if (dt.Door != 255) {
//				strBuf.append("，门号：");
//				strBuf.append(dt.Door);
//			}
//		}
//		if (Transaction instanceof CardTransaction) {
//			CardTransaction cardTrn = (CardTransaction) WatchTransaction.EventData;
//			strBuf.append("卡号：");
//			strBuf.append(cardTrn.CardData);
//			strBuf.append("，门号：");
//			strBuf.append(cardTrn.DoorNum());
//			if (cardTrn.IsEnter()) {
//				strBuf.append("，进门读卡");
//			}
//			else {
//				strBuf.append("，出门读卡");
//			}
//		}
//	}
//	/**
//	 * 打印监控消息
//	 * @param WatchTransaction 监控消息
//	 * @param strBuf 日志缓冲区
//	 */
//	public void PrintWatchEvent(FC8800WatchTransaction WatchTransaction, StringBuilder strBuf) {
//		switch (WatchTransaction.CmdIndex) {
//			case 1:// 读卡信息
//				logTransaction(WatchTransaction, strBuf, mCardTransactionList);
//				break;
//			case 2:// 出门开关信息
//				logTransaction(WatchTransaction, strBuf, mButtonTransactionList);
//				break;
//			case 3:// 门磁信息
//				logTransaction(WatchTransaction, strBuf, mDoorSensorTransactionList);
//				break;
//			case 4:// 远程开门信息
//				logTransaction(WatchTransaction, strBuf, mSoftwareTransactionList);
//				break;
//			case 5:// 报警信息
//				logTransaction(WatchTransaction, strBuf, mAlarmTransactionList);
//				break;
//			case 6:// 系统信息
//				logTransaction(WatchTransaction, strBuf, mSystemTransactionList);
//				break;
//			default:
//				logTransaction(WatchTransaction, strBuf, null);
//		}
//	}
//	public void GetCommandDetail(StringBuilder strBuf, INCommand cmd) {
//		if (cmd == null) {
//			strBuf.append("命令类型：null");
//			return;
//		}
//		strBuf.append("命令类型：");
//		strBuf.append(GetCommandName(cmd));
//		if (cmd.getCommandParameter() != null) {
//			strBuf.append("，SN:");
//			CommandDetail det = cmd.getCommandParameter().getCommandDetail();
//			strBuf.append(det.Identity.GetIdentity());
//			strBuf.append(",");
//			GetConnectorDetail(strBuf, det.Connector);
//		}
//	}
//	@Contract("null -> !null")
//	public String GetCommandName(INCommand cmd) {
//		if (cmd == null) {
//			return "null";
//		}
//		String sKey = cmd.getClass().getName();
//		return FormUtils.CommandName.getOrDefault(sKey, sKey);
//	}
//	public void GetConnectorDetail(StringBuilder strBuf, ConnectorDetail conn) {
//		if (conn == null) {
//			strBuf.append("ConnectorDetail：null");
//			return;
//		}
//		if (conn instanceof TCPClientDetail) {
//			TCPClientDetail tcp = (TCPClientDetail) conn;
//			strBuf.append("TCP远程设备:");
//			strBuf.append(tcp.IP);
//			strBuf.append(":");
//			strBuf.append(tcp.Port);
//		}
//		else if (conn instanceof UDPDetail) {
//			UDPDetail udp = (UDPDetail) conn;
//			strBuf.append("UDP远程设备:");
//			strBuf.append(udp.IP);
//			strBuf.append(":");
//			strBuf.append(udp.Port);
//		}
//		// strBuf.append(",");
//	}
}
