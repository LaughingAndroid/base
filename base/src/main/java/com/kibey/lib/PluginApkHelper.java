package com.kibey.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import com.kibey.android.app.IContext;

import dalvik.system.DexClassLoader;

/**
 * by liyihang
 * blog http://sijienet.com/
 */
public class PluginApkHelper {

    PluginApkHelperInterface apkHelperInterface;

    public PluginApkHelper(String apkPath, String dexOutPath, Context context) {
        apkHelperInterface = (PluginApkHelperInterface) new PluginInvocationHandler().bind(PluginApkHelperInterface.class);
        apkHelperInterface.init(apkPath, dexOutPath, context);
    }

    public <T> Class<T> getClassById(String pluginName) {
        return (Class<T>) apkHelperInterface.getClassById(pluginName);
    }

    public PackageInfo getPackageInfo() {
        return apkHelperInterface.getPackageInfo();
    }

    public DexClassLoader getDexClassLoader() {
        return apkHelperInterface.getDexClassLoader();
    }

    public Resources getResources() {
        return apkHelperInterface.getResources();
    }

    public Resources.Theme getTheme() {
        return apkHelperInterface.getTheme();
    }

    public void setContext(IContext context) {
        apkHelperInterface.setContext(context);
    }

    public Context getContext() {
        return apkHelperInterface.getContext();
    }
}
