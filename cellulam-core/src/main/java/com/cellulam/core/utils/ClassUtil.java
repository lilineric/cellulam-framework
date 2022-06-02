package com.cellulam.core.utils;

import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class ClassUtil {

    private static final Map<Class<?>, Class<?>> primitiveMap = new HashMap<>(9);


    static {
        primitiveMap.put(String.class, String.class);
        primitiveMap.put(Boolean.class, boolean.class);
        primitiveMap.put(Byte.class, byte.class);
        primitiveMap.put(Character.class, char.class);
        primitiveMap.put(Double.class, double.class);
        primitiveMap.put(Float.class, float.class);
        primitiveMap.put(Integer.class, int.class);
        primitiveMap.put(Long.class, long.class);
        primitiveMap.put(Short.class, short.class);
        primitiveMap.put(Date.class, Date.class);

        primitiveMap.put(LocalDate.class, LocalDate.class);
        primitiveMap.put(LocalDateTime.class, LocalDateTime.class);

        primitiveMap.put(Class.class, Class.class);
        primitiveMap.put(Locale.class, Locale.class);
        primitiveMap.put(URI.class, URI.class);
        primitiveMap.put(URL.class, URL.class);

        primitiveMap.put(BigDecimal.class, BigDecimal.class);

    }

    /**
     * 获取父类的泛型的实际Class
     *
     * @param clazz
     * @param index
     * @return
     */
    public static Class getSuperClassGenericType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class<?>) params[index];
    }

    private static boolean _isSimpleValueType(Class<?> clazz) {
        return (ClassUtils.isPrimitiveOrWrapper(clazz) || clazz.isEnum() ||
                CharSequence.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Date.class.isAssignableFrom(clazz) ||
                URI.class == clazz || URL.class == clazz ||
                Locale.class == clazz || Class.class == clazz);
    }


    public static boolean isSimpleValueType(Class<?> clazz) {
        return _isSimpleValueType(clazz);
    }


    /**
     * @param clazz
     * @return boolean
     * @description 判断基本类型
     */
    public static boolean isPrimitive(Class<?> clazz) {
        if (primitiveMap.containsKey(clazz)) {
            return true;
        }
        return clazz.isEnum() || clazz.isPrimitive() || _isSimpleValueType(clazz);
    }

    /**
     * 获取基本类型
     * @param clzz
     * @return
     */
    public static Class<?> getPrimitive(Class<?> clzz) {
       return primitiveMap.get(clzz);
    }

    /**
     * @param target
     * @param fieldName
     * @return Class<?>
     * @description 获取方法返回值类型
     */
    public static Class<?> getElementType(Class<?> target, String fieldName) {
        Class<?> elementTypeClass = null;
        try {
            Type type = target.getDeclaredField(fieldName).getGenericType();
            ParameterizedType t = (ParameterizedType) type;
            String classStr = t.getActualTypeArguments()[0].toString().replace("class ", "");
            elementTypeClass = Thread.currentThread().getContextClassLoader().loadClass(classStr);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException e) {
            try {
                elementTypeClass = getElementTypeUppercase(target, fieldName);
            } catch (Exception e2) {
                throw new RuntimeException("get fieldName[" + fieldName + "] error", e2);
            }
        }
        return elementTypeClass;
    }

    public static Class<?> getElementTypeUppercase(Class<?> target, String fieldName) {
        Class<?> elementTypeClass = null;
        try {
            Type type = target.getDeclaredField(fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).getGenericType();
            ParameterizedType t = (ParameterizedType) type;
            String classStr = t.getActualTypeArguments()[0].toString().replace("class ", "");
            elementTypeClass = Thread.currentThread().getContextClassLoader().loadClass(classStr);
        } catch (ClassNotFoundException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException("get fieldName[" + fieldName + "] error", e);
        }
        return elementTypeClass;
    }

    public static boolean hasInterface(Class<?> target, Class<?> interfaceClass) {
        Class<?>[] interfaces = target.getInterfaces();
        if (interfaces == null || interfaces.length <= 0) {
            return false;
        }

        for (Class<?> tClass : interfaces) {
            if (tClass.equals(interfaceClass)) {
                return true;
            }
        }
        return false;
    }
}
