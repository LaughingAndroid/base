package com.kibey.android.ui.adapter;

import android.view.ViewGroup;

public abstract class IHolderBuilder<DATA> implements BaseRVAdapter.IHolderCreator {
    protected int layout;
    protected String key;
    protected SuperViewHolder mViewHolder;
    protected BaseRVAdapter.HolderBuilderUtils mHolderBuilder;

    public IHolderBuilder(int layout, Class modelClz) {
        this.layout = layout;
        this.key = modelClz.getName();
    }

    public IHolderBuilder(int layout, String key) {
        this.layout = layout;
        this.key = key;
    }

    public int getLayout() {
        return layout;
    }

    public String getKey() {
        return key;
    }

    public void setHolderBuilder(BaseRVAdapter.HolderBuilderUtils holderBuilder) {
        mHolderBuilder = holderBuilder;
    }

    public SuperViewHolder createHolder(ViewGroup parent) {
        mViewHolder = new SuperViewHolder(parent, layout);
        mViewHolder.setHolderBuilder(this);
        return mViewHolder;
    }

    public int getType(DATA data) {
        return mHolderBuilder.getViewType(data);
    }

    public abstract void bindData(SuperViewHolder<DATA> holder, DATA object);
}
