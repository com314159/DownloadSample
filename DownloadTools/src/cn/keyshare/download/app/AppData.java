package cn.keyshare.download.app;

import java.lang.reflect.Field;

import cn.keyshare.download.core.Downloads;



//因为这是lib库,有provider,和通知,所以每一个单独的app project如果需要使用这个库,就必须定义自己的provider和通知信息
public class AppData {

		
    public static String initAuthority() {
        String authority = "cn.keyshare.downloadtool";

        try {

            ClassLoader loader = Downloads.class.getClassLoader();

            Class<?> clz = loader.loadClass("cn.keyshare.app.DownloadAppData");
            Field declaredField = clz.getDeclaredField("CONTENT_AUTHORITY");

            authority = declaredField.get(null).toString();
        } catch (ClassNotFoundException e) {} 
        catch (NoSuchFieldException e) {} 
        catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        return authority;
    }
    
    
    public static String initSuccessAction() {
        String authority = "cn.keyshare.downloadtool.success";

        try {

            ClassLoader loader = Downloads.class.getClassLoader();

            Class<?> clz = loader.loadClass("cn.keyshare.app.DownloadAppData");
            Field declaredField = clz.getDeclaredField("ACTION_DOWNLOAD_JUST_SUCCESS");

            authority = declaredField.get(null).toString();
        } catch (ClassNotFoundException e) {} 
        catch (NoSuchFieldException e) {} 
        catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        return authority;
    }
    
    public static String initNotificationClickAction() {
        String authority = "cn.keyshare.downloadtool.notificationclick";

        try {

            ClassLoader loader = Downloads.class.getClassLoader();

            Class<?> clz = loader.loadClass("cn.keyshare.app.DownloadAppData");
            Field declaredField = clz.getDeclaredField("ACTION_NOTIFICATION_CLICK");

            authority = declaredField.get(null).toString();
        } catch (ClassNotFoundException e) {} 
        catch (NoSuchFieldException e) {} 
        catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        return authority;
    }
    
    public static String initNotificationRemoveAction() {
        String authority = "cn.keyshare.downloadtool.notificationRemove";

        try {

            ClassLoader loader = Downloads.class.getClassLoader();

            Class<?> clz = loader.loadClass("cn.keyshare.app.DownloadAppData");
            Field declaredField = clz.getDeclaredField("ACTION_NOTIFICATION_SUCCESS_REMOVE");

            authority = declaredField.get(null).toString();
        } catch (ClassNotFoundException e) {} 
        catch (NoSuchFieldException e) {} 
        catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        return authority;
    }

}
