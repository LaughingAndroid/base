package com.kibey.android.utils;

import android.text.TextUtils;

import java.util.List;

import okhttp3.Cookie;


/**
 * @author feng
 * @version V5.6
 * @since 16/9/9
 */
public class CookieUtils {

    private static String DIVIDER = "; ";
    private static String COOKIE;
    private static long COOKIE_SET_TIME = 0;

    public static void saveCookie(String cookie) {
        COOKIE = cookie;
        COOKIE_SET_TIME = System.currentTimeMillis();
        PrefsHelper.getDefault().save(APPConfig.COOKIE, cookie);
        if (COOKIE != null && Logs.IS_DEBUG) {
            saveInfoInSDCard(APPConfig.COOKIE, cookie);
        }
    }

    public static String getCookie() {
        if (TextUtils.isEmpty(COOKIE) || (System.currentTimeMillis() - COOKIE_SET_TIME) > 1000 * 60) {
            if (Logs.IS_DEBUG) {
                COOKIE = getInfoFromSDCard(APPConfig.COOKIE);
            } else {
                COOKIE = PrefsHelper.getDefault().getString(APPConfig.COOKIE);
            }
            COOKIE_SET_TIME = System.currentTimeMillis();
        }
        return COOKIE;
    }

    public static String getSession() {
        // PHPSESSID=dlnlh8kgjlp6g3kmtq2ltpnth0; expires=Thu, 20-Apr-2017 08:54:10 GMT; Max-Age=2592000; path=/; domain=.app-echo.com; HttpOnly

        String cookie = getCookie();
        if (cookie.startsWith("PHPSESSID") && cookie.contains(";")) {
            String session = cookie.split(";")[0].replace("PHPSESSID=", "");
            return session;
        }
        return cookie;
    }


    public static void saveCookie(List<Cookie> cookies) {
        if (cookies.isEmpty()) {
            return;
        }
        StringBuilder result = new StringBuilder();
        result.append(cookies.get(0).toString());
        for (int i = 1; i < cookies.size(); i++) {
            result.append(DIVIDER).append(cookies.get(i).toString());
        }
        saveCookie(result.toString());

    }

    public static void saveInfoInSDCard(String key, String info) {
        String filePath = FilePathManager.getFilepath() + "/" + key;
        FileUtils.writeFile(filePath, info, false);
    }

    public static String getInfoFromSDCard(String key) {
        String filePath = FilePathManager.getFilepath() + "/" + key;
        StringBuilder sb = FileUtils.readFile(filePath);
        String str = sb != null ? sb.toString() : "";
        return str;
    }

}
