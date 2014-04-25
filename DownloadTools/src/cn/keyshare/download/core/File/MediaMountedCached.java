package cn.keyshare.download.core.File;

import android.os.Environment;
import cn.keyshare.utils.SdCardUtils;

/**
 * 这个类是为了防止检测sd卡是否可写过于频繁导致性能下降所做的缓存
 * 
 * @author ZhiCheng
 * 
 */
public class MediaMountedCached {

	private static MediaMountedCached mInstance;

	private final int mCheckInterval = 1000 * 10;// 秒检测一次

//	private long mLastCheckTimeFirstSdCard = -1;
	private long mLastCheckTimeSecondSdCard = -1;

//	private boolean mLastCheckValueFirstSdCard = false;
	private boolean mLastCheckValueSecondSdCard = false;

	public static MediaMountedCached getInstance() {
		if (mInstance == null) {
			synchronized (MediaMountedCached.class) {
				if (mInstance == null) {
					mInstance = new MediaMountedCached();
				}
			}
		}
		return mInstance;
	}

	// 判断sd卡是否可用了, fileName == null 表示 判断两个sd卡, 否则判断fileName对应的那个sd卡,
	// 当内置sd卡卸载时,外置sd卡一定卸载
	// 为了防止频繁检测导致性能下降，这个函数返回的是缓存SD卡的状态,这个状态不能保证准确
	//因为内置sd卡拔出时，不会导致应用退出，只有外置sd卡拔出时才会，所以只需要检测外置sd卡
	public boolean isSecondExternalMediaMountedCache(String fileName) {

		if (fileName == null
				|| fileName.startsWith(Environment
						.getExternalStorageDirectory().getPath())) {
			//不需要检测内置
			return true;

		} else {
			synchronized (MediaMountedCached.class) {
				
				if(checkCached(mLastCheckTimeSecondSdCard)){
					return mLastCheckValueSecondSdCard;
				}
				
				mLastCheckTimeSecondSdCard = System.currentTimeMillis();
				
				
				if(SdCardUtils.isSecondSDcardMounted()){
					mLastCheckValueSecondSdCard = true;
					return true;
				}else{
					mLastCheckValueSecondSdCard = false;
					return false;
				}
			}
		}

	}
	
	private boolean checkCached(long lastTime){
		long currentTime = System.currentTimeMillis();
		if(lastTime != -1&&(currentTime - lastTime)<mCheckInterval){
			return true;
		}
		return false;
	}

}
