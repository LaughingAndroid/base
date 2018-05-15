package com.kibey.android.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import rx.Observable;

/**
 * @author leiting
 * @des
 * @since 17/3/10
 */

public class ContextWrapper implements IContext {

    private IContext mInnerContext;

    public ContextWrapper(@NonNull IContext context) {
        this.mInnerContext = context;
    }

    @Override
    public boolean isDestroy() {
        return mInnerContext.isDestroy();
    }

    @Override
    public boolean isAdded() {
        return mInnerContext.isAdded();
    }

    @Override
    public Observable viewPrepare() {
        return mInnerContext.viewPrepare();
    }

    @Override
    public FragmentActivity getActivity() {
        return mInnerContext.getActivity();
    }

    @Override
    public Resources getResource() {
        return mInnerContext.getResource();
    }

    @Override
    public String getString(int resId) {
        return mInnerContext.getString(resId);
    }

    @Override
    public String getString(int resId, Object... formatArgs) {
        return mInnerContext.getString(resId, formatArgs);
    }

    @Override
    public <T extends View> T findView(View parent, @IdRes int id) {
        return mInnerContext.findView(parent, id);
    }

    @Override
    public void toast(@StringRes int text) {
        mInnerContext.toast(text);
    }

    @Override
    public void toast(CharSequence text) {
        mInnerContext.toast(text);
    }

    @Override
    public void showProgress(@StringRes int text) {
        mInnerContext.showProgress(text);
    }

    @Override
    public void showProgress(CharSequence text) {
        mInnerContext.showProgress(text);
    }

    @Override
    public void hideProgress() {
        mInnerContext.hideProgress();
    }

    @Override
    public void showActivity(Class<? extends Activity> activityClass) {
        mInnerContext.showActivity(activityClass);
    }

    @Override
    public void showActivity(Class<? extends Activity> activityClass, Bundle bundle) {
        mInnerContext.showActivity(activityClass, bundle);
    }

    @Override
    public FragmentManager getSupportFragmentManager() {
        return mInnerContext.getSupportFragmentManager();
    }

    @Override
    public void startActivity(Intent mIntent) {
        mInnerContext.startActivity(mIntent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        mInnerContext.startActivityForResult(intent, requestCode);
    }

    @Override
    public String getPackageName() {
        return mInnerContext.getPackageName();
    }

    @Override
    public void finish() {
        mInnerContext.finish();
    }

    @Override
    public boolean isResume() {
        return mInnerContext.isResume();
    }
}
