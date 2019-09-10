/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcardiodemo;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
/**
 * 窗体启动类
 * @author 赖金杰
 */
public class BootstrapMain {
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					//设置本属性将改变窗口边框样式定义
					BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.osLookAndFeelDecorated;
					org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
					// org.jb2011.lnf.beautyeye.BeautyEyeLookAndFeelWin.setMnemonicHidden(true);
					// 设置此开关量为false即表⽰示关闭之，BeautyEye LNF中默认是true
					//					BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
					//					UIManager.put("RootPane.setupButtonVisible", true);
					//					BeautyEyeLNFHelper.commonBackgroundColor.darker();
					new frmMain().setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
