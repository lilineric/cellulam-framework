package com.cellulam.core.utils;

import com.cellulam.core.exceptions.SysException;
import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class MapCopyUtils {

    /**
     *
     * Deep copy
     * Cannot support {@link java.util.Map}, the map must implements {@link Serializable}
     * @param source
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V>HashMap<K, V> deepCopy(HashMap<K, V> source) {
        if(source == null) {
            return null;
        }

        HashMap<K, V> target = Maps.newHashMap();
        target.putAll(source);
        return target;
    }

    /**
     * Deep copy
     * Cannot support {@link java.util.Map}, the map must implements {@link Serializable}
     *
     * @return
     */
    public static <MAP extends Map & Serializable> MAP deepCopy(MAP source, Class<MAP> mClass) {
        if (source == null) {
            return null;
        }
        try {
            MAP target = mClass.newInstance();
            target.putAll(source);
            return target;
        } catch (Exception e) {
            throw new SysException(e);
        }
    }
}
