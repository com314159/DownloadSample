package cn.keyshare.utils.android;

public class Log {
	private static boolean DEBUG_FLAG = true;
	
	public static void i(String tag, String msg) {
		if(!DEBUG_FLAG || null == tag || null == msg) {
			return ;
		}
		android.util.Log.i(tag, msg);
	}
	
	public static void w(String tag,String msg){
		if(null == tag || null == msg) {
			return ;
		}
		android.util.Log.w(tag, msg);
	}
	
	public static void e(String tag,String msg){
		if(null == tag || null == msg) {
			return ;
		}
		android.util.Log.e(tag, msg);
	}
	
	public static void setOutPutLog(boolean b){
		DEBUG_FLAG = b;
	}
	
}
