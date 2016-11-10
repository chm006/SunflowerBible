/*
 *
 */
package com.chm006.library.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author xiaocao000
 */
public class CollectionUtil {

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> void clear(List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        } else {
            list.clear();
        }
    }

    public interface Convetor<T, F> {
        T convert(F f);
    }

    public static <T, F> void addAll(List<T> to, List<F> from, Convetor<T, F> convert) {
        if (to == null) {
            to = new ArrayList<T>();
        }
        if (isNotEmpty(from) && convert != null) {
            for (F f : from) {
                to.add(convert.convert(f));
            }
        }
    }

    public static <K, V> void clear(Map<K, V> map) {
        if (map == null) {
            map = new HashMap<K, V>();
        } else {
            map.clear();
        }
    }

    public static <T> List<Map<String, T>> asMapList(T... values) {
        List<Map<String, T>> result = new ArrayList<Map<String, T>>();
        if (values.length % 2 != 0) {
            throw new RuntimeException("invalid params length");
        }

        if (values.length > 0) {
            for (int i = 0; i < values.length; i += 2) {
                result.add(asMap(values[i], values[i + 1]));
            }
        }
        return result;
    }

    public static <T> Map<String, T> asMap(T... values) {
        Map<String, T> result = new HashMap<String, T>();
        if (values.length % 2 != 0) {
            throw new RuntimeException("invalid params length");
        }
        if (values.length > 0) {
            for (int i = 0; i < values.length; i += 2) {
                result.put(values[i].toString(), values[i + 1]);
            }
        }

        return result;
    }
}