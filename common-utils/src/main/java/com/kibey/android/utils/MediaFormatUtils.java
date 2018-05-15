package com.kibey.android.utils;

import android.media.MediaFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author mchwind
 * @version V6.1
 * @since 17/4/18
 */
public class MediaFormatUtils {

    public static int getSampleRate(MediaFormat aacFormat) {
        int sampleRate = 0;
        try {
            sampleRate = aacFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        } catch (Exception e) {
            Field[] fields = aacFormat.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object object = field.get(aacFormat);
                    if (object instanceof Map) {
                        Object value = ((Map) object).get(MediaFormat.KEY_SAMPLE_RATE);
                        if (value instanceof Double) {
                            sampleRate = ((Double) value).intValue();
                        }
                    }
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sampleRate;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static long getDuration(MediaFormat aacFormat) {
        long duration = 0;
        try {
            duration = aacFormat.getLong(MediaFormat.KEY_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
            Field[] fields = aacFormat.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object object = field.get(aacFormat);
                    if (object instanceof Map) {
                        Object value = ((Map) object).get(MediaFormat.KEY_DURATION);
                        if (value instanceof Double) {
                            duration = ((Double) value).longValue();
                        }
                    }
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return duration;
    }
}
