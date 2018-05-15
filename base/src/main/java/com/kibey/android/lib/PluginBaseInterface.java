package com.kibey.android.lib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.kibey.android.app.IContext;
import com.kibey.android.ui.adapter.BaseRVAdapter;
import com.kibey.proxy.ui.IToolbar;

import java.util.List;

import rx.Observable;

/**
 * by liyihang
 * blog http://sijienet.com/
 */
public interface PluginBaseInterface {

    ViewGroup onCreate(Bundle savedInstanceState, IContext activity, String pluginName);

    void onDestroy();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onRestart();

    void onNewIntent(Intent intent);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    boolean onBackPressed();

    boolean onTouchEvent(MotionEvent event);

    void onSaveInstanceState(Bundle outState);

    void buildAdapterHolder(RecyclerView view, BaseRVAdapter adapter);

    Observable<List> loadData();

    int getToolbarFlags();

    void setupToolbar(IToolbar toolbar);

    boolean isTranslucentStatus();

    int getStatusBarColor();

    void setData(int page, List data);

    RecyclerView.LayoutManager buildLayoutManager();

    String contentLayoutRes();
}
