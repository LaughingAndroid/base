package com.kibey.android.utils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * @author mchwind
 * @version V1.0
 * @since 16/7/2
 */
public class RxUtils {
    /**
     * 延迟方法
     *
     * @param action
     * @param delay  毫秒
     */
    public static Subscription delay(Action1 action, long delay) {
        Subscription subscription = Subscriptions.empty();
        APPConfig.postDelayed(() -> {
            if (!subscription.isUnsubscribed()) {
                action.call(null);
            }
        }, delay);
        return subscription;
    }

    /**
     * 定时重复任务
     *
     * @param period
     * @param action
     * @return
     */
    public static Subscription timer(long period, Action1 action) {
        return Observable
                .create(Subscriber -> {
                    if (!Subscriber.isUnsubscribed()) {
                        Subscriber.onNext(null);
                    }
                })
                .interval(period, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(action);
    }

    /**
     * 网络操作设置Schedulers
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> applyNetSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
