package cn.keyshare.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

@SuppressLint("NewApi")
public class DeviceIDUtil {

	public  static String getDeviceId(Context context) {
		String id = Build.SERIAL;
		if (id == null) {
			id = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);
		}
		if (id == null) {
			TelephonyManager telephonyManager;

			telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

			/*
			 * getDeviceId() function Returns the unique device ID. for
			 * example,the IMEI for GSM and the MEID or ESN for CDMA phones.
			 */
			id = telephonyManager.getDeviceId();
		}
		return id;
	}

}
