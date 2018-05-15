package com.kibey.android.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class Logs {
    public static final boolean IS_DEBUG = true;
    public final static String TAG_ = "kibey_echo";

    public static void i(String... msg) {
        if (IS_DEBUG) {
            Log.i(TAG_, msg(msg));
        }
    }

    public static void e(String... msg) {
        if (IS_DEBUG) {
            Log.e(TAG_, msg(msg));
        }
    }

    public static void d(String... msg) {
        if (IS_DEBUG) {
            Log.d(TAG_, msg(msg));
        }
    }

    private static String msg(String[] msg) {
        StringBuffer sb = new StringBuffer();
        for (String s : msg) {
            sb.append(s).append(" ");
        }
        return sb.toString();
    }


    public static void json(String tag, String url, String msg) {
        if (IS_DEBUG && null != msg) {
            String str = "{\"url\":\"" + url + "\"," + msg.substring(1, msg.length());
            Log.i(tag, str);
        }
    }

    public static long timeConsuming(String tag, long start, Object... objects) {
        if (!Logs.IS_DEBUG) return 0;
        long time = (System.currentTimeMillis() - start);
        if (time > 20 && "main".equals(Thread.currentThread().getName())) {
            Logs.e("main time:***** " + time + " *****echo_time_log", tag);
        } else {
            Logs.d(Thread.currentThread().getName() + " time:***** " + time + " *****echo_time_log", tag);
        }

        if (null != objects) {
            if (objects.length > 0) {
                if (objects[0] instanceof ViewGroup) {
                    ViewGroup vg = (ViewGroup) objects[0];
                    int viewCount[] = printViewLog(vg, 0);
                    Logs.i("echo_time_log", " viewCount:" + viewCount[0] + " deep:" + viewCount[1] + " thread:" + Thread.currentThread().getName());
                }
            }
        }
        return time;
    }

    private static int[] printViewLog(View view, int index) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int child = vg.getChildCount();
            int result[] = new int[2];
            for (int i = 0; i < child; i++) {
                int[] temp = printViewLog(vg.getChildAt(i), index + 1);
                result[0] += temp[0];
                result[1] = Math.max(result[1], temp[1]);
                Logs.d("xxxxx_view:" + vg.getChildAt(i));
            }
            result[0]++;
            result[1]++;
            return result;
        }
        return new int[]{1, index};
    }

    public static void printCallStatck(String message) {
        Throwable ex = new Throwable(message);
        StackTraceElement[] stackElements = ex.getStackTrace();
        if (stackElements != null) {
            for (int i = 0; i < stackElements.length; i++) {
                Logs.e(stackElements[i].getClassName() + "/t");
                Logs.e(stackElements[i].getFileName() + "/t");
                Logs.e(stackElements[i].getLineNumber() + "/t");
                Logs.e(stackElements[i].getMethodName());
                Logs.e("-----------------------------------");
            }
        }
    }
}
