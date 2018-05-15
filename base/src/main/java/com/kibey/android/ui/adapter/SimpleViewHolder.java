package com.kibey.android.ui.adapter;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kibey.android.app.IContext;

/**
 *
 * 不依赖于Adapter 的ViewHolder
 *
 * @author seven
 * @version V1.0
 * @since 2016/10/25
 */
public class SimpleViewHolder<D> extends BaseRVAdapter.BaseViewHolder<D> {

    public SimpleViewHolder(IContext context, @LayoutRes int layoutRes) {
        this(context, LayoutInflater.from(context.getActivity()).inflate(layoutRes, null));
    }

    public SimpleViewHolder(IContext context, View view) {
        super(view);
        this.mContext = context;
    }

    @Override
    public BaseRVAdapter.BaseViewHolder createHolder(ViewGroup parent) {
        return null;
    }
}
