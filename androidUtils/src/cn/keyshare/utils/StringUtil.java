package cn.keyshare.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

public class StringUtil {
	
	// 当字符串为null时，返回"null"
	public static String getNotNullString(Object obj) {
		return (null == obj) ? "null" : obj.toString(); 
	}
	
	
	// 把bytes转为适合显示的单位,如 1mb等
	public static String getSizeText(Context context, long totalBytes) {
	String sizeText = "";
	if (totalBytes >= 0) {
	    sizeText = formatSize(context, totalBytes,false);
	}
	return sizeText;
    }
	
	private static String formatSize(Context context,long number,boolean shorter){
	    if (context == null) {
        return "";
    }

    float result = number;
    int suffix = R.string.keyshare_utils_byteShort;
    if (result > 900) {
        suffix = R.string.keyshare_utils_kilobyteShort;
        result = result / 1024;
    }
    if (result > 900) {
        suffix = R.string.keyshare_utils_megabyteShort;
        result = result / 1024;
    }
    if (result > 900) {
        suffix = R.string.keyshare_utils_gigabyteShort;
        result = result / 1024;
    }
    if (result > 900) {
        suffix = R.string.keyshare_utils_terabyteShort;
        result = result / 1024;
    }
    if (result > 900) {
        suffix = R.string.keyshare_utils_petabyteShort;
        result = result / 1024;
    }
    String value;
    if (result < 1) {
        value = String.format(Locale.US,"%.2f", result);
    } else if (result < 10) {
        if (shorter) {
            value = String.format(Locale.US,"%.1f", result);
        } else {
            value = String.format(Locale.US,"%.2f", result);
        }
    } else if (result < 100) {
        if (shorter) {
            value = String.format(Locale.US,"%.0f", result);
        } else {
            value = String.format(Locale.US,"%.2f", result);
        }
    } else {
        value = String.format(Locale.US,"%.0f", result);
    }
    return context.getResources().
        getString(R.string.keyshare_utils_fileSizeSuffix,
                  value, context.getString(suffix));
}
	
	
	public static String joinStrings(String joiner, Iterable<String> parts) {
	    StringBuilder builder = new StringBuilder();
	    boolean first = true;
	    for (String part : parts) {
		if (!first) {
		    builder.append(joiner);
		}
		builder.append(part);
		first = false;
	    }
	    return builder.toString();
	}
	
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }
	
    // 检查邮箱格式
    public static boolean isEmailFormat(String email) { 
        boolean tag = true; 
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"; 
        final Pattern pattern = Pattern.compile(pattern1); 
        final Matcher mat = pattern.matcher(email); 
        if (!mat.find()) { 
            tag = false; 
        } 
        return tag; 
    }
    
    //得到下载次数
	public static String getDownloadNumberString(long number){
		if(number<10000){
			return String.valueOf(number);
		}else if(number>=10000&&number<=100000000){
			return String.valueOf(number/10000) + "万+";
		}else {
			return String.valueOf(number/100000000) + "亿+";
		}
	}
	
	public static String getTailorString(String string,int length){
		
		if(string == null){
			return null;
		}
		
		if(string.length()<=length)
			return string;
		
		StringBuilder sb = new StringBuilder(string.substring(0, length-1));
		sb.append(".");
		sb.append(".");
		sb.append(".");
		return sb.toString();
	}
	
	public static String ArrayToString(Object[] ary) {
		
		if(ary == null){
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < ary.length; ++i) {
			sb.append(ary[i].toString());
			sb.append(",");
		}
		sb.append("}");
		return sb.toString();
	}

}
