package com.kibey.android.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mchwind on 15/7/8.
 */
public class ListUtils {
    public static boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }

    public static boolean notEmpty(Collection collection) {
        return !isEmpty(collection);
    }

    public static HashMap<Class, Collection> toSameClassList(Collection collection) {
        if (ListUtils.isEmpty(collection)) return null;
        HashMap<Class, Collection> result = new HashMap<>();
        for (Object o : collection) {
            Class clz = o.getClass();
            if (result.containsKey(clz)) {
                result.get(clz).add(o);
            } else {
                Collection list = new ArrayList();
                list.add(o);
                result.put(clz, list);
            }
        }
        return result;
    }

    public static int sizeOf(Collection data) {
        return null == data ? 0 : data.size();
    }

    /**
     * 去掉为空的对象
     *
     * @param list List
     */
    public static void trimNull(List list) {
        int count = sizeOf(list);
        if (count > 0) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (null == it.next()) {
                    it.remove();
                }
            }
        }
    }

    public static void addAll(Collection data, Collection append) {
        if (data != null && append != null) {
            data.addAll(append);
        }
    }

    public  static <T> T[] toList(List list,T[] objects) {
        list.toArray(objects);
        return objects;
    }

    public static ArrayList asList(Object[] array) {
        ArrayList list = new ArrayList();
        if (array == null) {
            return list;
        }
        for (Object o : array) {
            list.add(o);
        }
        return list;
    }
}
