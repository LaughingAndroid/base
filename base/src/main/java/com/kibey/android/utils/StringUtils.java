package com.kibey.android.utils;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Laughing Hu
 * @version V1.0
 * @date 2012-5-28
 * @description 字符处理工具类
 */
public abstract class StringUtils {

    private StringUtils() {
    }


    /**
     * 是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value);
    }

    public static boolean isNotEmpty(String str) {
        return !TextUtils.isEmpty(str);
    }

    /**
     * 将字符串转换成数字
     *
     * @param obj
     * @return 如果字符串是数字，返回对应的数字，否则返回0
     */
    public static int parseInt(String obj) {
        if (null == obj) return 0;
        try {
            return Integer.parseInt(obj);
        } catch (Exception e) {
        }

        return 0;
    }

    /**
     * 将字符串转换成数字
     *
     * @param obj
     * @return 如果字符串是数字，返回对应的数字，否则返回0
     */
    public static int parseInt(String obj,int defaultInt) {
        if (null == obj) return defaultInt;
        try {
            return Integer.parseInt(obj);
        } catch (Exception e) {
        }

        return defaultInt;
    }

    /**
     * 将字符串转换成long
     *
     * @param obj
     * @return
     */
    public static long parseLong(String obj) {
        if (null == obj) return 0;
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }

        return 0;
    }

    public static float parseFloat(String obj) {
        if (null == obj) return 0;
        try {
            return Float.parseFloat(obj);
        } catch (Exception e) {
        }

        return 0;
    }

    /**
     * true中文/false英文
     *
     * @param c
     * @return
     */
    public static boolean isCnorEn(char c) {
        boolean flag = true;
        if (c > 0 && c < 127) {
            flag = false;
        }
        return flag;
    }


    /**
     * 计算内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param c
     * @return
     */

    public static int calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            char temp = c.charAt(i);
            if (!isCnorEn(temp)) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return (int) (len * 2);
    }


    public static int calculatePosition(CharSequence c, int cus_len) {
        int result = 0;
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            char temp = c.charAt(i);
            if (!isCnorEn(temp)) {
                len += 0.5;
                result++;
                if ((int) len == cus_len / 2) {
                    return result;
                }
            } else {
                len++;
                result++;
                if ((int) len == cus_len / 2) {
                    return result;
                }
            }
        }
        return 0;
    }

    /**
     * 截取英文长度
     *
     * @param str
     * @param subLen
     * @return
     */
    public static String subChar(String str, int subLen) {
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        for (int index = 0; index < str.length(); index++) {
            if (isCnorEn(str.charAt(index))) {
                count = count + 2;
            } else {
                count++;
            }
            if (count == subLen) {
                buffer.append(str.charAt(index));
                return buffer.toString();
            } else if (count >= subLen) {
                return buffer.toString();
            }
            buffer.append(str.charAt(index));
        }
        return buffer.toString();
    }

    /**
     * 数组是否为空
     *
     * @param values
     * @return true不为空，false为空
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }


    /**
     * 字符串按字节截取
     *
     * @param str 原字符
     * @param len 截取长度
     * @return
     */
    public static String splitString(String str, int len) {
        return splitString(str, len, "...");
    }

    /**
     * 字符串按字节截取
     *
     * @param str   原字符
     * @param len   截取长度
     * @param elide 省略符
     * @return String
     */
    public static String splitString(String str, int len, String elide) {
        if (str == null)
            return "";
        int strlen = str.length();
        if (strlen - len > 0) {
            str = str.substring(0, len) + elide.trim();
        }
        return str;
    }

    public static boolean isEmail(String string) {
        if (TextUtils.isEmpty(string))
            return false;
        String reg = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        return string.matches(reg);
    }

    public static boolean isDate(String string) {
        if (TextUtils.isEmpty(string))
            return false;
        String reg = "\\d{4}-\\d{2}-\\d{2}";
        return string.matches(reg);
    }

    public static boolean isColor(String string) {
        if (TextUtils.isEmpty(string))
            return false;
        String reg = "^#[0-9a-fA-F]{6}";
        return string.matches(reg);
    }

    /**
     * 用html设置字体颜色
     *
     * @param strs 文字&颜色，text1，text2....#ffffff,#000000
     * @return
     * @des <br/>换行
     */
    public static Spanned getHtmlString(String... strs) {
        try {
            String texts[] = new String[strs.length / 2];
            String colors[] = new String[strs.length / 2];
            for (int i = 0; i < strs.length; i++) {
                if (i < strs.length / 2) {
                    texts[i] = strs[i];
                } else {
                    colors[i - strs.length / 2] = strs[i];
                }
            }
            StringBuffer buffer = new StringBuffer();
            if (null != texts && null != colors && texts.length == colors.length) {
                for (int i = 0; i < texts.length; i++) {
                    String color = colors[i];
                    String text = texts[i];
                    String textStr1 = getHtmlColorString(color, text);
                    buffer.append(textStr1);
                }
            }
            return Html.fromHtml(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Html.fromHtml("");
    }


    /**
     * 适用于只有两种颜色的文本
     *
     * @param normalStrings  原始颜色的字符串数组
     * @param coloredStrings 带颜色的字符串数组
     * @param color          要变得颜色
     * @return
     */
    public static Spanned getHtmlString(String[] normalStrings, String[] coloredStrings, String color) {
        int len1 = normalStrings.length;
        int len2 = coloredStrings.length;
        int len = Math.max(len1, len2);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (len1 > i) {
                sb.append(normalStrings[i]);
            }

            if (len2 > i) {
                String string = getHtmlColorString(color, coloredStrings[i]);
                sb.append(string);
            }
        }
        return Html.fromHtml(sb.toString());
    }

    /**
     * 生成html 颜色的string
     *
     * @param color 颜色值，如#888888
     * @param str   输入string
     * @return html string
     */
    public static String getHtmlColorString(String color, String str) {
        return "<font color=\"" + color + "\">" + str + "</font>";
    }

    public static String getCountString(int count, int tenThousandId) {
        if (count < 10000) {
            return count + "";
        }
        float str = count / 10000.0f;
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(str) + AppProxy.getApp().getString(tenThousandId);
    }

    public static String getCountByK(int count) {
        if (count < 1000) {
            return count + "";
        }
        float str = count / 1000.0f;
        DecimalFormat df = new DecimalFormat("#.0");
        return df.format(str) + "K";
    }

    public static String getCountByKByM(int count) {
        if (count < 1000) {
            return count + "";
        }

        float str = count / 1000.0f;
        DecimalFormat df = new DecimalFormat("#.0");

        //以K为单位
        if (count < 1000000) {
            return df.format(str) + "K";
        }

        //以M为单位
        str = str / 1000.0f;
        return df.format(str) + "M";
    }

    /**
     * 去掉换行
     *
     * @param source
     * @return
     */
    public static String replaceEnter(String source) {
        return source.replaceAll("[\r\n]+", "");
    }


    /**
     * 将数字填入字符串，并变色
     *
     * @param str
     * @param count
     * @param color
     * @return
     */
    public static Spannable getCountSpannable(String str, int count, int color) {
        String countString = String.format(str, count);
        int start = countString.indexOf(String.valueOf(count));
        if (start >= 0) {
            int end = start + String.valueOf(count).length();
            Spannable sp = new SpannableString(countString);
            ForegroundColorSpan span = new ForegroundColorSpan(color);
            sp.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return sp;
        } else {
            Log.i("StringUtils", "str argument may be wrong ");
            return null;
        }
    }

    /**
     * 拼接字符串
     *
     * @param list      字符串数组
     * @param separator 分隔符
     * @return String
     */
    public static String join(List<String> list, String separator) {
        if (null == list || list.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, N = list.size(); i < N; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * 数组拼接成字符串 中间以逗号连接
     *
     * @param data   需要连接的数据
     * @param symbol 连接符
     * @return String
     * kibey 2014-2-21 下午5:14:52
     */
    public static String commaInt(Object[] data, String symbol) {
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            sbf.append(data[i]);
            if (i < data.length - 1) {
                sbf.append(symbol);
            }
        }
        return sbf.toString();
    }

    /**
     * 集合拼接成字符串 中间以逗号连接
     *
     * @param data   需要连接的数据
     * @param symbol 连接符
     * @return String
     * kibey 2014-2-21 下午5:14:52
     */
    public static String commaInt(List<String> data, String symbol) {
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            sbf.append(data.get(i));
            if (i < data.size() - 1) {
                sbf.append(symbol);
            }
        }
        return sbf.toString();
    }

    public static String prettyBytes(long value) {
        String args[] = {"B", "KB", "MB", "GB", "TB"};
        StringBuilder sb = new StringBuilder();
        int i;
        if (value < 1024L) {
            sb.append(String.valueOf(value));
            i = 0;
        } else if (value < 1048576L) {
            sb.append(String.format("%.1f", value / 1024.0));
            i = 1;
        } else if (value < 1073741824L) {
            sb.append(String.format("%.2f", value / 1048576.0));
            i = 2;
        } else if (value < 1099511627776L) {
            sb.append(String.format("%.3f", value / 1073741824.0));
            i = 3;
        } else {
            sb.append(String.format("%.4f", value / 1099511627776.0));
            i = 4;
        }
        sb.append(' ');
        sb.append(args[i]);
        return sb.toString();
    }

    // --------------------------------------------------------------------------------------------
    // 对于字符串 添加0或者空格的处理
    public static String addPrefix(int num, String prefix) {
        return num < 10 ? prefix + num : String.valueOf(num);
    }

    public static String addPrefix(String numStr, String prefix) {
        int num = Integer.parseInt(numStr);
        return addPrefix(num, prefix);
    }

    public static String addPrefixZero(int num) {
        return addPrefix(num, "0");
    }

    /**
     * 拼接字符串
     *
     * @param list
     * @param separator
     * @return
     */
    public static String appendString(List list, String separator) {
        StringBuffer sb = new StringBuffer();
        int size = list.size();
        int count = 0;
        for (Object task : list) {
            count++;
            sb.append(task instanceof IAppendString ? ((IAppendString) task).getAppendString() : task.toString());
            if (count <= size - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public interface IAppendString {
        String getAppendString();
    }

    public static DecimalFormat sDecimalFormat0_00 = new DecimalFormat("##.##%");
    public static DecimalFormat sDecimalFormat0_0 = new DecimalFormat("##.#%");
}
