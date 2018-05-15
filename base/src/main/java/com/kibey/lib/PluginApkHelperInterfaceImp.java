package com.kibey.lib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.kibey.android.app.IContext;
import com.kibey.android.utils.Logs;
import com.kibey.plugin.R;

import dalvik.system.DexClassLoader;

/**
 * by liyihang
 * blog http://sijienet.com/
 */
public class PluginApkHelperInterfaceImp implements PluginApkHelperInterface {

    public static final String TAG = "SpeedApkHelper";

    private String apkPath;
    private Context ctx;

    private Drawable appIcon;
    private String pluginName;

    private PackageInfo packageInfo;
    private DexClassLoader dexClassLoader;
    private Resources resources;
    private Resources.Theme theme;
    private IContext mContext;

    @Override
    public void init(String apkPath, String dexOutPath, Context context) {
        this.apkPath = apkPath;
        this.ctx = context;

        packageInfo = PluginUtils.getPackageInfo(context, apkPath);

        appIcon = PluginUtils.getAppIcon(context, apkPath);
        pluginName = (String) PluginUtils.getAppLabel(context, apkPath);

        resources = PluginUtils.readApkRes(context, apkPath);
        this.theme = resources.newTheme();
        Logs.d(TAG, "app KibeyAppTheme:" + R.style.KibeyAppTheme + " plugin:" + resources.getIdentifier("KibeyAppTheme", "style", packageInfo.packageName));
        this.theme.applyStyle(resources.getIdentifier("KibeyAppTheme", "style", packageInfo.packageName), false);
        dexClassLoader = PluginUtils.readDexFile(context, apkPath, dexOutPath);
    }

    @Override
    public Class<?> getClassById(String classTag) {
        if ("create_context".equals(classTag)) {
            return null;
        }
        Class<?> aClass = null;
        try {
            String string = packageInfo.applicationInfo.metaData.getString(classTag);
            if (null == string) {
                string = classTag;
            }
            aClass = dexClassLoader.loadClass(string);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, classTag + " " + e.getMessage());
        }
        return aClass;
    }

    @Override
    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    @Override
    public DexClassLoader getDexClassLoader() {
        return dexClassLoader;
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    @Override
    public Resources.Theme getTheme() {
        return theme;
    }

    @Override
    public Context getContext() {
        if (null == mContext) {
            return null;
        }
        return mContext.getActivity();
    }

    @Override
    public void setContext(IContext context) {
        if (context instanceof Activity) {
            mContext = context;
        }
    }
}
