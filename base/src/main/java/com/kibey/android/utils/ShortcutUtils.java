package com.kibey.android.utils;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;

import java.util.List;

import static android.content.pm.PackageManager.NameNotFoundException;

/**
 * Created by mchwind on 15/8/7.
 * 快捷桌面处理
 */
public class ShortcutUtils {
    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

    public static void addShortcut(Context context, String name, Class activityClass, int iconId) {
        if (hasInstallShortcut(context, name)) return;
        Intent addIntent = new Intent(ACTION_ADD_SHORTCUT);
        Parcelable icon = Intent.ShortcutIconResource.fromContext(context, iconId); //获取快捷键的图标
        Intent myIntent = new Intent(context, activityClass);
        myIntent.setAction(Intent.ACTION_MAIN);
        myIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);//快捷方式的标题
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);//快捷方式的图标
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);//快捷方式的动作
        context.sendBroadcast(addIntent);//发送广播

        PrefsHelper.getDefault(context).save("hasCreateShortcut", true);
    }

    public static void removeShortcut(Context context, String name, Class activityClass) {
        // remove shortcut的方法在小米系统上不管用，在三星上可以移除
        Intent intent = new Intent(ACTION_REMOVE_SHORTCUT);

        // 名字
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);

        // 设置关联程序
        Intent launcherIntent = new Intent(context,
                activityClass).setAction(Intent.ACTION_MAIN);

        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);

        // 发送广播
        context.sendBroadcast(intent);
    }

    private static boolean hasInstallShortcut(Context context, String name) {
        return PrefsHelper.getDefault(context).getBoolean("hasCreateShortcut") || hasShortcut(name);
    }

    private static boolean hasShortcut(String name) {
        boolean hasShortcut = false;

        String url;
        if (getSystemVersion() < 8) {
            url = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }

        ContentResolver resolver = AppProxy.getApp().getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), new String[]{"title", "iconResource"},
                "title=?", new String[]{name}, null);
        if (cursor != null && cursor.getCount() > 0) {
            hasShortcut = true;
        }
        Logs.e(name + "hasShortcut:" + hasShortcut);

        return hasShortcut;
    }

    private static int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean addShortcutByPackageName(Context context, String pkg) {
        // 快捷方式名
        String title = "unknown";
        // MainActivity完整名
        String mainAct = null;
        // 应用图标标识
        int iconIdentifier = 0;
        // 根据包名寻找MainActivity
        PackageManager pkgMag = context.getPackageManager();
        Intent queryIntent = new Intent(Intent.ACTION_MAIN, null);
        queryIntent.addCategory(Intent.CATEGORY_LAUNCHER);// 重要，添加后可以进入直接已经打开的页面
        queryIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        queryIntent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

        List<ResolveInfo> list = pkgMag.queryIntentActivities(queryIntent, PackageManager.GET_INTENT_FILTERS);
        for (int i = 0; i < list.size(); i++) {
            ResolveInfo info = list.get(i);
            if (info.activityInfo.packageName.equals(pkg)) {
                title = info.loadLabel(pkgMag).toString();
                mainAct = info.activityInfo.name;
                iconIdentifier = info.activityInfo.applicationInfo.icon;
                break;
            }
        }

        if (mainAct == null) {
            // 没有启动类
            return false;
        }
        Intent shortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 不允许重复创建
        shortcut.putExtra("duplicate", false);
        ComponentName comp = new ComponentName(pkg, mainAct);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                queryIntent.setComponent(comp));
        // 快捷方式的图标
        Context pkgContext = null;
        if (context.getPackageName().equals(pkg)) {
            pkgContext = context;
        } else {
            // 创建第三方应用的上下文环境，为的是能够根据该应用的图标标识符寻找到图标文件。
            try {
                pkgContext = context.createPackageContext(pkg,
                        Context.CONTEXT_IGNORE_SECURITY
                                | Context.CONTEXT_INCLUDE_CODE);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (pkgContext != null) {
            Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource
                    .fromContext(pkgContext, iconIdentifier);
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        }
        // 发送广播，让接收者创建快捷方式
        // 需权限<uses-permission
        // android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
        context.sendBroadcast(shortcut);
        return true;
    }
}
