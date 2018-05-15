package com.kibey.echo.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kibey.android.utils.IClear;

public class HeadRV extends RecyclerView.ViewHolder implements IClear {

    public HeadRV(View itemView) {
        super(itemView);
        itemView.setTag(this);
    }

    @Override
    public void clear() {
        itemView.setTag(null);
    }
}