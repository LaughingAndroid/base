package com.kibey.lib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.kibey.android.app.IContext;

/**
 * by liyihang
 * blog http://sijienet.com/
 */
public class PluginHostBaseActivityInterfaceImp implements PluginHostBaseActivityInterface {

    private IContext context;
    private PluginApkHelper apkHelper;

    @Override
    public void init(IContext activity, String name) {
        apkHelper = PluginApkManager.getInstance().getHelper(name);
        this.context = activity;
        apkHelper.setContext(activity);
    }

    @Override
    public boolean isInit() {
        if (apkHelper != null)
            return true;
        return false;
    }

    public Class<?> getProxy(String pluginName, String classTag) {
        if (classTag == null) {
            classTag = PluginConfig.ROOT_CLASS_NAME;
        }
        Class<?> classById = apkHelper.getClassById(classTag);
        return classById;
    }

    @Override
    public PluginBaseInterface getBaserProxy(String pluginName, String classTag) {
        Class<?> proxy = getProxy(pluginName, classTag);
        PluginBaseInterface o = null;
        try {
            o = (PluginBaseInterface) proxy.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

    @Override
    public Resources getResources() {
        return apkHelper.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return apkHelper.getResources().getAssets();
    }

    @Override
    public ClassLoader getClassLoader() {
        return apkHelper.getDexClassLoader();
    }

    @Override
    public Resources.Theme getTheme() {
        return apkHelper.getTheme();
    }

    @Override
    public PackageInfo getPackageInfo() {
        return apkHelper.getPackageInfo();
    }

}
