package com.kibey.android.utils;


import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JsonUtils {
    // TODO: 17/3/7 ? KibeyGsonConverterFactory.newSafeGson()
    private static Gson sGson = new Gson();
    private static Gson sSafeGson = sGson;
    private static String TAG = "jsonutils ";

    public static String jsonFromObject(Object object) {
        return sGson.toJson(object);
    }

    public static String jsonFromObjectSafe(Object object) {
        return sSafeGson.toJson(object);
    }

    public static <T> T objectFromJson(String json, Class<T> clz) {
        T t = null;
        t = sGson.fromJson(json, clz);
        return t;
    }

    /**
     * release状态和之前完全一样，debug状态会使用一个带类型检查的Gson进行解析。
     */
    public static <T> T objectFromJsonSafe(String json, Class<T> clz) {
        T t = null;
        t = sSafeGson.fromJson(json, clz);
        return t;
    }

    /**
     * @param text
     * @param type
     * @return
     */
    public static <T> ArrayList<T> toJSONLisBean(String text, TypeToken type) {

        ArrayList rs = null;
        if (null != text) {
            try {
//                text = URLDecoder.decode(text, "UTF-8");
//                text = text.replace("\\u003d", "=");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (type != null) {
                rs = sGson.fromJson(text, type.getType());
            }
        }
        return rs;

    }

    /**
     * 撞击一个jsonobject,注意不是gson
     *
     * @param requestTag
     * @return
     */
    public static JSONObject createJsonObject(String requestTag) {
        try {
            return new JSONObject(requestTag);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Bundle转json object
     *
     * @param bundle {@link Bundle}
     * @return JSONObject
     */
    public static JSONObject createJsonObject(Bundle bundle) {
        JSONObject json = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                // json.put(key, bundle.get(key)); see edit below
                if (SDKUtils.hasKitkat()) {
                    json.put(key, JSONObject.wrap(bundle.get(key)));
                } else {
                    json.put(key, wrap(bundle.get(key)));
                }
            } catch (JSONException e) {
                //Handle exception here
            }
        }

        return json;
    }

    /**
     * Wraps the given object if necessary.
     * <p>
     * <p>If the object is null or , returns {@link JSONObject#NULL}.
     * If the object is a {@code JSONArray} or {@code JSONObject}, no wrapping is necessary.
     * If the object is {@code NULL}, no wrapping is necessary.
     * If the object is an array or {@code Collection}, returns an equivalent {@code JSONArray}.
     * If the object is a {@code Map}, returns an equivalent {@code JSONObject}.
     * If the object is a primitive wrapper type or {@code String}, returns the object.
     * Otherwise if the object is from a {@code java} package, returns the result of {@code toString}.
     * If wrapping fails, returns null.
     */
    static Object wrap(Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        if (o instanceof JSONArray || o instanceof JSONObject) {
            return o;
        }
        if (o.equals(JSONObject.NULL)) {
            return o;
        }
        try {
            if (o instanceof Collection) {
                return new JSONArray((Collection) o);
            } else if (o.getClass().isArray()) {
                return new JSONArray(Arrays.asList(o));
            }
            if (o instanceof Map) {
                return new JSONObject((Map) o);
            }
            if (o instanceof Boolean ||
                    o instanceof Byte ||
                    o instanceof Character ||
                    o instanceof Double ||
                    o instanceof Float ||
                    o instanceof Integer ||
                    o instanceof Long ||
                    o instanceof Short ||
                    o instanceof String) {
                return o;
            }
            if (o.getClass().getPackage().getName().startsWith("java.")) {
                return o.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static Bundle getBundleForJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return getBundleForJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
        /**
         * Bundle
         *
         * @return
         */
    public static Bundle getBundleForJson(JSONObject jsonObject) {
        try {
            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Bundle valueMap = new Bundle();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                if (value instanceof Integer) {
                    valueMap.putInt(key, (Integer) value);
                } else if (value instanceof Boolean) {
                    valueMap.putBoolean(key, (Boolean) value);
                } else if (value instanceof Double) {
                    valueMap.putDouble(key, (Double) value);
                } else if (value instanceof Float) {
                    valueMap.putFloat(key, (Float) value);
                } else if (value instanceof String) {
                    valueMap.putString(key, (String) value);
                } else if (value instanceof Serializable) {
                    valueMap.putSerializable(key, (Serializable) value);
                } else {
                    valueMap.putString(key, (String) value);
                }
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}