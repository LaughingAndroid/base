package com.kibey.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.kibey.android.utils.AppProxy;
import com.kibey.android.lib.PluginApkManager;
import com.kibey.android.lib.PluginConfig;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/13
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public class PluginManager {
    public static rx.Observable<PluginAction> open(String url) {
        Context context = AppProxy.getApp();
        PluginAction action = PluginAction.Builder.build(url);
        if (action != null && null != action.getPluginApk()) {
            return action.getPluginApk().install()
                    .flatMap(flag -> open(context, flag, action))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return Observable.just(null);
    }

    private static Observable<PluginAction> open(Context context, boolean install, final PluginAction action) {
        if (!install) {
            return Observable.just(null);
        }
        return rx.Observable.create(new Observable.OnSubscribe<PluginAction>() {
            @Override
            public void call(Subscriber<? super PluginAction> subscriber) {
                PluginApk plugin = action.getPluginApk();
                switch (action.getType()) {
                    case PluginConfig.URI_TYPE_ACTIVITY:
                        goActivity(AppProxy.getApp(), plugin.getPackage_name(), action.page);
                        break;

                    case PluginConfig.URI_TYPE_FRAGMENT:
                        Fragment fragment = newFragment(context, plugin.getPackage_name(), action.page, action.bundle);
                        action.setResult(fragment);
                        break;

                    case PluginConfig.URI_TYPE_CLASS:
                        Class clazz = getClassId(plugin.getPackage_name(), action.page);
                        action.setResult(clazz);

                        break;

                    default:
                        break;
                }
                subscriber.onNext(action);
                subscriber.onCompleted();
            }
        });
    }

    public static void goActivity(Context context, String plugin, String className) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PluginConfig.ACTIVITY_URL));
        intent.setPackage(context.getPackageName());
        intent.putExtra(PluginConfig.KEY_PLUGIN, plugin);
        intent.putExtra(PluginConfig.PAGE, className);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static Fragment newFragment(Context context, String pluginName, String page, Bundle bundle) {
        return FragmentBuilder.getInstance().create(context, pluginName, page, bundle);
    }

    public static Class getClassId(String pluginName, String classTag) {
        return PluginApkManager.getInstance().getHelper(pluginName).getClassById(classTag);
    }
}
