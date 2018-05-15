package com.kibey.manager;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kibey.proxy.ui.FragmentBuilderProxy;
import com.kibey.lib.PluginInvocationHandler;

public class FragmentBuilder implements FragmentBuilderProxy {
    private static FragmentBuilder instance = null;
    private FragmentBuilderProxy mFragmentBuilderProxy;

    private FragmentBuilder() {
        mFragmentBuilderProxy = (FragmentBuilderProxy) new PluginInvocationHandler().bind(FragmentBuilderProxy.class);
    }

    public static FragmentBuilder getInstance() {
        if (instance == null) {
            synchronized (FragmentBuilder.class) {
                if (instance == null) {
                    instance = new FragmentBuilder();
                }
            }
        }
        return instance;
    }

    @Override
    public Fragment create(Context context, String pluginName, String page, Bundle bundle) {
        return mFragmentBuilderProxy.create(context, pluginName, page, bundle);
    }


}
