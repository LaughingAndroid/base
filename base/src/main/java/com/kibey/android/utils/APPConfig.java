package com.kibey.android.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mchwind
 * @des
 * @since 16/4/9
 */
public class APPConfig {
    public static final String USER_AGENT = "APPConfig_USER_AGENT";
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String SECRET = "d&3j^"; // 和app签名生产原始密钥
    public static final String COOKIE = "Cookie";
    public static final String DEFAULT__SECRET = "000102030405060708090a0b0c0d0e0f";

    public static String ROOT_FILE_NAME = "kibey_echo";
    public static int API_VERSION = 9;
    private static int VERSION_CODE;
    private static String VERSION_NAME;
    public static String S1 = "";
    public static int ID = 0x301010;

    private static APPConfig sAppConfig;
    public static boolean isNew;
    private Handler sHandler;

    /**
     * 设备user agent，通过{@link WebSettings#getUserAgentString()}获取
     */
    private static String DEVICE_USER_AGENT;

    private LinkedList<WeakReference<Activity>> mActivityList = new LinkedList<>();
    private static List<Object> mObjects = new ArrayList<>();

    public static void putObject(Object obj) {
        for (Object o : mObjects) {
            if (subClassOfClz(o.getClass(), obj.getClass())) {
                mObjects.remove(o);
                break;
            }
        }
        mObjects.add(obj);
    }

    public static <T> T getObject(Class<T> clz) {
        if (mObjects.isEmpty()) {
//            AppInit.initData(); // TODO: 17/3/6  
        }

        for (Object o : mObjects) {
            if (subClassOfClz(o.getClass(), clz)) {
                return (T) o;
            }
        }
        return null;
    }

    public static boolean subClassOfClz(Class o, Class clz) {
        if (null == o) return false;

        if (o.getName().equals(clz.getName())) {
            return true;
        }
        return subClassOfClz(o.getSuperclass(), clz);
    }


    /// test
    public static APPConfig getAppConfig() {
        if (null == sAppConfig) {
            synchronized (APPConfig.class) {
                if (null == sAppConfig) {
                    sAppConfig = new APPConfig();
                }
            }
        }
        return sAppConfig;
    }

    public void init() {
        setUserAgent();
        initAppSign();
        sHandler = new Handler();
    }

    private static Context getApp() {
        return AppProxy.getApp();
    }

    private APPConfig() {
        init();
    }

    public static Activity getFirstActivity() {
        LinkedList<WeakReference<Activity>> list = getActivityList();
        if (ListUtils.isEmpty(list)) return null;
        return list.getLast().get();
    }

    public static void addActivity(Activity activity) {
        if (null == activity) return;
        List<WeakReference<Activity>> list = getActivityList();
        WeakReference<Activity> reference = new WeakReference<>(activity);
        if (activity instanceof ILimitedActivity) {
            int limitedNum = ((ILimitedActivity) activity).getLimitedNum();
            int count = 0;
            try {
                int firstLimitActivityPosition = -1;
                for (int i = 0; i < list.size(); i++) {
                    WeakReference<Activity> weakReference = list.get(i);
                    if (null != weakReference
                            && null != weakReference.get()
                            && weakReference.get().getClass() == activity.getClass()) {
                        count++;
                        if (firstLimitActivityPosition == -1) {
                            firstLimitActivityPosition = i;
                        }
                    }
                }

                if (count >= limitedNum) {
                    WeakReference<Activity> weakReference = list.remove(firstLimitActivityPosition);
                    if (null != weakReference && null != weakReference.get()) {
                        weakReference.get().finish();
                    }
                }
            } catch (Exception e) {
            }
        }
        list.add(reference);

        Logs.i("echo_activity: add " + activity + " " + list.size());
    }

    public static boolean removeActivity(Activity activity) {
        List<WeakReference<Activity>> list = getActivityList();
        boolean flag = false;
        if (null == list) {
            flag = false;
        } else {
            int length = list.size();
            for (int i = 0; i < length; i++) {
                if (null != list.get(i) && activity.equals(list.get(i).get())) {
                    list.remove(i);
                    flag = true;
                    break;
                }
            }
        }

        Logs.i("echo_activity: remove " + activity + " " + list.size());
        return flag;
    }

    public static void exit(boolean flag) {
        List<WeakReference<Activity>> list = getActivityList();
        if (null != list) {
            Iterator<WeakReference<Activity>> it = list.iterator();
            while (it.hasNext()) {
                Activity activity = it.next().get();
                if (null != activity) {
                    activity.finish();
                }
            }
            list.clear();
        }
        NotificationManager notificationManager = (NotificationManager) getApp()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if (flag) {
            System.exit(0);
        }

        ExitProxy exitProxy = getObject(ExitProxy.class);
        if (null != exitProxy) {
            exitProxy.exit(flag);
        }
    }

    public static LinkedList<WeakReference<Activity>> getActivityList() {
        return getAppConfig().mActivityList;
    }

    /**
     * 获取系统默认的UA
     *
     * @return 系统默认的ua
     */
    public static String getSystemUserAgent() {
        if (null == DEVICE_USER_AGENT) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    DEVICE_USER_AGENT = WebSettings.getDefaultUserAgent(AppProxy.getApp());
                } else {
                    DEVICE_USER_AGENT = new WebView(AppProxy.getApp()).getSettings().getUserAgentString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                DEVICE_USER_AGENT = getUserAgent();
            }
        }
        return DEVICE_USER_AGENT;
    }

    public static String getUserAgent() {
        return PrefsHelper.getDefault().getString(USER_AGENT);
    }

    public static String setUserAgent() {
//        ua=Android4.3,OPPO R6007,00000000-5d8e-ae3b-e300-1df800000000,V2.7-debug,65
        String RELEASE = Build.VERSION.RELEASE;
        String MANUFACTURER = Build.MANUFACTURER;
        MANUFACTURER = null == MANUFACTURER ? "unknow" : MANUFACTURER.replace(",", "");

        String MODEL = Build.MODEL;
        MODEL = null == MODEL ? "unknow" : MODEL.replace(",", "");

        String id = DeviceUtils.DeviceInfoUtil.getUniqueNumber();
        String versionName = getVersionName();
        int versionCode = getVersionCode();
        StringBuffer buffer = new StringBuffer();
        buffer.append("Android ").
                append(RELEASE).append(",")
                .append(MANUFACTURER).append(" ")
                .append(MODEL)
                .append(",").append(id)
                .append(",").append(versionName)
                .append(",").append(versionCode)
                .append(",").append(DeviceUtils.getImeiId());
        PrefsHelper.getDefault().save(USER_AGENT, buffer.toString());
        if (Logs.IS_DEBUG) {
            Logs.e("ua=" + buffer + "   -------getUniqueNumber=" + DeviceUtils.DeviceInfoUtil.getUniqueNumber());
        }
        return buffer.toString();
    }

    public static void post(Runnable runnable) {
        getAppConfig().sHandler.post(runnable);
    }


    public static void postDelayed(Runnable runnable, long delay) {
        getAppConfig().sHandler.postDelayed(runnable, delay);
    }

    public static void removeRunnable(Runnable runnable) {
        getAppConfig().sHandler.removeCallbacks(runnable);
    }

    public static String getPackageName() {
        return getApp().getPackageName();
    }

    public static int getVersionCode() {
        if (0 == VERSION_CODE) {
            try {
                VERSION_CODE = getApp().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return VERSION_CODE;
    }

    public static String getVersionName() {
        if (null == VERSION_NAME) {
            try {
                VERSION_NAME = getApp().getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return VERSION_NAME;
    }

    /**
     * 粗略的判断是否新安装
     *
     * @return
     */
    private static boolean isNewInstall() {
        return !new File(FilePathManager.getFilepath()).exists();
    }

    /**
     * @throws Exception
     */
    public static void initAppSign() {
        S1 = DeviceUtils.getSign(getApp(), getPackageName());
    }

    public static FragmentManager getFragmentManager() {
        Activity activity = getFirstActivity();
        if (activity != null && activity instanceof FragmentActivity) {
            Log.d("echo_result", "NAME=" + activity.getClass().toString());
            return ((FragmentActivity) activity).getSupportFragmentManager();
        }

        return null;
    }

    public static void activityFinish(boolean allFinish) {
        List<WeakReference<Activity>> mActivityStack = getActivityList();
        if (null == mActivityStack) return;
        for (int i = 0; i < mActivityStack.size(); i++) {
            if (null != mActivityStack.get(i) && (allFinish || !(mActivityStack.get(i).get() instanceof IMainActivity))) {
                if (mActivityStack.get(i).get() != null) {
                    mActivityStack.get(i).get().finish();
                }
            }
        }
        mActivityStack.clear();
    }

    public static IMainActivity getMainActivity() {
        List<WeakReference<Activity>> list = APPConfig.getActivityList();
        for (WeakReference<Activity> item : list) {
            if (item != null && item.get() instanceof IMainActivity) {
                return (IMainActivity) item.get();
            }
        }
        return null;
    }


    public interface ILimitedActivity {
        int getLimitedNum();
    }

    public static abstract class ExitProxy {
        public abstract void exit(boolean flag);
    }
}
