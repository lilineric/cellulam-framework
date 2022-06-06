package com.cellulam.core.utils;

import net.sf.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * bean copy base on cglib
 *
 * @author eric.li
 * @date 2022-06-06 16:40
 */
public abstract class BeanUtils {

    /**
     * @param source
     * @param clazz
     * @return
     */
    public static <T> T copy(Object source, Class<T> clazz) {
        if (EmptyUtils.isEmpty(source)) {
            return null;
        }
        T target = instantiate(clazz);
        BeanCopier copier = BeanCopier.create(source.getClass(), clazz, false);
        copier.copy(source, target, null);
        return target;
    }

    /**
     * @param source
     * @param target
     * @return
     */
    public static void copy(Object source, Object target) {
        AssertUtils.isNotNull(source, "The source must not be null");
        AssertUtils.isNotNull(target, "The target must not be null");
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    /**
     * @param datas
     * @param clazz
     * @return
     */
    public static <T> List<T> copyByList(List<?> datas, Class<T> clazz) {
        if (EmptyUtils.isEmpty(datas)) {
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(datas.size());
        for (Object data : datas) {
            result.add(copy(data, clazz));
        }
        return result;
    }

    /**
     * @param clazz
     * @return
     * @throws RuntimeException
     */
    public static <T> T instantiate(Class<T> clazz) {
        AssertUtils.isNotNull(clazz, "The class must not be null");
        try {
            return clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new RuntimeException(clazz + ":Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(clazz + ":Is the constructor accessible?", ex);
        }
    }
}
