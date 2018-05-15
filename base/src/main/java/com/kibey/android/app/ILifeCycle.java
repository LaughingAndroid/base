package com.kibey.android.app;

import rx.Observable;

/**
 * @author mchwind
 * @version V5.8
 * @since 16/11/16
 */
public interface ILifeCycle {

    boolean isDestroy();

    boolean isAdded();

    Observable viewPrepare();
}
