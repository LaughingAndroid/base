package com.kibey.android.utils;

import android.os.Environment;
import android.provider.Settings;

/**
 * @author Laughing.hu(laughing.hu.android@gmail.com)
 * @version V1.0
 * @d2012-7-26
 */
public class HLPConstants {
    public static String RECORD_GUIDE_CLASS_NAME = "com.kibey.echo.ui2.guide.ShortVideoRecordGuideDialog";
    public static String RECORD_GUIDE_CLASS_NAME_SHOW_METHOD = "show";
    /**
     * Bell download key secret
     */
    public static String B_SECRET = "F2B5055916EB7C5649244798E7E4405C3A720E9D9591B7D8BABE40D3D50E5873";
    public static final String APP_OPEN_COUNT = "app_open_count";
    public static final int DAY = 24 * 60 * 60;
    public static final int HOUR = 60 * 60;
    public static final int MINITE = 60;
    public static final int MINISECOND = 60 * 1000;
    public static String SYS_PACKAGE = "";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String JSON_STANDARD_DATEF_FORMAT_18 = "yyyy-MM-dd HH:mm:ss";
    public static final String JSON_STANDARD_DATEF_FORMAT_10 = "yyyy-MM-dd";

    /**
     * rootview id
     **/
    public static int id = 0x101010;
    /**
     * 手机相机照片存储地址
     **/
    public static final String SD_CAMERA_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
    /**
     * 图片名称前缀
     **/
    public static final String TRIPPLUS = "image";
    /**
     * 保存登录信息
     **/
    public static final String DISTRICT_PREFIX = "prefix";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";

    public static final String UPDATE = "hulapai_update_intent_activon";
    public static final String ALREADY_DOWNLOAD_VIERSION = "ALREADY_DOWNLOAD_VIERSION";
    public static final String VERSION = "VERSION";
    public static final String APK_LENGTH = "apk_length";
    public static final String HTTP_DEVICE = "User-Agent";
    /**
     * 状态栏高度
     */
    public static final String KEY_TOP_BAR_HEIGHT = "KEY_TOP_BAR_HEIGHT";
    /**
     * 底部系统菜单高度
     */
    public static final String KEY_BOTTOM_BAR_HEIGHT = "KEY_BOTTOM_BAR_HEIGHT";

    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";

    public static String setUserAgent() {
//        ua=Android4.3,OPPO R6007,00000000-5d8e-ae3b-e300-1df800000000,V2.7-debug,65
        String RELEASE = android.os.Build.VERSION.RELEASE;
        String MANUFACTURER = android.os.Build.MANUFACTURER;
        MANUFACTURER = null == MANUFACTURER ? "unknow" : MANUFACTURER.replace(",", "");

        String MODEL = android.os.Build.MODEL;
        MODEL = null == MODEL ? "unknow" : MODEL.replace(",", "");

        String id = DeviceUtils.getUniqueNumber();
        String versionName = APPConfig.getVersionName();
        String versionCode = APPConfig.getVersionCode() + "";
        StringBuffer buffer = new StringBuffer();
        buffer.append("Android ").
                append(RELEASE).append(",")
                .append(MANUFACTURER).append(" ")
                .append(MODEL)
                .append(",").append(id)
                .append(",").append(versionName)
                .append(",").append(versionCode)
                .append(",").append(DeviceUtils.getImeiId());
        PrefsHelper.getDefault().save(HLPConstants.HTTP_DEVICE, buffer.toString());
        if (Logs.IS_DEBUG) {
            Logs.e("ua=" + buffer);
            String brand = android.os.Build.MODEL;
            String model = android.os.Build.MODEL;
            String androidID = Settings.Secure.getString(AppProxy.getApp().getContentResolver(),
                    Settings.Secure.ANDROID_ID);





        }
        return buffer.toString();
    }

    public static boolean isCDMC812() {
        try {
            return getUserAgent().split(",")[1].toLowerCase().contains("m812");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getUserAgent() {
        return PrefsHelper.getDefault().getString(HLPConstants.HTTP_DEVICE);
    }


    public static final String kQiniuDomainName = "http://kibey-echo.qiniudn.com";
    public static final String kQiniuImageDomainName = "http://echo-image.qiniudn.com";
    public static final String kQiniuVideoDomainName = "http://7xjclq.com1.z0.glb.clouddn.com";
    //上传证件照七牛域名
    public static final String kQiniuFamousDomainName = "http://7xik56.com2.z0.glb.qiniucdn.com";
    //上传铃声
    public static final String kQiniuRingtoneDomainName = "http://ocnurs87t.bkt.clouddn.com";

    public static final String kUpYunBucketName = "kibey-echo";
    public static final String kUpYunPasscode = "zbWfFna8A4ELEUsO8vZG/ilddI8=";
    public static final String kUpYunDomainName = "http://kibey-echo.b0.upaiyun.com";

    public static final String kUpYunAvatarBucketName = "kibey-sys-avatar";
    public static final String kUpYunAvatarPasscode = "oYuJzA/iMKob+vYMifnK5Iq+CPs=";
    public static final String kUpYunAvatarDomainName = "http://kibey-sys-avatar.b0.upaiyun.com";
}
