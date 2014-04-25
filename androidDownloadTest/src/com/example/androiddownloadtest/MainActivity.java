package com.example.androiddownloadtest;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cn.keyshare.utils.SdCardUtils;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button bt = (Button)findViewById(R.id.test);
		
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DownloadUtil.getInstance(MainActivity.this).startDownload("http://gdown.baidu.com/data/wisegame/1c0a61760c95fdde/baidushoujizhushou_16782891.apk", "百度手机助手", SdCardUtils.getFirstExterPath()+File.separator+"Downloads" + File.separator, null);
			}
		});
		
	}

}
