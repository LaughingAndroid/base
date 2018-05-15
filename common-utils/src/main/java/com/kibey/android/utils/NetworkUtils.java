package com.kibey.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 网络工具类
 * <p>
 * kibey 2013-10-22 下午1:08:35
 */
public class NetworkUtils {

    /**
     * 没有网络
     */
    public static final String NETWORKTYPE_INVALID = "NETWORKTYPE_INVALID";
    /**
     * wap网络
     */
    public static final String NETWORKTYPE_WAP = "wap";
    /**
     * 2G网络
     */
    public static final String NETWORKTYPE_2G = "2G";

    /**
     * 3G
     */
    public static final String NETWORKTYPE_3G = "3G";
    /**
     * 4G
     */
    public static final String NETWORKTYPE_4G = "4G";

    /**
     * wifi网络
     */
    public static final String NETWORKTYPE_WIFI = "WIFI";


    private static String sNetworkType;


    private static long sLastRefreshTime;

    /**
     * 有效期10min
     */
    private static final int VALID_TIME = 1000 * 60 * 10;

    /**
     * 检测手机是否开启GPRS网络,需要调用ConnectivityManager,TelephonyManager 服务.
     *
     * @param context
     * @return boolean
     */
    public static boolean checkGprsNetwork(Context context) {
        boolean has = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {
            has = info.isConnected();
        }
        return has;
    }

    /**
     * 检测手机是否开启WIFI网络,需要调用ConnectivityManager服务.
     *
     * @param context
     * @return boolean
     */
    public static boolean checkWifiNetwork(Context context) {
        boolean has = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (netType == ConnectivityManager.TYPE_WIFI) {
            has = info.isConnected();
        }
        return has;
    }

    public static boolean isNetworkAvailable() {
        Context context = AppProxy.getApp();
        return isNetworkAvailable(context);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return !(activeNetwork == null || !activeNetwork.isConnected());
    }

    /**
     * 手机是否处在漫游
     *
     * @param mCm
     * @return boolean
     */
    public boolean isNetworkRoaming(Context mCm) {
        ConnectivityManager connectivity = (ConnectivityManager) mCm.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        boolean isMobile = (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE);
        TelephonyManager mTm = (TelephonyManager) mCm.getSystemService(Context.TELEPHONY_SERVICE);
        boolean isRoaming = isMobile && mTm.isNetworkRoaming();
        return isRoaming;
    }

    public static void setsNetworkType(String sNetworkType) {
        NetworkUtils.sNetworkType = sNetworkType;
    }


    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},          *{@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI}
     */

    public static String getNetWorkTypeWithCache() {
        if (!TextUtils.isEmpty(sNetworkType)
                && (System.currentTimeMillis() - sLastRefreshTime) < VALID_TIME) {
            return sNetworkType;
        }
        return getNetWorkType();
    }

    public static String getNetWorkType() {
        ConnectivityManager manager = (ConnectivityManager) AppProxy.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        String mNetWorkType = "";
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                String proxyHost = android.net.Proxy.getDefaultHost();
                TelephonyManager telephonyManager = (TelephonyManager) AppProxy.getApp().getSystemService(Context.TELEPHONY_SERVICE);
                int netType = telephonyManager.getNetworkType();
                boolean isFast = isFastMobileNetwork(netType);
                String net2g3g4g = isFast ?
                        (netType == TelephonyManager.NETWORK_TYPE_LTE ? NETWORKTYPE_4G : NETWORKTYPE_3G)
                        : NETWORKTYPE_2G;
                mNetWorkType = TextUtils.isEmpty(proxyHost)
                        ? net2g3g4g
                        : NETWORKTYPE_WAP;
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        sNetworkType = mNetWorkType;
        sLastRefreshTime = System.currentTimeMillis();
        return mNetWorkType;
    }

    private static boolean isFastMobileNetwork(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }


}
