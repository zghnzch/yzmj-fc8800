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
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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
}
