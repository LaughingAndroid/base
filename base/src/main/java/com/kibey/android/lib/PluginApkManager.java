package com.kibey.android.lib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kibey.android.utils.AppProxy;
import com.kibey.manager.PluginManager;

/**
 * by liyihang
 * blog http://sijienet.com/
 */
public class PluginApkManager {

    private static PluginApkManager instance = null;
    private PluginApkManagerInterface apkManagerInterface;

    private PluginApkManager() {
        apkManagerInterface = (PluginApkManagerInterface) new PluginInvocationHandler().bind(PluginApkManagerInterface.class);
        apkManagerInterface.init();
    }

    public static PluginApkManager getInstance() {
        if (instance == null) {
            synchronized (PluginApkManager.class) {
                if (instance == null) {
                    instance = new PluginApkManager();
                }
            }
        }
        return instance;
    }

    public void loadApk(String pluginName, String apkPath, String dexOutPath, Context context) {
        apkManagerInterface.load(pluginName, apkPath, dexOutPath, context);

    }

    public PluginApkHelper getHelper(String pluginName) {
        return apkManagerInterface.get(pluginName);
    }

    public boolean isLoaded(String pluginName) {
        PluginApkHelper helper = getHelper(pluginName);
        if (null == helper) {
            return false;
        }

        if (helper.getContext() == null) {
            PluginManager.goActivity(AppProxy.getApp(), pluginName, "create_context");
            return false;
        } else {
            return true;
        }
    }

    public void uninstall(String name) {
        apkManagerInterface.uninstall(name);
    }

    /**
     * 获取plugin app
     *
     * @param pluginName
     * @return
     */
    public static Context getPluginApp(String pluginName) {
        PluginApkHelper h = getInstance().getHelper(pluginName);
        return null == h ? AppProxy.getApp() : h.getContext();
    }

    public static View inflate(String pluginName, int id, ViewGroup parent) {
        return View.inflate(PluginApkManager.getPluginApp(pluginName), id, parent);
    }

}
