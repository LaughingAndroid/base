package com.kibey.lib;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kibey.android.app.IContext;
import com.kibey.proxy.ui.IToolbar;

import java.util.List;

/**
 * by liyihang
 * blog http://sijienet.com/
 */
public abstract class BasePlugin implements PluginBaseInterface {
    protected static String TAG;

    protected IContext mContext;

    protected IContext getContext() {
        return mContext;
    }

    public FragmentActivity getActivity() {
        return mContext.getActivity();
    }

    protected String mPluginName;

    @Override
    public ViewGroup onCreate(Bundle savedInstanceState, IContext activity, String pluginName) {
        TAG = getClass().getName();
        this.mContext = activity;
        this.mPluginName = pluginName;
        ViewGroup view = (ViewGroup) PluginApkManager.inflate(mPluginName, getLayoutId(contentLayoutRes()), null);
        onViewCreated(view);
        return view;
    }

    public void onViewCreated(View view) {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public boolean onBackPressed() {

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public int getToolbarFlags() {
        return IToolbar.FLAG_NONE;
    }

    @Override
    public void setupToolbar(IToolbar toolbar) {

    }

    @Override
    public int getStatusBarColor() {
        return Color.WHITE;
    }

    @Override
    public void setData(int page, List data) {

    }

    @Override
    public RecyclerView.LayoutManager buildLayoutManager() {
        return new LinearLayoutManager(getActivity());
    }

    @Override
    public boolean isTranslucentStatus() {
        return true;
    }

    @Override
    public String contentLayoutRes() {
        return "layout_base_list";
    }

    public int getLayoutId(String name) {
        return PluginApkManager.getPluginApp(mPluginName).getResources().getIdentifier(name, "layout", mPluginName);
    }
}
