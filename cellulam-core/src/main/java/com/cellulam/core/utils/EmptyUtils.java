package com.cellulam.core.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author eric.li
 * @date 2022-06-06 16:41
 */
public class EmptyUtils {

    private EmptyUtils() {

    }

    /**
     *
     * @param coll
     * @return
     */
    public static <T> boolean isEmpty(Collection<T> coll) {
        return (coll == null || coll.isEmpty());
    }

    /**
     *
     * @param coll
     * @return
     */
    public static <T> boolean isNotEmpty(Collection<T> coll) {
        return !isEmpty(coll);
    }

    /**
     *
     * @param map
     * @return
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.isEmpty());
    }

    /**
     *
     * @param map
     * @return
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    /**
     *
     * @param t
     * @return
     */
    public static <T> boolean isEmpty(T t) {
        if (t == null) {
            return true;
        }
        return StringUtils.isEmpty(t.toString());
    }

    /**
     */
    public static <T> boolean isNotEmpty(T[] datas) {
        return !isEmpty(datas);
    }

    /**
     */
    public static <T> boolean isEmpty(T[] datas) {
        return ObjectUtils.isEmpty(datas);
    }


    /**
     *
     * @param t
     * @return
     */
    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }

    /**
     * eg. if (parameter1==null || parameter2==null || parameter3==null)
     * equals
     * if (EmptyUtils.hasNull(parameter1, parameter2,parameter3))
     *
     * @param datas
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasNull(T... datas) {
        for (T t : datas) {
            if (t == null) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param datas
     * @param <K>
     * @param <V>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <K, V> boolean hasEmpty(Map<K, V>... datas) {
        for (Map<K, V> data : datas) {
            if (isEmpty(data)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param datas
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> boolean hasEmpty(Collection<T>... datas) {
        for (Collection<T> data : datas) {
            if (isEmpty(data)) {
                return true;
            }
        }
        return false;
    }

}
