package com.cellulam.core.utils;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 断言失败抛出<code>IllegalArgumentException</code>而非Error
 * 非测试使用
 */
public abstract class AssertUtils {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "this expression must be true");
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object) {
        isNull(object, "the object argument must be null");
    }

    public static void isNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotNull(Object object) {
        isNotNull(object, "this object must not be null");
    }

    public static void isNotEmpty(String text, String message) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotEmpty(String text) {
        isNotEmpty(text,
                "the text must not be null or empty");
    }


    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (StringUtils.isNotEmpty(textToSearch) && StringUtils.isNotEmpty(substring) &&
                textToSearch.indexOf(substring) != -1) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void doesNotContain(String textToSearch, String substring) {
        doesNotContain(textToSearch, substring,
                "this String argument must not contain the substring [" + substring + "]");
    }


    public static void isNotEmpty(Object[] array, String message) {
        if (ArrayUtils.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotEmpty(Object[] array) {
        isNotEmpty(array, "this array must not be empty: it must contain at least 1 element");
    }

    public static void isNotEmpty(Collection collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotEmpty(Collection collection) {
        isNotEmpty(collection,
                "this collection must not be empty: it must contain at least 1 element");
    }

    public static void isNotEmpty(Map map, String message) {
        if (MapUtils.isEmpty(map)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotEmpty(Map map) {
        isNotEmpty(map, "this map must not be empty; it must contain at least one entry");
    }


    public static void isInstanceOf(Class clazz, Object obj) {
        isInstanceOf(clazz, obj, "");
    }

    public static void isInstanceOf(Class type, Object obj, String message) {
        isNotNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(message +
                    "Object of class [" + (obj != null ? obj.getClass().getName() : "null") +
                    "] must be an instance of " + type);
        }
    }

    public static void isAssignable(Class superType, Class subType) {
        isAssignable(superType, subType, "");
    }

    public static void isAssignable(Class superType, Class subType, String message) {
        isNotNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
        }
    }
}
