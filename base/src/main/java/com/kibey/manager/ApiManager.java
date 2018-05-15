package com.kibey.manager;


import com.kibey.proxy.api.ApiProxy;
import com.kibey.android.lib.PluginInvocationHandler;

public class ApiManager implements ApiProxy {
    private static ApiManager instance = null;
    private ApiProxy mApiProxy;

    private ApiManager() {
        mApiProxy = (ApiProxy) new PluginInvocationHandler().bind(ApiProxy.class);
    }

    public static ApiManager getInstance() {
        if (instance == null) {
            synchronized (ApiManager.class) {
                if (instance == null) {
                    instance = new ApiManager();
                }
            }
        }
        return instance;
    }


    @Override
    public <T> T getApi(Class<T> service) {
        return mApiProxy.getApi(service);
    }
}
