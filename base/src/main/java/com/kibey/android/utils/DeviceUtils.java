package com.kibey.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.UUID;

/**
 * @author mchwind
 * @version V1.0
 * @since 16/6/3
 */
public class DeviceUtils {
    private static final String KEY_IMEI = "DeviceUtils_IMEI";

    static Context getApp() {
        return AppProxy.getApp();
    }

    private static String ANDROID_ID;


    /**
     * 获取设备唯一id，不是必须不要用，需要权限
     */
    public static String getImeiId() {
        String imei_id = PrefsHelper.getDefault().getString(KEY_IMEI);
        if (TextUtils.isEmpty(imei_id)) {
            final TelephonyManager telephonyManager = (TelephonyManager) getApp()
                    .getSystemService(Context.TELEPHONY_SERVICE);

            imei_id = telephonyManager.getDeviceId();
            imei_id = imei_id == null ? "" : imei_id;
            PrefsHelper.getDefault().save(KEY_IMEI, imei_id);
        }
        return imei_id;
    }

    /**
     * dp to px
     *
     * @param dpValue
     * @return
     */
    public static int d2p(float dpValue) {
        final float scale = getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px to dp
     *
     * @param pxValue
     * @return
     */
    public static int p2d(float pxValue) {
        final float scale = getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取签名
     *
     * @param context
     * @param pkName
     * @return
     */
    public static String getSign(Context context, String pkName) {
        Signature[] signs = getRawSignature(context, pkName);
        if ((signs == null) || (signs.length == 0)) {
            return "";
        } else {
            Signature sign = signs[0];
            String signMd5 = Md5Util.makeMd5SumTwo(sign.toByteArray());
            return signMd5.toLowerCase();
        }
    }

    public static Signature[] getRawSignature(Context context, String packageName) {
        if ((packageName == null) || (packageName.length() == 0)) {
            return null;
        }

        PackageManager pkgMgr = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pkgMgr.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        if (info == null) {
            return null;
        }

        return info.signatures;
    }

    public static boolean isPortrait(Resources resources) {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT == resources.getConfiguration().orientation;
    }

    /**
     * Role:获取当前设置的电话号码 <BR>
     * Date:2012-3-12 <BR>
     *
     * @author CODYY)peijiangping
     */
    public static String getPhoneNumber() {
        try {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager) AppProxy.getApp().getSystemService(Context.TELEPHONY_SERVICE);

            String phone = mTelephonyMgr.getLine1Number();

            if (null != phone && phone.length() > 11) {
                phone = phone.substring(phone.length() - 11, phone.length());
            }
            return phone;
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 获取运营商代码
     *
     * @return mcc+mnc. 如移动64002
     */
    public static String getSimOperator() {
        try {
            TelephonyManager mTelephonyMgr;
            mTelephonyMgr = (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);

            String operator = mTelephonyMgr.getSimOperator();

            return operator;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取屏幕分辨率
     *
     * @param activity
     * @return displayMerics[0] = width displayMerics[1] = height
     */
    public static int[] getDisplayMetrics(Activity activity) {
        DisplayMetrics dm;// 屏幕分辨率容器
        final int[] displayMerics = new int[2];
        dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        displayMerics[0] = dm.widthPixels;
        displayMerics[1] = dm.heightPixels;

        return displayMerics;
    }

    /**
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        String androidID = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return null == androidID ? "" : androidID;
    }

    /**
     * 得到设备序列号
     *
     * @param context
     * @return
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String sim = tm.getSimSerialNumber();
        return null == sim ? "" : sim;
    }

    static String token = "";

    /**
     * 得到设备唯一识别码
     *
     * @return
     */
    public static String getUniqueNumber() {
        try {
            if (TextUtils.isEmpty(token) || token.equals("echo_device_unknow")) {
                String androidID = getAndroidID(getApp());
                String imei = getImeiId();
                String simSerialNumber = getSimSerialNumber(getApp());
                UUID uuid = new UUID(androidID.hashCode(), ((long) imei.hashCode() << 32) | simSerialNumber.hashCode());
                token = uuid.toString();
                if (Logs.IS_DEBUG) {
                    Logs.e("echo_token:" + token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            token = "echo_device_unknow";
        }

        return token;
    }

    public static class DeviceInfoUtil {
        /**
         * @param context
         * @return
         */
        public static String getAndroidID(Context context) {
            if (null == ANDROID_ID) {
                try {
                    ANDROID_ID = Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return ANDROID_ID;
        }


        /**
         * 得到设备序列号
         *
         * @return
         */
        public static String getSimSerialNumber() {
            TelephonyManager tm = (TelephonyManager) getApp().getSystemService(Context.TELEPHONY_SERVICE);
            String sim = tm.getSimSerialNumber();
            return null == sim ? "" : sim;
        }

        static String token = "";

        /**
         * 得到设备唯一识别码
         *
         * @return
         */
        public static String getUniqueNumber() {
            try {
                if (TextUtils.isEmpty(token) || token.equals("echo_device_unknow")) {
                    String androidID = getAndroidID(getApp());
                    String imei = getImeiId();
                    String simSerialNumber = getSimSerialNumber();
                    UUID uuid = new UUID(androidID.hashCode(), ((long) imei.hashCode() << 32) | simSerialNumber.hashCode());
                    token = uuid.toString();
                    if (Logs.IS_DEBUG) {
                        Logs.e("echo_token:" + token);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                token = "echo_device_unknow";
            }
            return token;
        }

    }

    public static String getMac() {
        String macAddress = "";
        WifiManager wifiMgr = (WifiManager) (AppProxy.getApp().getSystemService(Context.WIFI_SERVICE));
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }
}
