package cn.keyshare.utils;

import java.util.Locale;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class SystemUtil {
	
	@SuppressWarnings("unused")
	private static final String TAG = "SystemUtil";
	
    // 获取系统语言
    public static String getSystemLanguage(Context context) {
    	if(null == context) {
    		return null;
    	}
    	
    	Locale locale = null;
    	try {
    		locale = context.getResources().getConfiguration().locale;
    	} catch (Exception e) {
    		return null;
    	}
    	
    	if(null != locale) {
    		return locale.getLanguage();
    	}
    	return null;
    }
    
    // 检查网络状态
  	public static boolean isNetworkAvailable(Context c, boolean wifiOnly) { 
  		if(c == null) {
  			return false;
  		}
  		
  	    Context context = c.getApplicationContext();  
  	    ConnectivityManager connectivity = (ConnectivityManager)context
  	    		.getSystemService(Context.CONNECTIVITY_SERVICE);  
  	    
  	    if (connectivity != null) {
  	    	
  	        NetworkInfo[] info = connectivity.getAllNetworkInfo();  
  	        
  	        if (info != null) {
  	        	
  	            for (int i = 0; i < info.length; i++) {  
  	                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
  	                	if(!wifiOnly) {
  	                		info = null;
  		                    return true;
  		                    
  	                	} else if (info[i].getType() == ConnectivityManager
  	                			.TYPE_WIFI) {
  	                		info = null;
  	                		return true;
  	                	}
  	                }  
  	            }
  	            
  	        }   
  	    }
  	    return false;  
  	}
  	
  	// 判断是否为系统应用
  	public static boolean isSystemApp(PackageInfo pInfo) {  
        return ((pInfo.applicationInfo.flags 
        		& ApplicationInfo.FLAG_SYSTEM) != 0);  
    }  
  	
  	// 判断是否为系统升级应用
    public static boolean isSystemUpdateApp(PackageInfo pInfo) {  
        return ((pInfo.applicationInfo.flags 
        		& ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);  
    } 
  	
  	// 根据Wifi信息获取本地Mac
    public static String getLocalMacAddressFromWifiInfo(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);  
        if(null == wifi) {
        	return new String("");
        }
        WifiInfo info = wifi.getConnectionInfo(); 
        if(null == info) {
        	return new String("");
        }
        return info.getMacAddress();
    }
    
    // 获取IP地址
    public static String getLocalIpAddress(Context context) {
    	
    	String ip = new String("null");
    	
    	if(null == context) {
    		return ip;
    	}
    	
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	if(null == wifiManager) {
    		return ip;
    	}

        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
        	wifiManager.setWifiEnabled(true);
        }
        
        WifiInfo wifiInfo = wifiManager.getConnectionInfo(); 
        if(null == wifiInfo) {
        	return ip;
        }
        
        int ipAddress = wifiInfo.getIpAddress(); 
        ip = intToIp(ipAddress);
        
        return ip;
    }
    
    public static String intToIp(int ipAddress) {
        return (ipAddress & 0xFF ) + "." +     

          	((ipAddress >> 8 ) & 0xFF) + "." +     

          	((ipAddress >> 16 ) & 0xFF) + "." +     

          	(ipAddress >> 24 & 0xFF) ;
    }
}
