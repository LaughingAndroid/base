package com.kibey.android.lib;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.kibey.android.app.IContext;

/**
 *  by liyihang
 *  blog http://sijienet.com/
 */
public class PluginHostActivityHelper {

    PluginHostBaseActivityInterface hostBaseActivityInterface;

    public PluginHostActivityHelper(IContext context, String name) {
        hostBaseActivityInterface= (PluginHostBaseActivityInterface) new PluginInvocationHandler().bind(PluginHostBaseActivityInterface.class);
        hostBaseActivityInterface.init(context,name);
    }

    public boolean isInit(){
        return hostBaseActivityInterface.isInit();
    }

    public PluginBaseInterface getBaserProxy(String pluginName, String classTag){
        return hostBaseActivityInterface.getBaserProxy(pluginName, classTag);
    }

    public Resources getResources(){
        return hostBaseActivityInterface.getResources();
    }

    public AssetManager getAssets() {
        return hostBaseActivityInterface.getAssets();
    }

    public ClassLoader getClassLoader() {
        return hostBaseActivityInterface.getClassLoader();
    }

    public Resources.Theme getTheme(){
        return hostBaseActivityInterface.getTheme();
    }

    public PackageInfo getPackageInfo(){
        return hostBaseActivityInterface.getPackageInfo();
    }


}
