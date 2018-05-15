package com.kibey.android.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by mchwind on 15/3/5.
 */
public class FilePathManager {
    public static final String FILE_PATH = "kibey_echo";

    public static final String FILE_GAOSI_FILE = FilePathManager.getFilepath() + File.separator + "gaosi";
    /**
     * 系统sdcard路径，可能有多张
     */
    public static ArrayList<String> SYSTEM_SDCARD = new ArrayList<>();

    /**
     * 封面图片路径
     */
    public static String FILE_COVER_PATH = getFilepath() + File.separator + "cover";


    /**
     * 聊天视频路径
     */
    public static String FILE_CHAT_VIDEO_PATH = getFilepath() + File.separator + "chat_video";

    /**
     * 网络图片存储路径
     */
    public static String FILE_WEB_IMG_PATH = getFilepath() + File.separator + "temp";

    public static final String FILE_LOG = FilePathManager.getFilepath() + "/echo_log";
    /**
     * push点击日志
     */
    public static final String FILE_PUSH_LOG = FILE_LOG + "/push";
    /**
     * 错误日志
     */
    public static final String FILE_ERROR_LOG = FILE_LOG + "/error";
    /**
     * mark的信息
     */
    public static final String FILE_EVENT_MARK = FILE_LOG + "/mark";
    /**
     * 听歌流量日志
     */
    public static final String FILE_FLOW = FILE_LOG + "/flow";
    /**
     * APK路径
     */
    public static final String FILE_APK = FilePathManager.getFilepath() + "/apk";
    /**
     * apk patch路径
     */
    public static final String FILE_PATCH = FilePathManager.getFilepath() + "/.patch";
    /**
     * 保存对象json路径
     */
    public static final String FILE_OBJECT_JSON_DATA = FilePathManager.getFilepath() + "/data";
    public static File dataFile = new File(FILE_OBJECT_JSON_DATA);
    /**
     * 老版本离线文件路径
     */
    @Deprecated
    public static String FILE_OLD_DOWNLOAD_PATH = FilePathManager.getFilepath() + "/http/."
            + Md5Util.makeMd5Sum("acd") + "/" + Md5Util.makeMd5Sum("asaf") + "/" + Md5Util.makeMd5Sum("bbb");
    /**
     * 51-85版本离线文件路径
     */
    public final static String FILE_NEW_DOWNLOAD_PATH_51 = "/offline";
    /**
     * >=86版本离线文件路径
     */
    public final static String FILE_NEW_DOWNLOAD_PATH_86 = "/offline_86";


    public final static String PATH_PHOTO = "photo";

    /**
     * 临时文件夹
     */
    public final static String TEMP = "temp";
    /**
     * apk文件夹
     */
    public final static String APK = "apk";
    /**
     * 图片文件夹
     */
    public final static String PHOTO = "photo";
    /**
     * api文件夹
     */
    public final static String API = "api";

    /**
     * 获取app文件存储跟目录
     *
     * @return
     */
    public static String getFilepath() {
        String filepath = Environment.getExternalStorageDirectory().toString() + File.separator + FILE_PATH;
        if (!FileUtils.isSdCardEnable()) {
            filepath = AppProxy.getApp().getCacheDir() + File.separator + FILE_PATH;
        }
        return filepath;
    }

    /**
     * 获取离线下载路径
     *
     * @param newOfflineDir true 51-85 ; false >=86
     * @param position      如果有多张sd卡，需要遍历查找是否有离线file
     * @return
     */
    public static String getDownloadPath(boolean newOfflineDir, int position) {
        String rootPath = "";
        try {
            rootPath = SYSTEM_SDCARD.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootPath + (newOfflineDir ? FILE_NEW_DOWNLOAD_PATH_86 : FILE_NEW_DOWNLOAD_PATH_51);
    }


    /**
     * 保存错误日志
     *
     * @param filename
     * @param o
     */
    public static void saveErrorLog(String filename, Object o) {
        try {
            String time = DateUtil.getCurrentTimeJson18();
            time = time.replace(" ", "_");
            time = time.replace(":", "_");
            time = time.replace("-", "_");
            String file = FILE_ERROR_LOG + File.separator + filename;

            if (!new File(file).exists()) {
                new File(file).mkdirs();
            }

            FileUtils.writeFile(FILE_ERROR_LOG + File.separator + filename + "/" + time, o.toString() + "\n", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存push信息
     *
     * @param o
     */
    public static void savePushLog(Object o) {
        try {
            FileUtils.writeFile(FILE_PUSH_LOG, o.toString() + "\n", true);
        } catch (Exception e) {
        }
    }

    /**
     * 获取APK根目录
     *
     * @param url
     * @return
     */
    public static String getApk(String url) {
        return FILE_APK + File.separator + Md5Util.makeMd5Sum(url) + ".apk";
    }

    /**
     * 获取object数据存储路径
     *
     * @param key
     * @return
     */
    public static String getDataFile(String key) {
        if (!dataFile.exists()) {
            dataFile.mkdirs();
        }
        return FILE_OBJECT_JSON_DATA + File.separator + Md5Util.makeMd5Sum(key) + ".bak";
    }

    /**
     * 表情图片存储位置
     *
     * @param url
     * @return
     */
    public static String getEffectImage(String url) {
        try {

            String file = FilePathManager.getFilepath() + "/effect/";
            if (!new File(file).exists()) {
                new File(file).mkdirs();
            }
            return FilePathManager.getFilepath() + "/effect/" + Md5Util.makeMd5Sum(URLEncoder.encode(url.replace("*", ""), "UTF-8").getBytes());
        } catch (Exception e) {
        }
        return "";
    }


    /**
     * 生成一个图片路径
     *
     * @param context
     * @return
     */
    public static String getUploadImg(Context context) {
        File cacheDir = getDiskCacheDir(context, TEMP);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        String toPath = cacheDir.getAbsolutePath() + File.separator
                + System.currentTimeMillis() + ".jpg";
        return toPath;
    }

    /**
     * 生成一个图片路径
     *
     * @param context
     * @param houzhui "/name.jpg"
     * @return
     */
    public static String getUploadImg(Context context, String houzhui) {
        File cacheDir = getDiskCacheDir(context, TEMP);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        String toPath = cacheDir.getAbsolutePath() + File.separator
                + System.currentTimeMillis() + houzhui;
        return toPath;
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        final String cachePath = isAvailableSDCard() ? FilePathManager.getFilepath() : context.getCacheDir()
                .getPath();
        return new File(cachePath + File.separator + uniqueName);
    }

    private static boolean isAvailableSDCard() {
        final String status = Environment.getExternalStorageState();

        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 根据文件名获取一个高斯模糊的图片文件路径字符串
     *
     * @param filename 文件名（不含路径）
     * @return 高斯模糊的图片文件路径
     */
    public static String getGaosiFilePath(String filename) {
        return FilePathManager.FILE_GAOSI_FILE + File.separator + Md5Util.makeMd5Sum(filename);
    }

    /**
     * 获取系统camera目录
     *
     * @return
     */
    public static String getSystemCameraFilePath() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        return mediaStorageDir + File.separator + FILE_PATH;
    }

    public static String getThumbFilePath() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), ".thumbnails");
        return mediaStorageDir.toString();
    }


    public static String getVideoFilePath(String filename) {
        return FilePathManager.getFilepath() + File.separator
                + PHOTO + File.separator + Md5Util.makeMd5Sum(filename);
    }

}
