package com.example.androiddownloadtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import cn.keyshare.app.DownloadAppData;
import cn.keyshare.utils.ToastUtil;

public class MyDownloadReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(action!=null){
			if(action.equals(DownloadAppData.ACTION_DOWNLOAD_JUST_SUCCESS)){
				
				Bundle bundle = intent.getExtras();
				String path = null;
				if(bundle != null){
					long id = bundle.getLong(cn.keyshare.download.core.Constants.KEY＿ID);
					path = DownloadUtil.getInstance(context).getFileName(context, id);
				}
				ToastUtil.showToastCancelShowing(context, "下载成功"+path, Toast.LENGTH_LONG);
			}
		}
	}


}
