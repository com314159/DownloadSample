package com.example.androiddownloadtest;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import cn.keyshare.download.core.DownloadManager;
import cn.keyshare.download.core.DownloadManager.Request;
import cn.keyshare.download.core.Downloads;
import cn.keyshare.utils.ToastUtil;

public class DownloadUtil {
	private static DownloadUtil mInstance = null;

	private DownloadManager mDownloadManager;

	private static final String[] DELETE_COLUMNS = new String[] {
			Downloads._ID, Downloads.COLUMN_STATUS, Downloads._DATA };

	private static final String[] GETFILE_COLUMNS = new String[] { Downloads._DATA };

	private static final String[] PayId_COLUMNS = new String[] { Downloads.COLUMN_APP_DATA };// 这一列现在存的是付费的ID

	final private int mPayId_ColumnId = 0;

	final private int mID_ColumnId = 0;

	final private int mGETFILE_File_ColumnId = 0;

	final private int mStatusColumnId = 1;
	final private int mDataColumnId = 2;

	@SuppressWarnings("unused")
	final private int mMimeTypeColumnId = 1;

	private Context mContext;

	private DownloadUtil() {

	}

	private void init(Context context) {
		mDownloadManager = new DownloadManager(context.getApplicationContext()
				.getContentResolver(), context.getApplicationContext()
				.getPackageName());

		mContext = context.getApplicationContext();
	}

	public static DownloadUtil getInstance(Context context) {
		if (mInstance == null) {
			synchronized (DownloadUtil.class) {
				if (mInstance == null) {
					mInstance = new DownloadUtil();
					mInstance.init(context);
				}
			}
		}
		return mInstance;
	}

	// 发起一个下载,返回一个下载的id
	public long startDownload(String downloadUrl, String name, String path,String fileName) {

		Uri srcUri = Uri.parse(downloadUrl);

		if (srcUri == null) {
			Log.e("gzc", " 下载的url为空" + name);
		}
		String scheme = srcUri.getScheme();
		if (scheme == null || !scheme.equals("http")) {

			String msg = "Can only download HTTP URIs: " + srcUri + name;

			Log.e("gzc", msg);

			ToastUtil.showToastCancelShowing(mContext,
					"下载url错误" + srcUri.toString(), Toast.LENGTH_LONG);
			return -1;
		}

		DownloadManager.Request request = new Request(srcUri);
		request.setDestinationPath(path, fileName);
		request.setTitle(name);
		request.setShowRunningNotification(Downloads.VISIBILITY_VISIBLE);
		long id = mDownloadManager.enqueue(request);

		return id;
	}

	public DownloadManager getDownloadManager() {
		return mDownloadManager;
	}

	// 把付费的ID写入数据库
	public void savePayId(Context context, long id, String payId) {

		ContentValues values = new ContentValues();
		values.put(Downloads.COLUMN_APP_DATA, payId);
		context.getContentResolver().update(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, values,
				Downloads._ID + " = ? ", new String[] { Long.toString(id) });
	}

	public String getPayId(Context context, long id) {
		Uri uri = ContentUris.withAppendedId(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, id);
		Cursor cursor = context.getContentResolver().query(uri, PayId_COLUMNS,
				Downloads.COLUMN_DELETED + " != '1'", null, null);

		if(cursor == null){
			return null;
		}
		
		if (!cursor.moveToFirst()) {
			return null;
		}
		String payId = cursor.getString(mPayId_ColumnId);

		if (payId != null)
			Log.i("gzc", "得到的ID 为 " + payId + " id" + id);
		else {
			Log.i("gzc", "得到的ID 为 空"+ " id" + id);
		}

		return payId;
	}

	public String getFileName(Context context, long id) {
		Uri uri = ContentUris.withAppendedId(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, id);
		Cursor cursor = context.getContentResolver().query(uri,
				GETFILE_COLUMNS, Downloads.COLUMN_DELETED + " != '1'", null,
				null);

		if(cursor == null){
			return null;
		}
		
		if (!cursor.moveToFirst()) {
			return null;
		}
		String filename = cursor.getString(mGETFILE_File_ColumnId);
		return filename;
	}

	public long getTotalBytes(Context context, long id){
		
		String[] COLUMNS = new String[] { Downloads.COLUMN_TOTAL_BYTES };
		
		Uri uri = ContentUris.withAppendedId(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, id);
		Cursor cursor = context.getContentResolver().query(uri,
				COLUMNS, Downloads.COLUMN_DELETED + " != '1'", null,
				null);

		if(cursor == null){
			return -1;
		}
		
		if (!cursor.moveToFirst()) {
			return -1;
		}
		return cursor.getLong(0);		
	}
	
	public String getDownloadUrl(Context context, long id) {
		
		String[] COLUMNS = new String[] { Downloads.COLUMN_URI };
		
		Uri uri = ContentUris.withAppendedId(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, id);
		Cursor cursor = context.getContentResolver().query(uri,
				COLUMNS, Downloads.COLUMN_DELETED + " != '1'", null,
				null);

		if(cursor == null){
			return null;
		}
		
		if (!cursor.moveToFirst()) {
			return null;
		}
		String filename = cursor.getString(0);
		return filename;
	}
	
	public int getDownloadStatus(Context context,long id){
		String[] COLUMNS = new String[] { Downloads.COLUMN_STATUS };
		Uri uri = ContentUris.withAppendedId(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, id);
		
		Cursor cursor = context.getContentResolver().query(uri,
				COLUMNS, Downloads.COLUMN_DELETED + " != '1'", null,
				null);
		
		if(cursor == null){
			return -1;
		}
		
		if (!cursor.moveToFirst()) {
			return -1;
		}
		
		return cursor.getInt(0);
		
	}
	
	public void renameFileName(Context context,long id,String newFileName){
		ContentValues values = new ContentValues();
		values.put(Downloads._DATA, newFileName);
		context.getContentResolver().update(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, values,
				Downloads._ID + " = ? ", new String[] { Long.toString(id) });		
	}
	
	// 删除数据库记录,如果有文件,会稍后删除文件. 如果数据表格被删除,无法删除文件,所以必须自己删除一遍,其他删除函数类似
	public void deleteDownloadByFileName(Context context, String fileName) {

		String selection = Downloads._DATA + " like '%" + fileName + "'";
		Cursor cursor = context.getContentResolver().query(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, DELETE_COLUMNS, selection,
				null, null);

		if(cursor == null){
			return;
		}
		
		if (!cursor.moveToFirst()) {
			return;
		}

		if (cursor.getCount() > 1) {
			throw new IllegalArgumentException(
					" the fileName is not unique, it will delete more than one file");
		}

		long id = cursor.getLong(mID_ColumnId);
		int status = cursor.getInt(mStatusColumnId);
		String filename = cursor.getString(mDataColumnId);

		boolean isComplete = Downloads.isStatusCompleted(status);

		if (isComplete && filename != null) {
			String path = Uri.parse(filename).getPath();
			if (path.startsWith(Environment.getExternalStorageDirectory()
					.getPath())) {
				mDownloadManager.markRowDeleted(id);
				return;
			}
		}
		mDownloadManager.remove(id);

	}

	
	public long getDownloadId(Context context, String DownloadUrl){
		
		String[] COLUMNS = new String[] { Downloads._ID };
		
		Cursor cursor = context.getContentResolver().query(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, COLUMNS,
				Downloads.COLUMN_URI + " = '" + DownloadUrl + "'", null, null);

		if(cursor == null){
			return -1;
		}
		
		if (!cursor.moveToFirst()) {
			return -1;
		}
		
		
		return cursor.getLong(0);
	}
	
	
	// 删除数据库记录,如果有文件,会稍后删除文件. 如果数据表格被删除,无法删除文件,所以必须自己删除一遍,其他删除函数类似
	public void deleteDownload(Context context, String Downloadurl) {
		Cursor cursor = context.getContentResolver().query(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, DELETE_COLUMNS,
				Downloads.COLUMN_URI + " = '" + Downloadurl + "'", null, null);

		if(cursor == null){
			return;
		}
		
		if (!cursor.moveToFirst()) {
			return;
		}

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			long id = cursor.getLong(mID_ColumnId);
			int status = cursor.getInt(mStatusColumnId);
			String filename = cursor.getString(mDataColumnId);

			boolean isComplete = Downloads.isStatusCompleted(status);

			if (isComplete && filename != null) {
				String path = Uri.parse(filename).getPath();
				if (path.startsWith(Environment.getExternalStorageDirectory()
						.getPath())) {
					mDownloadManager.markRowDeleted(id);
					return;
				}
			}
			mDownloadManager.remove(id);
		}

	}

	// 删除数据库记录,如果有文件,会稍后删除文件. 如果数据表格被删除,无法删除文件,所以必须自己删除一遍,其他删除函数类似
	public void deleteDownload(Context context, long id) {

		Uri uri = ContentUris.withAppendedId(
				Downloads.ALL_DOWNLOADS_CONTENT_URI, id);
		Cursor cursor = context.getContentResolver().query(uri, DELETE_COLUMNS,
				Downloads.COLUMN_DELETED + " != '1'", null, null);

		if(cursor == null){
			return;
		}
		
		if (!cursor.moveToFirst()) {
			return;
		}

		int status = cursor.getInt(mStatusColumnId);
		String filename = cursor.getString(mDataColumnId);

		boolean isComplete = Downloads.isStatusCompleted(status);

		if (isComplete && filename != null) {
			String path = Uri.parse(filename).getPath();
			if (path.startsWith(Environment.getExternalStorageDirectory()
					.getPath())) {
				mDownloadManager.markRowDeleted(id);
				return;
			}
		}
		mDownloadManager.remove(id);
	}

	public void pauseDownload(long... id) {
		mDownloadManager.pauseDownload(id);
	}

	public void resumeDownload(long... id) {
		mDownloadManager.resumeDownload(id);
	}

	public void restartDownload(long... id) {
		mDownloadManager.restartDownload(id);
	}

}
