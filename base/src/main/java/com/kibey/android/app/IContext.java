package com.kibey.android.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Context 常用工具方法
 *
 * @author seven
 * @version V1.0
 * @since 16/5/26
 */
public interface IContext extends ILifeCycle {

    /**
     * 获取Activity实例
     *
     * @return Activity实例
     */
    FragmentActivity getActivity();

    /**
     * 获取Resource实例
     *
     * @return Resource实例
     */
    Resources getResource();

    /**
     * 获取strings.xml下的string
     *
     * @param resId string id
     * @return string
     */
    String getString(int resId);

    /**
     * 获取strings.xml下的string
     *
     * @param resId      string id
     * @param formatArgs 格式化的参数
     * @return string
     */
    String getString(int resId, Object... formatArgs);

    /**
     * Convenient method to find view by id
     *
     * @param parent parent view
     * @param id     view id
     * @param <T>    view class
     * @return view object
     */
    <T extends View> T findView(View parent, @IdRes int id);

    /**
     * 显示一个toast
     *
     * @param text text to be show
     */
    void toast(@StringRes int text);

    /**
     * 显示一个toast
     *
     * @param text text to be show
     */
    void toast(CharSequence text);

    /**
     * 显示一个带消息的进度框
     *
     * @param text text to be show
     */
    void showProgress(@StringRes int text);

    /**
     * 显示一个带消息的进度框
     *
     * @param text text to be show
     */
    void showProgress(CharSequence text);

    /**
     *
     */
    void hideProgress();

    /**
     * 启动Activity
     *
     * @param activityClass Activity class
     */
    void showActivity(Class<? extends Activity> activityClass);

    /**
     * 启动Activity
     *
     * @param activityClass Activity class
     * @param bundle        Bundle
     */
    void showActivity(Class<? extends Activity> activityClass, Bundle bundle);

    /**
     * 获取support fragment manager
     *
     * @return
     */
    FragmentManager getSupportFragmentManager();

    void startActivity(Intent mIntent);

    void startActivityForResult(Intent intent, int requestCode);
    String getPackageName();
    void finish();
    boolean isResume();
}
