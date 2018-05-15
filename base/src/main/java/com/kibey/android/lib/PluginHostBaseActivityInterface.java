package com.kibey.android.lib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.kibey.android.app.IContext;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public interface PluginHostBaseActivityInterface {

    void init(IContext context, String name);

    boolean isInit();

    PluginBaseInterface getBaserProxy(String pluginName, String classTag);

    Resources getResources();

    AssetManager getAssets();

    ClassLoader getClassLoader();

    Resources.Theme getTheme();

    PackageInfo getPackageInfo();



}
