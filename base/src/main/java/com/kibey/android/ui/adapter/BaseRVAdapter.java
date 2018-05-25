package com.kibey.android.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kibey.android.app.IContext;
import com.kibey.android.data.model.Model;
import com.kibey.android.utils.AppProxy;
import com.kibey.android.utils.DialogUtils;
import com.kibey.android.utils.IClear;
import com.kibey.android.utils.ListUtils;
import com.kibey.android.utils.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.kibey.android.utils.Logs.timeConsuming;
import static java.lang.System.currentTimeMillis;

/**
 * Author: xl
 * Date: 16/4/19
 */
public class BaseRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    /**
     * 最大Data数量,这个是终极限制,{@link #setMaxCount}的方法数值的上限
     */
    private static final int MAX_COUNT = 1000 * 1000;
    private String TAG = getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());

    protected IContext mEchoContext;
    private HolderBuilderUtils mCurrentBuilder;
    private ViewTypeProvider mViewTypeProvider;
    private List<SuperViewHolder> mCacheHolders = new ArrayList<>();

    /**
     * 默认状态,不做任何滑动
     */
    public static final int SMOOTH_SCROLL_TO_DEFAULT = 0;
    /**
     * 滑动到第一个,
     * 这里可以配合{@link android.support.v7.widget.LinearLayoutManager#setReverseLayout(boolean)}达到一些特殊效果
     */
    public static final int SMOOTH_SCROLL_TO_FIRST = 1;
    /**
     * 滑动到最后一个,
     * 这里可以配合{@link android.support.v7.widget.LinearLayoutManager#setReverseLayout(boolean)}达到一些特殊效果
     */
    public static final int SMOOTH_SCROLL_TO_LAST = 2;

    private int mSmoothScrollType = SMOOTH_SCROLL_TO_DEFAULT;

    private RecyclerView mRecyclerView;

    protected List<Object> mData;
    private int mSize;
    private boolean isRecycleOnCallback = false;

    private OnCreateViewHolderListener mOnCreateViewHolderListener;

    /**
     * 设置OnCreateViewHolder监听
     *
     * @param listener {@link OnCreateViewHolderListener}
     */
    public void setOnCreateViewHolderListener(OnCreateViewHolderListener listener) {
        mOnCreateViewHolderListener = listener;
    }

    /**
     * 设置是否在{@link #onViewRecycled(RecyclerView.ViewHolder)}clear
     * 目前就一个地方用到,一般情况不要用这个,当未知原因{@link #onCreateViewHolder(ViewGroup, int)}频繁调用的时候可以开启
     * ps:开启这个最好onAttach不要做很重的任务,消耗比较大
     *
     * @param recycleOnCallback
     */
    public void setRecycleOnCallback(boolean recycleOnCallback) {
        isRecycleOnCallback = recycleOnCallback;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public BaseRVAdapter(IContext context) {
        mData = new ArrayList<>();
        mEchoContext = context;
    }

    public void setHolderBuilder(HolderBuilderUtils currentBuilder) {
        mCurrentBuilder = currentBuilder;
    }

    /**
     * @param model
     * @param vh
     * @return
     */
    public BaseRVAdapter build(String model, IHolderCreator vh) {
        ensureUtils();
        mCurrentBuilder.add(model, vh);
        return this;
    }

    /**
     * @param clz
     * @param vh
     * @return
     */
    public BaseRVAdapter build(Class clz, IHolderCreator vh) {
        return build(clz.getName(), vh);
    }

    /**
     * @param clz
     * @param vh
     * @return
     */
    public BaseRVAdapter build(Class clz, Class<? extends IHolderCreator> vh) {
        return build(clz.getName(), vh);
    }

    /**
     * @param model
     * @param vh
     * @return
     */
    public BaseRVAdapter build(String model, Class<? extends IHolderCreator> vh) {
        try {
            return build(model, vh.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            if (Logs.IS_DEBUG) {
                Logs.printCallStatck(vh + " BaseRvAdapter build失败 ");
                DialogUtils.show(vh.getSimpleName() + " build失败,检查publish和构造函数 \n" + e.toString(), SweetAlertDialog.ERROR_TYPE);
            }
        }
        return null;
    }

    public void addHolderBuilder(IHolderBuilder... builders) {
        ensureUtils();
        for (IHolderBuilder builder : builders) {
            mCurrentBuilder.add(builder.getKey(), builder);
        }
    }

    /**
     * 设置自动滑动
     *
     * @param type 参考{@link #SMOOTH_SCROLL_TO_DEFAULT},{@link #SMOOTH_SCROLL_TO_FIRST},{@link #SMOOTH_SCROLL_TO_LAST}
     */
    public void setSmoothScroll(int type) {
        mSmoothScrollType = type;
    }

    /**
     * 需要清除以前的数据
     *
     * @param list
     */
    public void setData(List list) {
        if (null == list) {
            mData.clear();
            notifyDataSetChanged();
            return;
        }
        if (mData != list) { // bug fix
            mData.clear();
            mData.addAll(list);
        }
        checkMaxIndex(getMaxCount());
        notifyDataSetChanged();
        log("setdata " + ListUtils.sizeOf(list) + " size=" + getItemCount());
    }

    /**
     * 设置数据 不刷新
     *
     * @param list
     */
    public void setDataNotNotifyDataSetChange(List list) {
        if (null == list) return;
        mData.clear();
        mData.addAll(list);
        log("setdata " + ListUtils.sizeOf(list) + " size=" + getItemCount());
    }


    /**
     * 刷新adapter
     */
    public void notifyDataSetChangedInRunnable() {
        RecyclerView v = getRecyclerView();
        if (null != v && v.isComputingLayout()) {
            // 避免出现Cannot invalidate item decorations during a scroll or layout 错误
            v.postDelayed(() -> notifyDataSetChangedInRunnable(), 100);
        } else {
            notifyDataSetChanged();
        }
    }

    private boolean checkMaxIndex(int max) {
        int size = getItemCount();

        if (size > max) {
            List temp = mData.subList(size - max, size);
            mData = temp;
            return true;
        }
        return false;
    }


    private int mMaxCount = MAX_COUNT;

    /**
     * 可以设置Adapter最多可以保存多少条数据
     *
     * @param maxCount 最大值,1表示1个,0表示没有,最小值0
     * @return this
     */
    public BaseRVAdapter setMaxCount(int maxCount) {
        // 如果maxCount是负数或者比MAX_COUNT还大,则取MAX_COUNT
        // 不能小于0,不能大于MAX_COUNT,否则取MAX_COUNT
        mMaxCount = maxCount < 0 ? MAX_COUNT : (Math.max(MAX_COUNT, maxCount));
        return this;
    }

    /**
     * 最大Item数量
     *
     * @return 数量
     */
    public int getMaxCount() {
        return mMaxCount;
    }


    /**
     * 添加到列表后面
     *
     * @param list
     */
    public void addData(List list) {
        addData(list, true);
    }

    /**
     * 添加数据
     *
     * @param list   数据
     * @param notify 是否刷新列表
     */
    public void addData(List list, boolean notify) {
        if (ListUtils.isEmpty(list)) return;
        mData.addAll(list);
        notifyDataSetChanged();
    }


    public void add(Object data) {
        mData.add(data);
        if (checkMaxIndex(getMaxCount())) {
            notifyDataSetChanged();
        } else {
            notifyDataSetChangedInRunnable();
        }
    }

    public void removeAll() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void remove(Object item) {
        int index = mData.indexOf(item);
        remove(index);
    }

    public void remove(int index) {
        if (index < 0) {
            return;
        }
        mData.remove(index);
        notifyItemRemoved(index);
        smoothScroll();
    }

    /**
     * 注意,notifyItem相关的方法都是final不能重写,所以这里所有调用notifyData更新的地方都需要调用这个方法.
     */
    public void smoothScroll() {
        switch (mSmoothScrollType) {
            case SMOOTH_SCROLL_TO_DEFAULT:
//              mRecyclerView.smoothScrollToPosition(position);
                break;
            case SMOOTH_SCROLL_TO_FIRST:
                mRecyclerView.smoothScrollToPosition(0);
                break;
            case SMOOTH_SCROLL_TO_LAST:
                mRecyclerView.smoothScrollToPosition(getItemCount());
                break;
            default:
                break;
        }
    }

    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        if (mViewTypeProvider != null) {
            String viewType = mViewTypeProvider.getItemType(getItem(position), position);
            if (!TextUtils.isEmpty(viewType)) {
                return mCurrentBuilder.getViewType(viewType);
            }
        }
        Object item = getItem(position);
        if (null != mCurrentBuilder) {
            int type = mCurrentBuilder.getViewType(item);
            if (HolderBuilderUtils.NO_TYPE != type) {
                return type;
            }
        }
        return HolderBuilderUtils.NO_TYPE;
    }

    /**
     * 创建ViewHolder
     *
     * @param parent   父容器
     * @param viewType View类型
     * @return ViewHolder
     */
    @Override
    public SuperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ensureUtils();
        Object creator = getCurrentBuilderUtils().getClz(viewType);
        long start = currentTimeMillis();
        SuperViewHolder holder = createVH(creator, parent);
        timeConsuming(TAG + " " + holder.getClass().getSimpleName() + " createVH", start);
        if (null != mOnCreateViewHolderListener) {
            mOnCreateViewHolderListener.onCreateViewHolder(holder);
        }
        return holder;
    }

    private SuperViewHolder createVH(Object creator, ViewGroup parent) {
        if (null == creator) {
            return new EmptyHolder(creator);
        }

        if (creator instanceof IHolderBuilder) {
            ((IHolderBuilder) creator).setHolderBuilder(getCurrentBuilderUtils());
        }

        if (creator instanceof IHolderCreator) {
            SuperViewHolder vh = ((IHolderCreator) creator).createHolder(parent);
            if (null != vh) {
                return vh;
            }
        }

        return new EmptyHolder(creator);
    }

    private HolderBuilderUtils getCurrentBuilderUtils() {
        ensureUtils();
        return mCurrentBuilder;
    }

    /**
     * 绑定数据到ViewHolder上,会在每次ViewHolder可见时调用
     * 注意:这里实际测试,有时候会出现cast类型异常,暂时不清楚原因
     *
     * @param holder   ViewHolder
     * @param position position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SuperViewHolder) {
            SuperViewHolder bv = (SuperViewHolder) holder;
            if (!bv.isAttach()) {
                bv.onAttach(mEchoContext);
            }
            bv.setAdapterSize(mSize);
            long start = currentTimeMillis();
            bv.setData(getItem(position));
            timeConsuming(TAG + " " + holder.getClass().getSimpleName() + " _onBindViewHolder", start);
            bv.setAdapterSize(mSize);
            bv.onBindViewHolder();
            if (mCacheHolders != null && !mCacheHolders.contains(bv)) {
                mCacheHolders.add(bv);
            }
        }

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (isRecycleOnCallback) {
            mCacheHolders.remove(holder);
            if (holder instanceof IClear) {
                ((IClear) holder).clear();
            }
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public int getItemCount() {
        mSize = ListUtils.sizeOf(mData);
        return mSize;
    }

    private <T> T newHolder(Class<T> clz) {
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        if (mData != null) {
            mData.clear();
        }
        clearCacheHolder();
        if (mCurrentBuilder != null) {
            mCurrentBuilder.clear();
        }
        mCurrentBuilder = null;
        mViewTypeProvider = null;
        mEchoContext = null;
    }

    public void clearCacheHolder() {
        if (mCacheHolders == null) return;
        for (SuperViewHolder holder : mCacheHolders) {
            holder.clear();
        }
        mCacheHolders.clear();
    }

    public List getData() {
        return mData;
    }

    public void setViewTypeProvider(ViewTypeProvider viewTypeProvider) {
        mViewTypeProvider = viewTypeProvider;
    }

    public void clearBuildHolder() {
        getCurrentBuilderUtils().clear();
    }

    public static class BaseViewHolder<DATA> extends SuperViewHolder<DATA> {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        public BaseViewHolder() {
            super();
        }

        public BaseViewHolder(ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        public BaseViewHolder(Context context, int layoutId, ViewGroup parent) {
            super(context, layoutId, parent);
        }
    }


    public <T extends SuperViewHolder> List<T> getHolders() {
        return getHolders(getRecyclerView());
    }

    public static <T extends SuperViewHolder> List<T> getHolders(ViewGroup vg) {
        List<T> list = new ArrayList<>();
        int size = vg.getChildCount();
        for (int i = 0; i < size; i++) {
            try {
                T temp = (T) vg.getChildAt(i).getTag();
                list.add(temp);
            } catch (Exception e) {
                Logs.e("Don't worry, " + e.toString());
            }
        }
        return list;
    }


    public static class HolderBuilderUtils {
        public static final int NO_TYPE = -1;
        public final HashMap<Integer, Object> VIEW_HOLDER = new HashMap<>();
        public final HashMap<String, Integer> VIEW_HOLDER_TYPE = new HashMap<>();
        public final HashMap<Object, ArrayList<SuperViewHolder>> mHolderCache = new HashMap<>();
        int type = Integer.MAX_VALUE / 2;

        public void add(String model, Object vh) {
            VIEW_HOLDER_TYPE.put(model, model.hashCode());
            VIEW_HOLDER.put(VIEW_HOLDER_TYPE.get(model), vh);
        }

        public SuperViewHolder createVH(Object creator, ViewGroup parent) {
            if (creator instanceof IHolderBuilder) {
                ((IHolderBuilder) creator).setHolderBuilder(this);
            }

            if (creator instanceof BaseRVAdapter.IHolderCreator) {
                SuperViewHolder vh = fromCache(creator);
                if (vh == null) {
                    vh = ((BaseRVAdapter.IHolderCreator) creator).createHolder(parent);
                    ArrayList<SuperViewHolder> list;
                    if (!mHolderCache.containsKey(creator)) {
                        list = new ArrayList<>();
                        mHolderCache.put(creator, list);
                    } else {
                        list = mHolderCache.get(creator);
                    }
                    list.add(vh);
                }

                if (null != vh) {
                    return vh;
                }
            }
            return new BaseRVAdapter.EmptyHolder(creator);
        }

        private SuperViewHolder fromCache(Object creator) {
            if (mHolderCache.containsKey(creator)) {
                ArrayList<SuperViewHolder> list = mHolderCache.get(creator);
                for (SuperViewHolder o : list) {
                    if (o.getView().getParent() == null) {
                        Logs.d("from cache " + o.getClass().getSimpleName());
                        return o;
                    }
                }
            }
            return null;
        }

        public Object getClz(int viewType) {
            return VIEW_HOLDER.get(viewType);
        }

        public Integer getViewType(Object item) {
            Integer type = NO_TYPE;
            if (item instanceof Model) {
                if (!VIEW_HOLDER_TYPE.containsKey(((Model) item).getEchoViewType())) {
                    type = NO_TYPE;
                } else {
                    type = VIEW_HOLDER_TYPE.get(((Model) item).getEchoViewType());
                }
            } else if (null != item) {
                type = VIEW_HOLDER_TYPE.get(item.getClass().getName());
            }
            if (null == type) {
                type = NO_TYPE;
            }
            return type;
        }

        public Integer getViewType(String viewType) {
            Integer type;
            if (!VIEW_HOLDER_TYPE.containsKey(viewType)) {
                type = NO_TYPE;
            } else {
                type = VIEW_HOLDER_TYPE.get(viewType);
            }
            return type;
        }

        public void clear() {
            VIEW_HOLDER.clear();
            VIEW_HOLDER_TYPE.clear();
        }

        public int getViewTypeCount() {
            return VIEW_HOLDER_TYPE.size();
        }
    }

    /**
     * 用来创建RecycleView.Holder
     *
     * @param <T>
     */
    public interface IHolderCreator<T extends SuperViewHolder> {
        T createHolder(ViewGroup parent);
    }

    /**
     * item 点击
     *
     * @param <VH>
     */
    public interface IHolderItemClick<VH> {
        void onItemClick(VH vh);
    }

    public interface ViewTypeProvider {
        String getItemType(Object data, Integer integer);
    }

    public static class EmptyHolder extends BaseViewHolder {
        TextView tv;
        Object creator;

        public EmptyHolder(Object creator) {
            super(new LinearLayout(AppProxy.getApp()));
            this.creator = creator;
            if (Logs.IS_DEBUG) {
                tv = new TextView(AppProxy.getApp());
                tv.setBackgroundColor(Color.RED);
                ((ViewGroup) itemView).addView(tv);
                tv.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tv.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                tv.setGravity(Gravity.LEFT);
                tv.setTextSize(13);
                tv.setTextColor(Color.WHITE);

            }
        }

        @Override
        public void setData(Object o) {
            super.setData(o);
            if (tv != null) {
                tv.setText("holder创建错误:\ndata:" + o + "\nholder:" + (null == creator ? null : creator.getClass().getName()) + "\n检查构造函数 & 检查内部类，需要publish static");
            }
        }

        @Override
        public BaseViewHolder createHolder(ViewGroup parent) {
            return null;
        }
    }

    public void log(String msg) {
        if (Logs.IS_DEBUG) {
            Logs.e(getClass().getSimpleName() + " ", msg);
        }
    }

    private void ensureUtils() {
        if (mCurrentBuilder == null) {
            mCurrentBuilder = new HolderBuilderUtils();
        }
    }

    /**
     * 监听创建ViewHolder
     */
    public interface OnCreateViewHolderListener {

        void onCreateViewHolder(SuperViewHolder holder);
    }

}
