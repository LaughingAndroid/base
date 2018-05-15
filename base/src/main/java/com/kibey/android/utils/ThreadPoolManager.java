package com.kibey.android.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Observable;
import rx.Subscriber;

import static com.kibey.android.utils.Logs.timeConsuming;
import static java.lang.System.currentTimeMillis;

/**
 * Created by fengguangyu1 on 15/11/5.
 */
public class ThreadPoolManager {

    private static final int count = Runtime.getRuntime().availableProcessors();

    private static ExecutorService mExecutorService = null;

    private static ThreadPoolManager mThreadPoolManager;

    public static synchronized ThreadPoolManager getInstance() {
        if (mThreadPoolManager == null) {
            mThreadPoolManager = new ThreadPoolManager();
        }
        return mThreadPoolManager;
    }


    protected ThreadPoolManager() {
        initExecutorService();
    }

    public static void initExecutorService() {
        if (mExecutorService == null) {
            mExecutorService = Executors.newFixedThreadPool(count);
        }
    }

    public static void execute(Runnable runnable) {
        getInstance().mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long start = currentTimeMillis();
                    runnable.run();
                    if (System.currentTimeMillis() - start > 16) {
                        timeConsuming(runnable + " ThreadPoolManager run time", start);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (Logs.IS_DEBUG) {
                        APPConfig.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    DialogUtils.show("测试忽略，开发debug信息" + e.toString(), SweetAlertDialog.ERROR_TYPE);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public static <T> Observable<T> executeRx(RxRunnable runnable) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                runnable.setSubscriber(subscriber);
                ThreadPoolManager.execute(runnable);
            }
        });
    }

    public abstract static class RxRunnable<T> implements Runnable {
        public Subscriber<T> subscriber;

        public void setSubscriber(Subscriber<T> subscriber) {
            this.subscriber = subscriber;
        }
    }
}
