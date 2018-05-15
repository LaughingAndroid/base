package com.kibey.android.lib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import com.kibey.android.app.IContext;

import dalvik.system.DexClassLoader;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public interface PluginApkHelperInterface {

    void init(String apkPath, String dexOutPath, Context context);

    Class<?> getClassById(String pluginName);

    PackageInfo getPackageInfo();

    DexClassLoader getDexClassLoader();

    Resources getResources();

    Resources.Theme getTheme();

    Context getContext();

    void setContext(IContext context);
}
