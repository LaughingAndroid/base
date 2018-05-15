package com.kibey.lib;

import android.content.Context;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public interface PluginApkManagerInterface {

    void init();

    void load(String pluginName, String apkPath, String dexOutPath, Context context);

    PluginApkHelper get(String pluginName);

    void uninstall(String pluginName);
}
