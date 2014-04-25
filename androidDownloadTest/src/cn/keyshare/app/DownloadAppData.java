package cn.keyshare.app;

//请必须保证，定义此类的包名、类名、变量名一致，值可以变就行。我是用反射来实现的。 主要是为了libproject 复用，但是同一个系统不能出现两个同样的provider
public class DownloadAppData {

	public static final String CONTENT_AUTHORITY = "cn.keyshare.test.download.content.provider";
	
	//下载成功的通知
	public static final String ACTION_DOWNLOAD_JUST_SUCCESS = "cn.keyshare.test.download.success";
	
	//下载成功后，点击通知栏的通知
	public static final String ACTION_NOTIFICATION_CLICK = "cn.keyshare.test.download.notification.click";

	//下载成功后，移除通知栏的通知
	public static final String ACTION_NOTIFICATION_SUCCESS_REMOVE = "cn.keyshare.test.download.notification.remove";
}
