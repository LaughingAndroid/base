package com.kibey.android.utils;

import android.content.Context;

/**
 * @author mchwind
 * @version V5.9
 * @since 17/3/6
 */
public abstract class AppProxy {

    static Context mApplication;

    public static void init(Context context) {
        if (context.getApplicationContext() == null) {
            mApplication = context;
        } else {
            mApplication = context.getApplicationContext();
        }
    }

    public static Context getApp() {
        if (null != mApplication && mApplication.getApplicationContext() != null) {
            return mApplication.getApplicationContext();
        }
        return mApplication.getApplicationContext();
    }
}
