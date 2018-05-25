package com.kibey.android.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author mchwind
 * @version V1.0
 * @since 18/5/22
 * <p>
 * |       |    |   |   |````  |   |   ---  |\  |   |````
 * |      |_|   |   |   |  `|  |---|    |   | \ |   |  `|
 * |___  |   |   |_|     \_/   |   |   ___  |  \|    \_/
 */
public class ListenerView extends View {
    ISuperHolderWindowStatus mViewStatusListener;

    public ListenerView(Context context) {
        super(context);
    }

    public ListenerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ListenerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ListenerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ListenerView(Context context, ISuperHolderWindowStatus listener) {
        this(context);
        this.mViewStatusListener = listener;
    }

    boolean isAttachedToWindow;
    boolean hasWindowFocus = true;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttachedToWindow = true;
        onViewStatusChange();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttachedToWindow = false;
        onViewStatusChange();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        this.hasWindowFocus = hasWindowFocus;
        onViewStatusChange();
    }

    String mLastCallStatus = "";

    void onViewStatusChange() {
        String status = "" + isAttachedToWindow + hasWindowFocus;
        if (mViewStatusListener != null && !status.equals(mLastCallStatus)) {
            mViewStatusListener.onSuperHolderWindowStatusChange(isAttachedToWindow, hasWindowFocus);
        }
        mLastCallStatus = status;
    }


    public interface ISuperHolderWindowStatus {
        /**
         * view是否在当前window及window focus回调
         */
        void onSuperHolderWindowStatusChange(boolean isAttachedToWindow, boolean hasWindowFocus);
    }
}
