package com.kibey.android.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * 获取相关系统信息
 * <p>
 * kibey 2013-10-12 下午2:26:34
 */
public class SystemUtils {
    private static Context mContext = AppProxy.getApp();


    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取指定应用程序的版本号
     *
     * @param packageName
     * @return String
     * kibey 2013-10-12 下午2:29:51
     */
    public static int getAppVersionCode(String packageName) {
        int versionCode = 0;

        if (packageName == null) {
            packageName = mContext.getPackageName();
        }

        try {
            PackageManager packageManager = getApp().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            Logs.e(e + "");
            versionCode = 0;
        }

        return versionCode;
    }


    /*
     * 判断是否是该签名打包
     */
    public static boolean isRelease(String signatureString) {
        final String releaseSignatureString = signatureString;
        if (releaseSignatureString == null || releaseSignatureString.length() == 0) {
            throw new RuntimeException("Release signature string is null or missing.");
        }

        final Signature releaseSignature = new Signature(releaseSignatureString);
        try {
            PackageManager pm = getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getApp().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature sig : pi.signatures) {
                if (sig.equals(releaseSignature)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Logs.e(e + "");
            return true;
        }
        return false;
    }

    /**
     * 判断是否是模拟器
     *
     * @return boolean
     * kibey 2013-10-12 下午2:28:40
     */
    public static boolean isEmulator() {
        return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk") || Build.MODEL.startsWith("Android SDK");
    }

    /**
     * 设置手机立刻震动
     */
    public static void vibrate(long milliseconds) {
        Vibrator vib = (Vibrator) getApp().getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static boolean isPortrait(Resources resources) {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == resources.getConfiguration().orientation;
    }

    /**
     * 得到本地IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (final Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                final NetworkInterface intf = en.nextElement();
                for (final Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    final InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (final SocketException ex) {
            if (Logs.IS_DEBUG) {
                Logs.i(" AndroidUtils getLocalIpAddress : " + ex.toString());
            }
        }
        return null;
    }


    /**
     * 复制文本
     *
     * @param content
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void copy(String content, Context context) {
        //
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * app是否显示在前台
     *
     * @param context
     * @return
     */
    public static boolean isRuningInTheForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取 metadata value
     */
    public static String getMetaValue(String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = AppProxy.getApp().getPackageManager().getApplicationInfo(AppProxy.getApp().getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
                if (TextUtils.isEmpty(apiKey)) {
                    apiKey = "" + metaData.getInt(metaKey);
                }
            }

        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * 获取当前进程名
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
        if (ListUtils.notEmpty(processes)) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : processes) {
                if (runningAppProcessInfo.pid == pid) {
                    String processName = runningAppProcessInfo.processName;
                    return processName;
                }
            }
        }
        return null;
    }


    public static boolean isMainProcess(Context context) {
        String processName = getCurrentProcessName(context);
        return context.getPackageName().equals(processName);
    }

    /**
     * 通过系统标准Api获取。实际内容理论上跟build.prop文件内容相同，但是是通过Api获取，不是读文件。比读文件更可靠。但是没有文件数据全。
     *
     * @return 格式化的 {@link Build}内容
     */
    public static String getSystemInfoByBuildClass() {
        try {
            Class<Build> clazz = Build.class;
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder result = new StringBuilder();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object o = field.get(null);
                    if (o instanceof Object[]) {
                        Object[] array = (Object[]) o;
                        result.append(field.getName()).append(" : ").append(Arrays.toString(array)).append("\n");
                    } else {
                        result.append(field.getName()).append(" : ").append(o.toString()).append("\n");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static void hideSystemUI(Activity activity) {
        // This snippet hides the system bars.
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void showSystemUI(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_VISIBLE);
    }

    /**
     * Converts an intent into a {@link Bundle} suitable for use as fragment
     * arguments.
     * <p>
     * 将intent转换为适用于fragment的bundle
     */
    public static Bundle intentToFragmentArguments(Intent intent) {
        final Bundle arguments = new Bundle();
        if (intent == null) {
            return arguments;
        }

        final Uri data = intent.getData();
        if (data != null) {
            arguments.putParcelable("_uri", data);
        }

        final Bundle extras = intent.getExtras();
        if (extras != null) {
            arguments.putAll(intent.getExtras());
        }

        return arguments;
    }

    public static Context getApp() {
        return AppProxy.getApp();
    }
}
