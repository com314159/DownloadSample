package cn.keyshare.download.core;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DownloadDataEngine {

	private ReadWriteLock mReadWriteLock = new ReentrantReadWriteLock();

	private Condition mInitCondition = mReadWriteLock.writeLock()
			.newCondition();

	private boolean mInited = false;

	private static volatile DownloadDataEngine mInstance;

	public Map<String, Boolean> mFileNameInUseMap;

	private DownloadDataEngine() {
	}

	public static DownloadDataEngine getInstance() {
		if (mInstance == null) {
			synchronized (DownloadDataEngine.class) {
				if (mInstance == null) {
					mInstance = new DownloadDataEngine();
				}
			}
		}
		return mInstance;
	}

	public void setDownloadData(Map<Long, DownloadInfo> downloads) {
		initFileNameInUse(downloads);
	}

	private void initFileNameInUse(Map<Long, DownloadInfo> downloads) {

		mReadWriteLock.writeLock().lock();
		try {
			mFileNameInUseMap = new ConcurrentHashMap<String, Boolean>();

			Iterator<?> iter = downloads.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
				DownloadInfo info = (DownloadInfo) entry.getValue();

				if (!Downloads.isStatusCompleted(info.mStatus)
						&& info.mFileName != null) {
					mFileNameInUseMap.put(info.mFileName, true);
				}
			}

			if (!mInited) {
				mInited = true;
				mInitCondition.signalAll();
			}

		} finally {
			mReadWriteLock.writeLock().unlock();
		}

	}

	// 这里其实是生产者消费者模式，不过此处，必须等生产者生产完成，否则消费者拿不到数据

	public boolean isFileNameInUse(String fileName) {
		if (!mInited) {
			mReadWriteLock.writeLock().lock();
			try {
				while (!mInited)
					mInitCondition.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				mReadWriteLock.writeLock().unlock();
			}
		}

		mReadWriteLock.readLock().lock();
		
		try {
			if (mFileNameInUseMap.get(fileName) != null) {
				return true;
			}
		} finally {
			mReadWriteLock.readLock().unlock();
		}
		return false;
	}

}
