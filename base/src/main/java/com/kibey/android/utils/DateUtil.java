package com.kibey.android.utils;

import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期处理相关类
 *
 * @author Lin
 * @version V1.0
 * @since 16/5/25
 */
public class DateUtil {
    public static long _DAY_M = 24 * 60 * 60 * 1000;

    /**
     * 常用格式
     */
    public static final String DAY = "dd";
    public static final String FORMAT_UNDERLINE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_UNDERLINE_YYYY_MM = "yyyy-MM";
    public static final String FORMAT_POINT_YYYY_MM_DD = "yyyy.MM.dd";
    public static final String FORMAT_SLANT_YYYY_MM_DD = "yyyy/MM/dd";

    public static final String YYYY_MM_DD__HH_MM = "yyyy-MM-dd HH:mm";

    public static final String FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String FORMAT_HH_MM = "HH:mm";
    public static final String FORMAT_MM_SS = "mm:ss";
    public static final String FORMAT_DD_MM = "MM/dd";

    public static final String FORMAT_XM_XD_HH_MM = "MM月dd日 " + FORMAT_HH_MM;
    public static final String JSON_STANDARD_DATEF_FORMAT_18 = "yyyy-MM-dd " + FORMAT_HH_MM_SS;

    private static SimpleDateFormat format;
    private static SimpleDateFormat sDateFormat;
    private static SimpleDateFormat sDateFormatDDMM;


    /**
     * 格式化日期
     *
     * @param timeStr php 时间戳 秒
     * @return day
     */
    public static String getDayFromPhpData(String timeStr) {
        long time = 0;

        if (null != timeStr) {
            try {
                time = Long.parseLong(timeStr) * 1000;
            } catch (Exception e) {
                time = System.currentTimeMillis();
            }
        } else {
            time = System.currentTimeMillis();
        }

        return String.valueOf(android.text.format.DateFormat.format(DAY, time));
    }

    /**
     * 格式化日期
     *
     * @param timeStr    php 时间戳 秒
     * @param dataFormat 返回格式
     * @return day
     */
    public static String formatDate(String timeStr, String dataFormat) {
        long time = 0;

        if (!TextUtils.isEmpty(timeStr)) {
            try {
                time = Long.parseLong(timeStr) * 1000;
            } catch (Exception e) {
                time = System.currentTimeMillis();
            }
        } else {
            time = System.currentTimeMillis();
        }

        return String.valueOf(android.text.format.DateFormat.format(dataFormat, time));
    }

    /**
     * 将php 时间戳转换成2011-01-01格式
     *
     * @param sec
     * @return
     */
    public static String php2data10(String sec) {
        String time = "";
        if (sec == null || sec.equals("")) {
            time = "unknown";
        }
        try {
            Long timestamp = Long.parseLong(sec) * 1000;
            Date date = new Date(timestamp);
            SimpleDateFormat d = new SimpleDateFormat(
                    HLPConstants.JSON_STANDARD_DATEF_FORMAT_10);
            time = d.format(date);
        } catch (Exception e) {

        }

        return time;
    }

    /**
     * @param timestampString
     * @return
     */
    public static String timeStamp2DateWithOblique(String timestampString, String formatStr) {
        if (StringUtils.isEmpty(timestampString)) {
            return JSON_STANDARD_DATEF_FORMAT_18;
        }
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formatStr)
                .format(new Date(timestamp));
        return date;
    }


    /**
     * json中的日期String转换成date类型
     *
     * @param date
     * @return
     */
    public static Date string2Date10(String date) {
        date = StringUtils.isEmpty(date) ? JSON_STANDARD_DATEF_FORMAT_18 : date;
        SimpleDateFormat format = new SimpleDateFormat(
                HLPConstants.JSON_STANDARD_DATEF_FORMAT_10);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date d = null;
        try {
            d = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Date string2Date18(String date) {
        date = StringUtils.isEmpty(date) ? JSON_STANDARD_DATEF_FORMAT_18 : date;
        SimpleDateFormat format = new SimpleDateFormat(
                HLPConstants.JSON_STANDARD_DATEF_FORMAT_18);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        Date d = null;
        try {
            d = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 将date转换成json中的年月日格式
     *
     * @param date
     * @return
     */
    public static String date2String10(Date date) {
        if (null == date) {
            return JSON_STANDARD_DATEF_FORMAT_18;
        }
        SimpleDateFormat d = new SimpleDateFormat(
                HLPConstants.JSON_STANDARD_DATEF_FORMAT_10);
        String time = d.format(date);
        return time;
    }

    /**
     * 将Date对象转为日期Str
     *
     * @param date      Date对象
     * @param strFormat 格式化
     * @return 格式化日期Str
     */
    public static String date2Str(Date date, String strFormat) {
        if (null == date) {
            return getDefaultDate(strFormat);
        }
        try {
//            java.lang.StringIndexOutOfBoundsException: length=10; index=17
//            java.lang.StringIndexOutOfBoundsException: length=10; index=10
            // TODO: 16/12/4 暂时这样处理一下
            return getSimpleDateFormat(strFormat).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 输入符合格式化的Str，转为Date对象
     *
     * @param date      输入符合格式化的Str
     * @param strFormat 格式化
     * @return Date对象
     */
    public static Date str2Date(String date, String strFormat) {
        date = TextUtils.isEmpty(date) ? getDefaultDate(strFormat) : date;
        SimpleDateFormat format = getSimpleDateFormat(strFormat);
        Date d = null;
        try {
            d = format.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 格式化时间输出
     * 参考节点:GMT
     *
     * @param time       (s)
     * @param dataFormat
     * @return
     */
    public static String formatDateStr(long time, String dataFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat();
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            fmt.applyPattern(dataFormat);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return fmt.format(time * 1000);
    }

    /**
     * 从Date对象,获取HH:mm
     *
     * @param date
     * @return 获取HH:mm
     */
    public static String getHHmm(Date date) {
        return date2Str(date, FORMAT_HH_MM);
    }

    /**
     * 获取SimpleDateFormat实例
     *
     * @param dataFormat 日期格式
     * @return SimpleDateFormat实例
     */
    private static SimpleDateFormat getSimpleDateFormat(String dataFormat) {
        if (null == format) {
            format = new SimpleDateFormat();
            format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        }
        try {
            format.applyPattern(dataFormat);
        } catch (Exception e) {
            e.printStackTrace();
            //返回系统默认格式
        }
        return format;
    }

    /**
     * @return 获取当前时间
     * See{@link #FORMAT_UNDERLINE_YYYY_MM_DD}
     */
    public static String getCurrentTimeJson10() {
        return date2Str10(new Date());
    }

    /**
     * @return 获取当前时间
     * See{@link #JSON_STANDARD_DATEF_FORMAT_18}
     */
    public static String getCurrentTimeJson18() {
        return date2Str18(new Date());
    }

    /**
     * @return 获取明天时间
     * See{@link #FORMAT_UNDERLINE_YYYY_MM_DD}
     */
    public static String getTomorrowTimeJson10() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();
        return date2Str10(date);
    }

    /**
     * @return 获取明天时间
     * See{@link #JSON_STANDARD_DATEF_FORMAT_18}
     */
    public static String getTomorrowTimejson18() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date date = calendar.getTime();
        return date2Str18(date);
    }


    /**
     * json中的日期String转换成date类型
     *
     * @param date json中的日期Str
     *             See {@link #FORMAT_UNDERLINE_YYYY_MM_DD}.
     * @return Date对象
     */
    public static Date str10toDate(String date) {
        return str2Date(date, FORMAT_UNDERLINE_YYYY_MM_DD);
    }

    /**
     * json中的日期String转换成date类型
     *
     * @param date json中的日期Str
     *             See {@link #JSON_STANDARD_DATEF_FORMAT_18}.
     * @return Date对象
     */
    public static Date str18toDate(String date) {
        return str2Date(date, JSON_STANDARD_DATEF_FORMAT_18);
    }

    /**
     * 将date转换成json中的年月日格式
     *
     * @param date
     * @return json中的年月日格式
     * See {@link #FORMAT_UNDERLINE_YYYY_MM_DD}
     */
    public static String date2Str10(Date date) {
        return date2Str(date, FORMAT_UNDERLINE_YYYY_MM_DD);
    }

    /**
     * 将date转换成json中的标准日期Str格式
     *
     * @param date
     * @return json中的标准日期Str格式
     * See {@link #JSON_STANDARD_DATEF_FORMAT_18}
     */
    public static String date2Str18(Date date) {
        return date2Str(date, JSON_STANDARD_DATEF_FORMAT_18);
    }


    /**
     * 获取系统日期
     *
     * @param dataFormat 日期格式
     * @return 系统日期Str
     */
    private static String getDefaultDate(String dataFormat) {
        return date2Str(new Date(), dataFormat);
    }

    public static String timestamp2Date18(String timestampString) {
        return formatDate(timestampString, JSON_STANDARD_DATEF_FORMAT_18);
    }

    public static String getString(int resId) {
        return AppProxy.getApp().getString(resId);
    }


    /**
     * 获取微秒
     *
     * @return
     */
    public static long getMicroSecond() {
        long initNanoTime = System.nanoTime();
        long initTime = System.currentTimeMillis();
        long duration = (System.nanoTime() - initNanoTime) / 1000;
        long microSecond = (initTime * 1000 + duration);
        if (Logs.IS_DEBUG) {
            Log.d("date_utils", "getMicroSecond: " + microSecond + " " + DateUtil.timestamp2Date18("" + (microSecond / 1000 / 1000)));
        }
        return microSecond;
    }

    public static String formatTimeMMSS(long time) {
        if (sDateFormat == null) {
            sDateFormat = new SimpleDateFormat(FORMAT_MM_SS, Locale.getDefault());
        }
        return sDateFormat.format(new Date(time));
    }

    public static String formatTimeDDMM(long time) {
        if (sDateFormatDDMM == null) {
            sDateFormatDDMM = new SimpleDateFormat(FORMAT_DD_MM, Locale.getDefault());
        }
        return sDateFormatDDMM.format(new Date(time));
    }

    public static String formatTimeHHMMSS(long time) {
        String mmss = formatTimeMMSS(time);
        String hh = StringUtils.addPrefixZero((int) (time / (3600 * 1000)));
        return hh + ":" + mmss;
    }

    /**
     * @param phpDate 10位时间戳，秒
     * @return Feb 23
     */
    public static String dateToShortName(String phpDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        return dateFormat.format(new Date(StringUtils.parseLong(phpDate) * 1000));
    }
}
