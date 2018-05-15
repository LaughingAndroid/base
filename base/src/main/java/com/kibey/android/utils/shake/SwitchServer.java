package com.kibey.android.utils.shake;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.kibey.android.utils.APPConfig;
import com.kibey.android.utils.Logs;


/**
 * @author mchwind
 * @version V5.7.1
 * @since 16/10/27
 * 切换服务器
 */
public abstract class SwitchServer {
    public static void openSwitchServer(View vg, Context context) {
        if (Logs.IS_DEBUG && null != vg) {
            vg.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    APPConfig.getObject(SwitchServer.class).showServerDialog(context);
                }
            });
        }
    }

    public abstract void showServerDialog(Context context);
}
