package com.kibey.android.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.kibey.android.utils.APPConfig;

import java.util.Collection;

import rx.Observable;

/**
 * Context 常用的工具类
 *
 * @author seven
 * @version V1.0
 * @since 16/5/26
 */
public class ContextHelper implements IContext {
    private ILifeCycle mLifeCycle;
    private FragmentActivity mActivity;

    private Toast mToast;

    private ProgressDialog mProgressDialog;
    private boolean progressDialogCancelable = true;

    public ContextHelper(@NonNull FragmentActivity activity, ILifeCycle lifeCycle) {
        this.mActivity = activity;
        this.mLifeCycle = lifeCycle;
    }

    /**
     * 获取Activity实例
     *
     * @return Activity实例
     */
    @Override
    public FragmentActivity getActivity() {
        return mActivity;
    }

    /**
     * 获取Resource实例
     *
     * @return Resource实例
     */
    @Override
    public Resources getResource() {
        return mActivity.getResources();
    }

    /**
     * 获取strings.xml下的string
     *
     * @param resId string id
     * @return string
     */
    @Override
    public String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * 获取strings.xml下的string
     *
     * @param resId      string id
     * @param formatArgs 格式化的参数
     * @return string
     */
    @Override
    public String getString(int resId, Object... formatArgs) {
        return getResource().getString(resId, formatArgs);
    }

    /**
     * 类似于{@link View#findViewById(int)}
     * 多了类型转换
     *
     * @param parent parent view
     * @param id     view id
     * @return view object
     */
    @Override
    public <T extends View> T findView(View parent, @IdRes int id) {
        if (null == parent) {
            return null;
        }
        return (T) parent.findViewById(id);
    }

    /**
     * 显示一个toast
     *
     * @param text text to be show
     */
    @Override
    public void toast(@StringRes int text) {
        if (getActivity() == null) return;
        toast(getString(text));
    }

    /**
     * 显示一个toast
     *
     * @param text text to be show
     */
    @Override
    public void toast(CharSequence text) {
        if (getActivity() == null) return;
        if (null == mToast) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * 显示一个带消息的进度框
     *
     * @param text text to be show
     */
    @Override
    public void showProgress(@StringRes int text) {
        try {
            if (getActivity() != null && getActivity().getWindow() != null && getActivity().getWindow().isActive()) {
                showProgress(getString(text));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示一个带消息的进度框
     *
     * @param text text to be show
     */
    @Override
    public void showProgress(CharSequence text) {
        try {
            APPConfig.post(new Runnable() {
                @Override
                public void run() {
                    if (getActivity() != null && getActivity().getWindow() != null && getActivity().getWindow().isActive()) {
                        if (null == mProgressDialog) {
                            mProgressDialog = ProgressDialog.show(getActivity(), null, text);
                            mProgressDialog.setCancelable(true);
                        } else {
                            mProgressDialog.setMessage(text);
                            mProgressDialog.show();
                        }
                        mProgressDialog.setCancelable(progressDialogCancelable);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideProgress() {
        if (getActivity() == null) return;
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 启动Activity
     *
     * @param activityClass Activity class
     */
    @Override
    public void showActivity(Class<? extends Activity> activityClass) {
        showActivity(activityClass, null);
    }

    /**
     * 启动Activity
     *
     * @param activityClass Activity class
     * @param bundle        Bundle
     */
    @Override
    public void showActivity(Class<? extends Activity> activityClass, Bundle bundle) {
        showActivity(activityClass, bundle, null);
    }

    @Override
    public void finish() {
        if (mActivity != null) {
            mActivity.finish();
        }
    }

    @Override
    public boolean isResume() {
        return mActivity instanceof IContext ? ((IContext) mActivity).isResume() : true;
    }

    @Override
    public Observable viewPrepare() {
        return mLifeCycle.viewPrepare();
    }

    @Override
    public void startActivity(Intent mIntent) {
        getActivity().startActivity(mIntent);
    }

    @Override
    public boolean isDestroy() {
        if (mLifeCycle == null) {
            return mLifeCycle.isDestroy();
        }
        return true;
    }

    @Override
    public boolean isAdded() {
        return null != mLifeCycle && mLifeCycle.isAdded();
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    /**
     * 启动Activity
     *
     * @param activityClass Activity class
     * @param bundle        Bundle
     * @param flags         intent flags like {@link Intent#FLAG_ACTIVITY_CLEAR_TASK}
     */
    public void showActivity(Class<? extends Activity> activityClass, Bundle bundle, int... flags) {
        Intent intent = new Intent(getActivity(), activityClass);
        if (null != flags) {
            for (int i = 0, N = flags.length; i < N; i++) {
                intent.addFlags(flags[i]);
            }
        }

        if (null != bundle) {
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
    }

    /**
     * 释放内存
     */
    public void onDestroy() {
        if (null != mProgressDialog) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        if (null != mToast) {
            mToast.cancel();
            mToast = null;
        }

        mActivity = null;
        mLifeCycle = null;
    }

    // =================================== 其他常用方法 ===================================

    /**
     * 判断字符串是否为空
     *
     * @param text 字符串
     * @return true为空，false不为空
     */
    public boolean isEmpty(CharSequence text) {
        return TextUtils.isEmpty(text);
    }

    /**
     * 判断字符串是否不为空
     *
     * @param text 字符串
     * @return true不为空，false为空
     */
    public boolean isNotEmpty(CharSequence text) {
        return !isEmpty(text);
    }

    /**
     * 判断集合是否为空
     *
     * @param c 集合
     * @return true为空，false不为空
     */
    public boolean isEmpty(Collection c) {
        return null == c || c.size() == 0;
    }

    /**
     * 判断集合是否不为空
     *
     * @param c 集合
     * @return true不为空，false为空
     */
    public boolean isNotEmpty(Collection c) {
        return null != c && c.size() > 0;
    }

    public void setProgressBarCancelable(boolean cancelable) {
        this.progressDialogCancelable = cancelable;
    }

    @Override
    public String getPackageName() {
        return mActivity.getPackageName();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        // 实际上用不到~ 都掉a、f自己的
        mActivity.startActivityForResult(intent, requestCode);
    }
}
