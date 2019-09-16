package fcardutils.stringutil;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
/**
 * @author Administrator
 */
public class LogUtil {
	public final static String T = "	";
	public final static String S = " ";
	static Map<Integer, Integer> map = new HashMap<>();
	// static {
	// Calendar calendar = Calendar.getInstance();
	// map.put(0, calendar.get(Calendar.YEAR));// 年
	// map.put(1, calendar.get(Calendar.MONTH) + 1);// 月
	// map.put(2, calendar.get(Calendar.DAY_OF_MONTH));// 日
	// map.put(3, calendar.get(Calendar.HOUR_OF_DAY));// 时
	// map.put(4, calendar.get(Calendar.MINUTE));// 分
	// map.put(5, calendar.get(Calendar.SECOND));// 秒
	// map.put(6, calendar.get(Calendar.DAY_OF_WEEK));// 周几 0~6
	// map.put(7, calendar.get(Calendar.DAY_OF_YEAR));// 本年第N天
	// map.put(8, calendar.get(Calendar.DAY_OF_MONTH));// 本月第N天
	// }
	//获取年月日时分秒等
	public static Integer getT(int t) {
		Calendar calendar = Calendar.getInstance();
		map.put(0, calendar.get(Calendar.YEAR));// 年
		map.put(1, calendar.get(Calendar.MONTH) + 1);// 月
		map.put(2, calendar.get(Calendar.DAY_OF_MONTH));// 日
		map.put(3, calendar.get(Calendar.HOUR_OF_DAY));// 时
		map.put(4, calendar.get(Calendar.MINUTE));// 分
		map.put(5, calendar.get(Calendar.SECOND));// 秒
		map.put(6, calendar.get(Calendar.DAY_OF_WEEK));// 周几  0~6
		map.put(7, calendar.get(Calendar.DAY_OF_YEAR));// 本年第N天
		map.put(8, calendar.get(Calendar.DAY_OF_MONTH));// 本月第N天
		int i = -1;
		if (map.containsKey(t)) {
			i = map.get(t);
		}
		return i;
	}
	public static String Bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}
}
