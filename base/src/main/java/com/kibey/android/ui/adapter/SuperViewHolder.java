package com.kibey.android.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kibey.android.app.IContext;
import com.kibey.android.utils.APPConfig;
import com.kibey.android.utils.AppProxy;
import com.kibey.android.utils.Logs;
import com.kibey.manager.ImageManager;
import com.kibey.proxy.image.ImgLoadListener;
import com.kibey.lib.PluginApkManager;

import java.lang.reflect.Constructor;

import butterknife.ButterKnife;

import static com.kibey.android.utils.Logs.timeConsuming;
import static java.lang.System.currentTimeMillis;

/**
 * 推荐子类这么写
 * ==== public static class TestHolder extends SuperViewHolder<HomeData> {  ====
 * ====                                                                     ====
 * ====   public TestHolder() {                                             ====
 * ====       super();                                                      ====
 * ====   }
 * ====
 * ====   public TestHolder(ViewGroup parent, int layoutId) {               ====
 * ====       super(parent, layoutId);                                      ====
 * ====   }                                                                 ====
 * ====                                                                     ====
 * ====   @Override                                                         ====
 * ====   protected int contentLayoutRes() {                                ====
 * ====       return R.layout.item_home_radio_music_mv_bell_rank;           ====
 * ====   }                                                                 ====
 * ==== }                                                                   ====
 *
 * @param <DATA>
 */
public class SuperViewHolder<DATA> extends RecyclerView.ViewHolder implements IHolder<DATA>, BaseRVAdapter.IHolderCreator {
    public static final Context APP = AppProxy.getApp();
    private ViewGroup EMPTY;

    protected DATA data;
    protected String mVolleyTag = getClass().getName();
    protected IContext mContext;
    private SparseArray<View> mViews = new SparseArray<>();
    private int mAdapterSize;
    private IHolderBuilder mHolderBuilder;

    protected SuperViewHolder() {
        super(new LinearLayout(AppProxy.getApp()));
        EMPTY = (ViewGroup) itemView;
    }

    public SuperViewHolder(ViewGroup parent, int layoutId) {
        this(inflate(layoutId, parent));
    }

    public SuperViewHolder(Context context, int layoutId, ViewGroup parent) {
        this(inflate(context, layoutId, parent));
    }

    public SuperViewHolder(View itemView) {
        super(itemView);
        if (itemView != EMPTY) {
            ButterKnife.bind(this, itemView);
        }
    }

    public DATA getData() {
        return data;
    }

    public void setData(DATA data) {
        this.data = data;
    }

    @Override
    public View getView() {
        return itemView;
    }

    /**
     * 这里Model-VH UI进行绑定，设置UI
     */
    public void onBindViewHolder() {
        if (mHolderBuilder != null) {
            mHolderBuilder.bindData(this, getData());
        }
    }

    /**
     * 这个方法绑定IContext,可以设置callback
     *
     * @param context
     */
    public void onAttach(IContext context) {
        itemView.setTag(this);
        mContext = context;

        if (context instanceof BaseRVAdapter.IHolderItemClick) {
            itemView.setOnClickListener(v -> ((BaseRVAdapter.IHolderItemClick) context).onItemClick(this));
        } else if (null != mHolderItemClick) {
            itemView.setOnClickListener(v -> mHolderItemClick.onItemClick(this));
        }
    }

    BaseRVAdapter.IHolderItemClick mHolderItemClick;

    public void setHolderItemClick(BaseRVAdapter.IHolderItemClick holderItemClick) {
        mHolderItemClick = holderItemClick;
        itemView.setOnClickListener(v -> mHolderItemClick.onItemClick(this));
    }

    public boolean isAttach() {
        return mContext != null;
    }

    /**
     * 清理内存
     * 这里清理的数据,要在onAttach生成
     */
    @Override
    public void clear() {
        if (itemView != null) {
            itemView.setTag(null);
        }
        mContext = null;
    }


    public void setAdapterSize(int adapterSize) {
        mAdapterSize = adapterSize;
    }

    public int getAdapterSize() {
        return mAdapterSize;
    }

    @Override
    public SuperViewHolder createHolder(ViewGroup parent) {
        try {
            if (contentLayoutRes() != 0) {
                Class clz = getClass();

                if (isPluginHolder()) {
                    Constructor c1 = clz.getDeclaredConstructor(Context.class, int.class, ViewGroup.class);
                    Context context = PluginApkManager.getPluginApp(getPluginName());
                    SuperViewHolder instance = (SuperViewHolder) c1.newInstance(context, contentLayoutRes(), parent);
                    return instance;
                } else {
                    Constructor c1 = clz.getDeclaredConstructor(ViewGroup.class, int.class);
                    SuperViewHolder instance = (SuperViewHolder) c1.newInstance(parent, contentLayoutRes());
                    return instance;
                }
            } else {
                Class clz = getClass();
                Constructor c1 = clz.getDeclaredConstructor(ViewGroup.class);
                SuperViewHolder instance = (SuperViewHolder) c1.newInstance(parent);
                return instance;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logs.e(mVolleyTag + " 创建失败 " + e + getClass());
            if (Logs.IS_DEBUG) {
                Logs.printCallStatck(mVolleyTag + " 创建失败 ");
            }
        }
        return null;
    }

    protected boolean isPluginHolder() {
        return false;
    }

    protected String getPluginName() {
        return null;
    }

    protected int contentLayoutRes() {
        return 0;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            if (null == view) return null;
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return itemView;
    }

    /****以下为辅助方法*****/

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public SuperViewHolder setText(int viewId, Object text) {
        TextView tv = findViewById(viewId);
        if (text instanceof Integer) {

            tv.setText((Integer) text);
        } else if (text instanceof CharSequence) {
            tv.setText((CharSequence) text);
        }
        return this;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public SuperViewHolder setText(int viewId, int text, Object... objects) {
        TextView tv = findViewById(viewId);
        tv.setText(getString(text, objects));
        return this;
    }

    public SuperViewHolder setImageResource(int viewId, int resId) {
        ImageView view = findViewById(viewId);
        view.setImageResource(resId);
        return this;
    }

    public SuperViewHolder setImageUrl(int viewId, String url, ImgLoadListener listener) {
        ImageView view = findViewById(viewId);
        ImageManager.getInstance().loadImage(url, view, listener);
        return this;
    }

    public SuperViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = findViewById(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public SuperViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = findViewById(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public SuperViewHolder setBackgroundColor(int viewId, int color) {
        View view = findViewById(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public SuperViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = findViewById(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public SuperViewHolder setTextColor(int viewId, int textColor) {
        TextView view = findViewById(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public SuperViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = findViewById(viewId);
        view.setTextColor(mContext.getActivity().getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public SuperViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            findViewById(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            findViewById(viewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Helper class for {@link View#setVisibility(int)}
     *
     * @param visibility visibility
     * @param views      View array
     */
    public static void setVisibility(int visibility, View... views) {
        for (View v : views) {
            v.setVisibility(visibility);
        }
    }

    public SuperViewHolder setVisible(int viewId, boolean visible) {
        View view = findViewById(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public SuperViewHolder linkify(int viewId) {
        TextView view = findViewById(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public SuperViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = findViewById(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public SuperViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = findViewById(viewId);
        view.setProgress(progress);
        return this;
    }

    public SuperViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = findViewById(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public SuperViewHolder setMax(int viewId, int max) {
        ProgressBar view = findViewById(viewId);
        view.setMax(max);
        return this;
    }

    public SuperViewHolder setRating(int viewId, float rating) {
        RatingBar view = findViewById(viewId);
        view.setRating(rating);
        return this;
    }

    public SuperViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = findViewById(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public SuperViewHolder setTag(int viewId, Object tag) {
        View view = findViewById(viewId);
        view.setTag(tag);
        return this;
    }

    public SuperViewHolder setTag(int viewId, int key, Object tag) {
        View view = findViewById(viewId);
        view.setTag(key, tag);
        return this;
    }

    public SuperViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = findViewById(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public SuperViewHolder setOnClickListener(int viewId,
                                              View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public SuperViewHolder setOnTouchListener(int viewId,
                                              View.OnTouchListener listener) {
        View view = findViewById(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public SuperViewHolder setOnLongClickListener(int viewId,
                                                  View.OnLongClickListener listener) {
        View view = findViewById(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    //              //
    // tools method //
    //              //
    public static View inflate(int layoutId) {
        return LayoutInflater.from(APP).inflate(layoutId, null, false);
    }

    public static View inflate(int layoutId, ViewGroup parent) {
        long start = currentTimeMillis();
        Context context = APPConfig.getFirstActivity();
        if (context == null) {
            context = AppProxy.getApp();
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        timeConsuming("inflate " + context.getResources().getResourceName(layoutId), start);
        return view;
    }

    public static View inflate(Context context, int layoutId, ViewGroup parent) {
        if (null == context) {
            context = AppProxy.getApp();
        }
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    public static String getString(int id, Object... args) {
        return AppProxy.getApp().getString(id, args);
    }

    public static String getString(int id) {
        return AppProxy.getApp().getString(id);
    }

    public void setHolderBuilder(IHolderBuilder iHolderBuilder) {
        this.mHolderBuilder = iHolderBuilder;
    }

    public void refresh() {
        setData(data);
    }

    public void show() {
        itemView.setVisibility(View.VISIBLE);
    }

    public void hide() {
        itemView.setVisibility(View.GONE);
    }

    public void invisible() {
        itemView.setVisibility(View.INVISIBLE);
    }
}
