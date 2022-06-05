package com.cellulam.core.reflect;

import com.cellulam.core.enums.CodeDescriptionFeature;
import com.cellulam.core.enums.EnumUtils;
import com.cellulam.core.utils.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Deep copy
 * the target class must have a no args constructor
 * @author eric.li
 */
@Slf4j
public class DeepBeanCopier {

    /**
     * the beanCopierMap
     */
    private static final ConcurrentMap<String, BeanCopier> beanCopierMap = new ConcurrentHashMap<>();

    /**
     * convert
     *
     * @param source
     * @param target
     * @return T
     */
    public static <T> T convert(Object source, Class<T> target) {
        if (source == null) {
            return null;
        }

        if (CodeDescriptionFeature.class.isAssignableFrom(target)) {
            if (source instanceof Integer) {
                return (T) EnumUtils.valueOf((Integer) source, (Class<CodeDescriptionFeature>) target);
            } else if (source instanceof CodeDescriptionFeature) {
                return (T) EnumUtils.valueOf(((CodeDescriptionFeature) source).getCode(), (Class<CodeDescriptionFeature>) target);
            }
        }
        if (CodeDescriptionFeature.class.isAssignableFrom(source.getClass()) && (Number.class.isAssignableFrom(target) || int.class.isAssignableFrom(target))) {
            return (T) ((Object) ((CodeDescriptionFeature) source).getCode());
        }

        if (ClassUtil.isPrimitive(source.getClass())) {
            return (T) source;
        }

        if (source instanceof Number) {
            return (T) source;
        }

        T ret;
        try {
            ret = target.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("create class[" + target.getName()
                    + "] instance error", e);
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(), target);
        beanCopier.copy(source, ret, new DeepCopyConverter(target));

        return ret;
    }

    /**
     * convert to list
     *
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> convertList(List<?> source, Class<T> target) {
        if (source == null) {
            return null;
        }
        if (source.size() == 0) {
            return Collections.emptyList();
        }
        List<T> ret = new ArrayList<>(source.size());
        for (Object obj : source) {
            ret.add(convert(obj, target));
        }
        return ret;
    }

    public static class DeepCopyConverter implements Converter {

        /**
         * The Target.
         */
        private Class<?> target;

        /**
         * Instantiates a new Deep copy converter.
         *
         * @param target the target
         */
        public DeepCopyConverter(Class<?> target) {
            this.target = target;
        }

        @Override
        public Object convert(Object value, Class targetClazz, Object methodName) {
            if (value == null) {
                return value;
            }
            if (value instanceof List && List.class.isAssignableFrom(targetClazz)) {
                List values = (List) value;
                List retList = new ArrayList<>(values.size());
                for (final Object source : values) {
                    String tempFieldName = methodName.toString().replace("set",
                            "");
                    String fieldName = tempFieldName.substring(0, 1).toLowerCase() + tempFieldName.substring(1);
                    Class clazz = ClassUtil.getElementType(target, fieldName);
                    retList.add(DeepBeanCopier.convert(source, clazz));
                }
                return retList;
            } else if (value instanceof List && targetClazz.isArray()) {
                List values = (List) value;

                Object retArraies = Array.newInstance(targetClazz.getComponentType(), values.size());
                for (int i = 0; i < values.size(); i++) {
                    String tempFieldName = methodName.toString().replace("set",
                            "");
                    String fieldName = tempFieldName.substring(0, 1).toLowerCase() + tempFieldName.substring(1);
                    Class clazz = null;
                    try {
                        clazz = target.getDeclaredField(fieldName).getType().getComponentType();
                    } catch (NoSuchFieldException e) {
                        log.error("", e);
                    }
                    Array.set(retArraies, i, DeepBeanCopier.convert(values.get(i), clazz));
                }
                return retArraies;
            } else if (value.getClass().isArray() && List.class.isAssignableFrom(targetClazz)) {
                Object[] values = (Object[]) value;
                List retList = new ArrayList<>(values.length);
                for (final Object source : values) {
                    String tempFieldName = methodName.toString().replace("set",
                            "");
                    String fieldName = tempFieldName.substring(0, 1).toLowerCase() + tempFieldName.substring(1);
                    Class clazz = ClassUtil.getElementType(target, fieldName);
                    retList.add(DeepBeanCopier.convert(source, clazz));
                }
                return retList;
            } else if (value instanceof Map) {
                // TODO
            } else if (!ClassUtil.isPrimitive(targetClazz)) {
                return DeepBeanCopier.convert(value, targetClazz);
            } else if (targetClazz.isEnum()) {
                if (value instanceof Number) {
                    if (CodeDescriptionFeature.class.isAssignableFrom(targetClazz)) {
                        return EnumUtils.valueOf(((Number) value).intValue(), targetClazz);
                    }
                } else {
                    return DeepBeanCopier.convert(value, targetClazz);
                }
            } else if (value.getClass().isEnum() && (Number.class.isAssignableFrom(targetClazz) || int.class.isAssignableFrom(targetClazz))) {
                return DeepBeanCopier.convert(value, targetClazz);
            }
            return value;
        }
    }

    /**
     * @param source
     * @param target
     * @return BeanCopier
     * @description get BeanCopier
     */
    public static BeanCopier getBeanCopier(Class<?> source, Class<?> target) {
        String beanCopierKey = generateBeanKey(source, target);
        if (beanCopierMap.containsKey(beanCopierKey)) {
            return beanCopierMap.get(beanCopierKey);
        } else {
            BeanCopier beanCopier = BeanCopier.create(source, target, true);
            beanCopierMap.putIfAbsent(beanCopierKey, beanCopier);
        }
        return beanCopierMap.get(beanCopierKey);
    }

    /**
     * @param source
     * @param target
     * @return String
     * @description generate key
     */
    public static String generateBeanKey(Class<?> source, Class<?> target) {
        return source.getName() + "@" + target.getName();
    }

}
