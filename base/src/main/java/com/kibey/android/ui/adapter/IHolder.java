package com.kibey.android.ui.adapter;

import android.view.View;

import com.kibey.android.utils.IClear;

/**
 * @author mchwind
 * @version V5.8
 * @since 16/12/6
 */
public interface IHolder<Data> extends IClear {
    View getView();

    Data getData();

    void setData(Data data);
}
