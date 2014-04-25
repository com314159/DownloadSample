package cn.keyshare.download.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import cn.keyshare.download.core.Downloads;
import cn.keyshare.utils.StringUtil;


public class CaculateDownloadSpeedUtil {

	private static CaculateDownloadSpeedUtil mInstance;

	private Map<Long, SpeedInfo> mDownloadSpeedInfos = new HashMap<Long, SpeedInfo>();
	private Set<Long> mNoLongerCaculateSpeed;

	private final double NANOS_PER_SECOND = 1000000000.0;

//	private final long mCalculateAverageSpeedTime = (long) (10 * NANOS_PER_SECOND); // 计算平均速度的周期

	private CaculateDownloadSpeedUtil() {
	}

	public static CaculateDownloadSpeedUtil getInstance() {
		if (mInstance == null) {
			synchronized (CaculateDownloadSpeedUtil.class) {
				if (mInstance == null) {
					mInstance = new CaculateDownloadSpeedUtil();
				}
			}
		}
		return mInstance;
	}

	// 把不需要计算速度的下载移除
	public void startCaculate() {
		mNoLongerCaculateSpeed = new HashSet<Long>(mDownloadSpeedInfos.keySet());
	}

	public void endCaculate() {
		for (Long id : mNoLongerCaculateSpeed) {
			mDownloadSpeedInfos.remove(id);
			// Log.i("gzc", "计算速度中移除 id " + id);
		}
	}

	// totalBytes == -1 表示还没开始下载
	public String caculateSpeed(Context context, long id, long currentBytes,
			long totalBytes, int status) {

		long speedByte = 0;
		// Log.i("gzc", "开始计算速度"+ id + " 现在byte" + currentBytes + " 总的byte" +
		// totalBytes + " 状态为" + status);
		if ((currentBytes >= 0 && currentBytes < totalBytes || totalBytes == -1)
				&& (status == Downloads.STATUS_RUNNING
						|| status == Downloads.STATUS_WAITING || status == Downloads.STATUS_PENDING)) {

			mNoLongerCaculateSpeed.remove(id);

			long currentTime = System.nanoTime();

			SpeedInfo info = mDownloadSpeedInfos.get(id);
			if (info == null) {
//				 Log.i("gzc", "计算速度" + id + "加入" + new SpeedInfo(currentTime,
//				 currentBytes) );
				mDownloadSpeedInfos.put(id, new SpeedInfo(currentTime,
						currentBytes));
			} else {
				long timeNanos = 0;
//				boolean notUpdateTime = false;
				if (status == Downloads.STATUS_RUNNING) {
					long downloadByte = currentBytes - info.mLastDownloadByte;
					timeNanos = currentTime - info.mLastTime;
//					 Log.i("gzc", "计算速度" + id + " 得到了上一次的  " + info +
//					 " 现在的时间为" + currentTime + " 现在的下载量为" + currentBytes +
//					 " 时间差为" + timeNanos + " 下载量差为" + downloadByte);
					 
					 speedByte = caculateSpeed(downloadByte, timeNanos);
					 
					 //出现小于0的情况,说明数据库虽然记录了上一次下载了n,但是实际文件只写入了n-a,现在是以文件为准从n-a开始下载,所以要重新计算速度
					 if(downloadByte<0){
						 info.mLastDownloadByte = currentBytes;
						 info.mLastTime = currentTime;
						 speedByte = 0;
					 }
					 
					
					if (speedByte == 0) {
						speedByte = info.mLastSpeed;
//						notUpdateTime = true;
					}
					
					if(info.mLastSpeed == 0){
						//第一次测速误差非常大,为了减小误差,除以10
						speedByte /= 10;
					}
					
					if (info.mLastDownloadByte == currentBytes) {
//						notUpdateTime = true;
					}
				}
//				if (timeNanos > mCalculateAverageSpeedTime) {
//					info.mLastDownloadByte = currentBytes;
//					if (!notUpdateTime) {
//						info.mLastTime = currentTime;
//					}
//				}

				info.mLastSpeed = speedByte;
			}
		}

		String speed = StringUtil.getSizeText(context, speedByte);
		if (speed == null || speed.equals("")) {
			speed = "0 KB";
		}
//		 Log.i("gzc", " 得到的速度为" + speed + " id = " + id);
		return speed + "/s";
	}

	private long caculateSpeed(long downloadByte, long timeNano) {
		if (timeNano == 0) {
			return 0;
		}
		double timeSeconds = timeNano / NANOS_PER_SECOND;
		return (long) (downloadByte / timeSeconds);
	}

	private class SpeedInfo {

		public SpeedInfo(long lastTime, long lastDownloadByte) {
			mLastTime = lastTime;
			mLastDownloadByte = lastDownloadByte;
		}

		long mLastTime;
		long mLastDownloadByte;
		long mLastSpeed;

		@Override
		public String toString() {
			return "lastTime" + mLastTime + " lastSpeed" + mLastSpeed
					+ " lastDownloadByte" + mLastDownloadByte;
		}
	}
}
