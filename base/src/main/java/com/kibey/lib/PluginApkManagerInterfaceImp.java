package com.kibey.lib;

import android.content.Context;

import com.kibey.manager.PluginManager;

import java.util.HashMap;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class PluginApkManagerInterfaceImp implements PluginApkManagerInterface {

    private HashMap<String, PluginApkHelper> apkHelperHashMap;

    @Override
    public void init() {
        apkHelperHashMap=new HashMap<>();
    }

    @Override
    public void load(String pluginName, String apkPath, String dexOutPath, Context context) {
        PluginApkHelper speedApkHelper = new PluginApkHelper(apkPath, dexOutPath, context);
        PluginManager.goActivity(context, pluginName, "create_context");
        apkHelperHashMap.put(pluginName, speedApkHelper);
    }

    @Override
    public PluginApkHelper get(String pluginName) {
        return apkHelperHashMap.get(pluginName);
    }

    @Override
    public void uninstall(String pluginName) {
        apkHelperHashMap.remove(pluginName);
    }
}
