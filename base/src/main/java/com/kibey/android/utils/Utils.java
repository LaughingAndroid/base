package com.kibey.android.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * 常用工具类
 *
 * @author seven
 * @version V1.0
 * @since 16/6/2
 */
public final class Utils {

    private Utils() {
    }

    /**
     * 显示一个toast
     *
     * @param context Context
     * @param text    text resource
     */
    public static void toast(Context context, @StringRes int text) {
        if (null == context) {
            context = AppProxy.getApp();
        }
        Context finalContext = context;
        APPConfig.post(() -> Toast.makeText(finalContext, text, Toast.LENGTH_SHORT).show());
    }

    /**
     * 显示一个toast
     *
     * @param context Context
     * @param text    text CharSequence
     */
    public static void toast(Context context, CharSequence text) {
        if (null == context) {
            context = AppProxy.getApp();
        }
        Context finalContext = context;
        APPConfig.post(() -> Toast.makeText(finalContext, text, Toast.LENGTH_SHORT).show());
    }
}
