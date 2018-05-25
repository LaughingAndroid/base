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
import com.kibey.android.data.model.SuperHolderData;
import com.kibey.android.ui.adapter.BaseRVAdapter;
import com.kibey.proxy.ui.IToolbar;

import java.util.List;

import rx.Observable;

/**
 * by liyihang
 * blog http://sijienet.com/
 */
public abstract class BasePlugin implements PluginBaseInterface, ILoadData {
    protected static String TAG;

    protected IContext mContext;
    private BaseRVAdapter mAdapter;

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
        ViewGroup view = (ViewGroup) PluginApkManager.inflate(mPluginName, contentLayoutRes(), null);
        onViewCreated(view);
        return view;
    }

    @Override
    public void buildAdapterHolder(RecyclerView view, BaseRVAdapter adapter) {
        mAdapter = adapter;
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
    public int contentLayoutRes() {
        return getLayoutId("layout_base_list");
    }

    public int getLayoutId(String name) {
        return PluginApkManager.getPluginApp(mPluginName).getResources().getIdentifier(name, "layout", mPluginName);
    }

    @Override
    public ILoadData getLoadDataImp() {
        return this;
    }

    /**
     * {@link ILoadData}
     *
     * @return
     */
    @Override
    public Observable<SuperHolderData> loadSuperHolderData() {
        return null;
    }

    @Override
    public Observable<List> loadData() {
        return null;
    }

    @Override
    public BaseRVAdapter getAdapter() {
        return mAdapter;
    }
}
