package com.cellulam.core.enums;


import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class EnumUtils {

    public static final Map<Class<? extends CodeFeature>, Map<Integer, ? extends CodeFeature>> map = Maps.newHashMap();

    /**
     * 注册枚举类 对应的 所有枚举值
     *
     * @param enumClass
     */
    public static void register(Class<? extends CodeFeature> enumClass) {
        CodeFeature[] features = enumClass.getEnumConstants();
        if (ArrayUtils.isEmpty(features)) {
            return;
        }
        Map<Integer, CodeFeature> mm = Arrays.stream(features).collect(Collectors.toMap(CodeFeature::getCode, x -> x));
        map.put(enumClass, mm);
    }

    /**
     * 从<code>code</code>, 获取枚举实例
     *
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeFeature> T valueOf(Integer code, Class<T> enumClass) {
        if (code == null) {
            return null;
        }
        if (map.containsKey(enumClass)) {
            return (T) map.get(enumClass).getOrDefault(code, null);
        }
        register(enumClass);
        return (T) map.get(enumClass).getOrDefault(code, null);
    }

    /**
     * 从<code>code</code>, 获取枚举实例的描述, 如果不存在, 返回空字符串
     *
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeDescriptionFeature> String getDescription(Integer code, Class<T> enumClass) {
        return getDescription(code, enumClass, "");
    }

    /**
     * 从<code>code</code>, 获取枚举实例的描述, 如果不存在, 返回<code>defaultValue</code>
     *
     * @param code
     * @param enumClass
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T extends CodeDescriptionFeature> String getDescription(Integer code, Class<T> enumClass, String defaultValue) {
        CodeDescriptionFeature enu = valueOf(code, enumClass);
        return enu != null ? enu.getDescription() : defaultValue;
    }


    /**
     * 获取一个枚举类型的所有的枚举值
     *
     * @param enumClass
     * @return
     */
    public static <T> List<T> getEnums(Class<T> enumClass) {
        return Arrays.asList(enumClass.getEnumConstants());
    }

    /**
     * 获取枚举和描述map
     *
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeDescriptionFeature> Map<Integer, String> getEnumDescList(Class<T> enumClass) {
        Map<Integer, String> maps = new HashMap<>();

        List<T> enums = getEnums(enumClass);
        if (enums == null || enums.isEmpty()) {
            return maps;
        }

        for (T em : enums) {
            maps.put(em.getCode(), em.getDescription());
        }
        return maps;
    }

    /**
     * 兼容, , 如果不存在, 返回空字符串
     *
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeDescriptionFeature> String GetDescription(Integer code, Class<T> enumClass) {
        return getDescription(code, enumClass, "");
    }

    /**
     * 数字, 文本值(不区分大小写), 统统给你转换成 枚举
     *
     * @param source
     * @param enumType
     * @param <T>
     * @return
     */
    public static <T extends Enum> T textToEnum(String source, Class<T> enumType) {
        if (StringUtils.isBlank(source)) {
            return null;
        }

        if (!enumType.isEnum()) {
            throw new RuntimeException("class[" + enumType + "] MUST be enum-class");
        }

        source = StringUtils.trimToEmpty(source);

        if (CodeDescriptionFeature.class.isAssignableFrom(enumType)) {
            if (NumberUtils.isDigits(source)) {
                return (T) EnumUtils.valueOf(Integer.valueOf(source), (Class<? extends CodeDescriptionFeature>) enumType);
            }
        }

        T[] enumValues = enumType.getEnumConstants();
        for (int i = enumValues.length; --i >= 0; ) {
            T e = enumValues[i];
            if (StringUtils.equalsIgnoreCase(e.toString(), source)) {
                return e;
            }
        }

        throw new RuntimeException("no suitable enum for text[" + source + "] in enum-class[" + enumType + "]");
    }

}
