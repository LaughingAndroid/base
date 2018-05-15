package com.kibey.android.utils.shake;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.kibey.android.utils.APPConfig;
import com.kibey.android.utils.AppProxy;
import com.kibey.android.utils.Logs;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DebugOnOff
 *
 * @author xl
 * @version V1.0
 * @since 23/12/2016
 */
public final class DebugOnOff implements ShakeDetector.ShakeListener {
    private ShakeDetector mShakeDetector;
    private Context mContext;

    public DebugOnOff(Context context) {
        if (!Logs.IS_DEBUG) {
            return;
        }
        if (context == null) {
            throw new NullPointerException("Context can not be null");
        }
        mContext = context.getApplicationContext();
    }

    public void unregisterShakeDetector() {
        if (!Logs.IS_DEBUG) {
            return;
        }
        if (mShakeDetector != null) {
            mShakeDetector.stop();
        }
    }

    public void registerShakeDetector() {
        if (!Logs.IS_DEBUG) {
            return;
        }
        if (mShakeDetector == null) {
            mShakeDetector = new ShakeDetector(this);
        }
        mShakeDetector.start((SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE));
    }

    private Map<Object, Boolean> mShowing = new HashMap<>();
    AlertDialog mAlertDialog;

    public static void restartApp() {
        if (!Logs.IS_DEBUG) {
            return;
        }
        Context context = APPConfig.getFirstActivity();
        if (context == null) {
            return;
        }
        Toast.makeText(context, "正在重启App，部分手机可能会延迟3~5秒", Toast.LENGTH_SHORT).show();
        context = context.getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        context.startActivity(intent);

        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long triggerTime = SystemClock.elapsedRealtime() + 1000;
        int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mgr.setExact(type, triggerTime
                    , PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        } else {
            mgr.set(type, triggerTime
                    , PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        }
        Process.killProcess(Process.myPid());

//        MEchoEventBusEntity.post(MEchoEventBusEntity.EventBusType.REFRESH_MAIN_LOGIN_UI, 500);
    }

    @Override
    public void onShake() {
        if (!Logs.IS_DEBUG) {
            return;
        }
        final Map<String, Object> map = new LinkedHashMap<>();
        Activity activity = APPConfig.getFirstActivity();
        map.put("切换服务器", new Runnable() {
            @Override
            public void run() {
                APPConfig.getObject(SwitchServer.class).showServerDialog(activity);
            }
        });
        if (activity == null || (mShowing.get(activity.hashCode()) != null && mShowing.get(activity.hashCode()))) {
            return;
        }
        if (activity instanceof IRegisterDebugMethod) {
            ((IRegisterDebugMethod) activity).registerDebugMethod(map);
        }
        if (activity instanceof FragmentActivity) {
            List<Fragment> fragments = ((FragmentActivity) activity).getSupportFragmentManager().getFragments();
            //noinspection Convert2streamapi
            if (null != fragments) {
                for (Fragment fragment : fragments) {
                    if (fragment == null) {
                        continue;
                    }
                    if (fragment instanceof IRegisterDebugMethod) {
                        ((IRegisterDebugMethod) fragment).registerDebugMethod(map);
                    }
                    if (fragment != null) {
                        FragmentManager childFragmentManager = fragment.getChildFragmentManager();
                        if (childFragmentManager != null) {
                            List<Fragment> childFragments = childFragmentManager.getFragments();
                            if (childFragments != null) {
                                for (Fragment childFragment : childFragments) {
                                    if (childFragment instanceof IRegisterDebugMethod) {
                                        ((IRegisterDebugMethod) childFragment).registerDebugMethod(map);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("开发者菜单");
        final String[] array = map.keySet().toArray(new String[]{});
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Object obj = map.get(array[which]);
                if (obj instanceof Class) {
                    try {
                        // 检查是否是Activity的子类
                        if (Activity.class.isAssignableFrom((Class) obj)) {
                            Intent intent = new Intent(mContext, (Class) obj);
                            activity.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (obj instanceof DialogInterface.OnClickListener) {
                    ((DialogInterface.OnClickListener) obj).onClick(dialog, which);
                }

                if (obj instanceof Runnable) {
                    ((Runnable) obj).run();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mShowing.put(activity.hashCode(), false);
            }
        });
        dialog.show();
        mShowing.put(activity.hashCode(), true);
    }

    public static void register() {
        if (Logs.IS_DEBUG) {
            DebugOnOff debugOnOff = new DebugOnOff(AppProxy.getApp());
            debugOnOff.registerShakeDetector();
        }
    }

}
