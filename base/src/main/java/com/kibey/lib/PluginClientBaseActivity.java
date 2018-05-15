package com.kibey.lib;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.kibey.plugin.R;
import com.kibey.android.app.IContext;
import com.kibey.android.ui.adapter.BaseRVAdapter;
import com.kibey.android.ui.adapter.SuperViewHolder;
import com.kibey.android.utils.shake.IRegisterDebugMethod;

import java.util.List;
import java.util.Map;

import rx.Observable;


/**
 * by liyihang
 * blog http://sijienet.com/
 */
public  abstract class PluginClientBaseActivity extends AppCompatActivity implements IContext, BaseRVAdapter.IHolderItemClick<SuperViewHolder>, IRegisterDebugMethod {
    private PluginBaseInterface proxyClass;
    private Bundle mSavedInstanceState;
    RecyclerView mRecyclerView;
    BaseRVAdapter mAdapter;
    private ViewGroup mContentView;

    public void buildAdapterHolder() {
        if (proxyClass != null) {
            proxyClass.buildAdapterHolder(mRecyclerView, mAdapter);
            Observable<List> ob = proxyClass.loadData();
            if (null != ob) {
                ob.subscribe(list -> {
                    setData(0, list);
                });
            }
        }
    }

    public void setData(int page, List list) {
        mAdapter.setData(list);
        if (proxyClass != null) {
            proxyClass.setData(page, list);
        }
    }

    public Observable<List> loadData() {
        Observable<List> ob = proxyClass.loadData();
        if (ob == null) {
            return Observable.empty();
        }
        return ob;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new PluginLayoutInflaterFactory());
        super.onCreate(savedInstanceState);
        getIntentParm();
        mSavedInstanceState = savedInstanceState;
        if (proxyClass == null) {
            finish();
            return;
        }
        mContentView = proxyClass.onCreate(mSavedInstanceState, this, pluginName());
        setContentView(mContentView);
        mRecyclerView = (RecyclerView) findViewById(R.id.irv);
        mRecyclerView.setHorizontalFadingEdgeEnabled(false);
        mRecyclerView.setMotionEventSplittingEnabled(false);

        RecyclerView.LayoutManager layoutManager = buildLayoutManager();
        if (null != layoutManager) {
            mRecyclerView.setLayoutManager(layoutManager);
        }
        mAdapter = new BaseRVAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        buildAdapterHolder();
    }

    protected abstract String pluginName();

    private RecyclerView.LayoutManager buildLayoutManager() {
        return proxyClass.buildLayoutManager();
    }


    private void getIntentParm() {
        try {
            String classTag = getIntent().getStringExtra(PluginConfig.PAGE);
            PackageInfo packageInfo2 = PluginUtils.getPackageInfo2(this, getPackageName());
            String className = packageInfo2.applicationInfo.metaData.getString(classTag);
            if (null == className) {
                className = classTag;
            }
            proxyClass = (PluginBaseInterface) getClassLoader().loadClass(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (proxyClass == null)
            proxyClass = getProxyBase();
    }

    public abstract PluginBaseInterface getProxyBase();


    protected int getToolbarFlags() {
        return proxyClass.getToolbarFlags();
    }

    protected void setupToolbar() {
    }


    @Override
    public void onDestroy() {
        if (proxyClass != null) {
            proxyClass.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (proxyClass != null) {
            proxyClass.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (proxyClass != null) {
            proxyClass.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (proxyClass != null) {
            proxyClass.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (proxyClass != null) {
            proxyClass.onStop();
        }
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (proxyClass != null) {
            proxyClass.onRestart();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (proxyClass != null) {
            proxyClass.onNewIntent(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (proxyClass != null) {
            proxyClass.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (proxyClass != null) {
            proxyClass.onBackPressed();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = null != proxyClass && proxyClass.onTouchEvent(event);
        if (b) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (proxyClass != null) {
            proxyClass.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onItemClick(SuperViewHolder superViewHolder) {
        if (proxyClass instanceof BaseRVAdapter.IHolderItemClick) {
            ((BaseRVAdapter.IHolderItemClick) proxyClass).onItemClick(superViewHolder);
        }
    }

    @Override
    public void registerDebugMethod(Map<String, Object> map) {
        if (proxyClass instanceof IRegisterDebugMethod) {
            ((IRegisterDebugMethod) proxyClass).registerDebugMethod(map);
        }
    }

    @Override
    public FragmentActivity getActivity() {
        return this;
    }

    @Override
    public Resources getResource() {
        return getResources();
    }

    @Override
    public <T extends View> T findView(View parent, int id) {
        return null;
    }

    @Override
    public void toast(int text) {

    }

    @Override
    public void toast(CharSequence text) {

    }

    @Override
    public void showProgress(int text) {

    }

    @Override
    public void showProgress(CharSequence text) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showActivity(Class<? extends Activity> activityClass) {

    }

    @Override
    public void showActivity(Class<? extends Activity> activityClass, Bundle bundle) {

    }

    @Override
    public boolean isResume() {
        return false;
    }

    @Override
    public boolean isDestroy() {
        return false;
    }

    @Override
    public boolean isAdded() {
        return false;
    }

    @Override
    public Observable viewPrepare() {
        return null;
    }
}
