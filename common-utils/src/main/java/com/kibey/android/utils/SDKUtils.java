package com.kibey.android.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

/**
 * 版本判断工具类
 * @author seven
 * @since 15/12/24
 */
public final class SDKUtils {

    private SDKUtils() {}

    /**
     * sdk 16
     * @return
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * sdk 19
     * @return
     */
    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * sdk 21
     * @return
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * sdk 23
     * @return
     */
    public static boolean hasM() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }



    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasGingerbreadMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean hasJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static boolean isGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1;
    }
}
