package com.kibey.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


/**
 * SharedPreferences 工具类
 *
 * @author seven
 * @version V1.0
 * @since 16/5/28
 */
public final class PrefsHelper {

    private static PrefsHelper sInstance;

    public static PrefsHelper getDefault() {
        Context context = AppProxy.getApp();
        return getDefault(context);
    }

    public static PrefsHelper getDefault(Context context) {
        if (null == sInstance) {
            synchronized (PrefsHelper.class) {
                // 这里用到App这个context，耦合较高～
                Log.e("laughing", "PrefsHelper init=====>" + AppProxy.getApp());
                sInstance = new PrefsHelper(context);
            }
        }
        return sInstance;
    }

    private SharedPreferences mPreferences;

    public PrefsHelper(SharedPreferences preferences) {
        setSharedPreferences(preferences);
    }

    public PrefsHelper(Context context, String prefsName) {
        this(context.getSharedPreferences(prefsName, Context.MODE_PRIVATE));
    }

    public PrefsHelper(Context context) {
        this(context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE));
    }

    private void setSharedPreferences(SharedPreferences preferences) {
        this.mPreferences = preferences;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    private boolean isNull(String key) {
        return null == key || null == mPreferences;
    }

    /**
     * 保存boolean
     *
     * @param key   Key
     * @param value Value
     */
    public void save(String key, boolean value) {
        if (isNull(key)) {
            return;
        }

        mPreferences.edit().putBoolean(key, value).commit();
    }

    /**
     * 保存int
     *
     * @param key   Key
     * @param value Value
     */
    public void save(String key, int value) {
        if (isNull(key)) {
            return;
        }

        mPreferences.edit().putInt(key, value).commit();
    }

    /**
     * 保存long
     *
     * @param key   Key
     * @param value Value
     */
    public void save(String key, long value) {
        if (isNull(key)) {
            return;
        }

        mPreferences.edit().putLong(key, value).commit();
    }

    /**
     * 保存float
     *
     * @param key   Key
     * @param value Value
     */
    public void save(String key, float value) {
        if (isNull(key)) {
            return;
        }

        mPreferences.edit().putFloat(key, value).commit();
    }

    /**
     * 保存String
     *
     * @param key   Key
     * @param value Value
     */
    public void save(String key, String value) {
        if (isNull(key)) {
            return;
        }
        try {
            value = AESUtils.encrypt(key, value);
        } catch (Exception e) {
            Logs.e("" + e + "\n" + value);
        }
        mPreferences.edit().putString(key, value).commit();
    }

    /**
     * 获取boolean
     *
     * @param key Key
     * @return 默认返回false
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * 获取boolean
     *
     * @param key          Key
     * @param defaultValue 默认值
     * @return boolean
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        if (isNull(key)) {
            return defaultValue;
        }
        return mPreferences.getBoolean(key, defaultValue);
    }

    /**
     * 获取int
     *
     * @param key Key
     * @return 默认返回0
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * 获取int
     *
     * @param key          Key
     * @param defaultValue 默认值
     * @return int
     */
    public int getInt(String key, int defaultValue) {
        if (isNull(key)) {
            return defaultValue;
        }
        return mPreferences.getInt(key, defaultValue);
    }

    /**
     * 获取long
     *
     * @param key Key
     * @return 默认返回－1
     */
    public long getLong(String key) {
        return getLong(key, -1);
    }

    /**
     * 获取long
     *
     * @param key          Key
     * @param defaultValue 默认值
     * @return long
     */
    public long getLong(String key, long defaultValue) {
        if (isNull(key)) {
            return defaultValue;
        }
        return mPreferences.getLong(key, defaultValue);
    }

    /**
     * 获取float
     *
     * @param key Key
     * @return 默认返回0
     */
    public float getFloat(String key) {
        return getFloat(key, 0);
    }

    /**
     * 获取float
     *
     * @param key          Key
     * @param defaultValue 默认值
     * @return float
     */
    public float getFloat(String key, float defaultValue) {
        if (isNull(key)) {
            return defaultValue;
        }
        return mPreferences.getFloat(key, defaultValue);
    }

    /**
     * 获取String
     *
     * @param key Key
     * @return 默认返回null
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * 获取String
     *
     * @param key          Key
     * @param defaultValue 默认值
     * @return String
     */
    public String getString(String key, String defaultValue) {
        if (isNull(key)) {
            return defaultValue;
        }
        String value = mPreferences.getString(key, defaultValue);
        try {
            return AESUtils.decrypt(key, value);
        } catch (Exception e) {
            Logs.e("" + e + "\n" + value);
        }
        return value;
    }

	/**
	 * 判断是否有配置
	 * @param key Key
	 * @return true ／ false
	 */
	public boolean has(String key) {
		if (isNull(key)) {
			return false;
		}

		return mPreferences.contains(key);
	}

	/**
	 * 删除 配置
	 * @param key Key
	 */
	public void remove(String key) {
		if (isNull(key)) {
			return;
		}

		mPreferences.edit().remove(key).commit();
	}
}
