package com.kibey.android.utils;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 状态栏工具类
 *
 * @author seven
 * @version V1.0
 * @since 2018/4/9
 */
public final class StatusBarUtils {

    public static void lightStatus(Window window) {
        if (SDKUtils.hasM()) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            View view = window.getDecorView();
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static void darkStatus(Window window) {
        if (SDKUtils.hasM()) {
            int flag = window.getDecorView().getSystemUiVisibility() & (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }

    public static void lightStatus(Window window, boolean on) {
        if (SDKUtils.hasM()) {
            int flag = window.getDecorView().getSystemUiVisibility();
            if (on) {
                flag |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flag &= (~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            window.getDecorView().setSystemUiVisibility(flag);
        }
    }
}
