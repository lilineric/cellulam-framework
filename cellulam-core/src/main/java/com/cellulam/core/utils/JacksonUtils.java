package com.cellulam.core.utils;

import com.cellulam.core.json.CustomEnumDeserializers;
import com.cellulam.core.json.Jacksonor;
import com.cellulam.core.json.LocalDateJsonDeserializer;
import com.cellulam.core.json.LocalDateTimeJsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Jackson-Json Util
 * @author eric.li
 */
public abstract class JacksonUtils {

    private static final Jacksonor jacksoner = Jacksonor.generic();

    /**
     * 创建ObjectMapper实例, <br>
     * 1. 忽略未知的属性, <br>
     * 2. 允许控制字符, <br>
     * 3. 注册JodaModule, 解析DateTime, <br>
     *
     * @return
     */
    public static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        mapper.configure(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING, true);

        mapper.configure(MapperFeature.USE_GETTERS_AS_SETTERS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 忽略大小写
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        // 兼容lombok
        mapper.configure(MapperFeature.INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES, false);
        // 禁用自动检测public属性, 必须使用getter-setter
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, false);

        mapper.registerModule(new JodaModule());
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeJsonDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        module.addDeserializer(LocalDate.class, new LocalDateJsonDeserializer(DateTimeFormatter.ISO_DATE));

        mapper.registerModule(module);

        SimpleModule enumModule = new SimpleModule();
        enumModule.setDeserializers(new CustomEnumDeserializers());
        mapper.registerModule(enumModule);

        return mapper;
    }

    /**
     * 转换一个对象为json字符串
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return jacksoner.toJson(obj);
    }

    /**
     * 转换一个json字符串为一个对象(指定class)
     *
     * @param jsonText
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T toObj(String jsonText, Class<T> clz) {
        return jacksoner.toObj(jsonText, clz);
    }

    /**
     * 转换 map 为一个对象(指定class)
     *
     * @param map
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T toObj(Map map, Class<T> clz) {
        return jacksoner.toObj(map, clz);
    }

    /**
     * 转换为Map结构
     *
     * @param jsonText
     * @return
     */
    public static Map toMap(String jsonText) {
        return toObj(jsonText, LinkedHashMap.class);
    }

    /**
     * 转换为List结构
     *
     * @param jsonText
     * @return
     */
    public static List toList(String jsonText) {
        return toObj(jsonText, ArrayList.class);
    }

    /**
     * 转换为数组结构
     *
     * @param jsonText
     * @return
     */
    public static Object[] toArray(String jsonText) {
        return toObj(jsonText, Object[].class);
    }

    /**
     * 转换为Map结构, 且指定key的类型和value的类型
     *
     * @param jsonText   json字符串
     * @param keyClass   key的class类型
     * @param valueClass value的class类型
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> toMap(String jsonText, Class<K> keyClass, Class<V> valueClass) {
        return jacksoner.toMap(jsonText, keyClass, valueClass);
    }

    /**
     * 转换为数组结构, 指定数组类的元素类型
     *
     * @param jsonText
     * @param elementClass 数组内元素类型
     * @param <T>
     * @return
     */
    public static <T> T[] toArray(String jsonText, Class<T> elementClass) {
        return jacksoner.toArray(jsonText, elementClass);
    }

    /**
     * 转换为List结构, 指定List内的元素类型
     *
     * @param jsonText
     * @param elementClass 集合内元素类型
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String jsonText, Class<T> elementClass) {
        return jacksoner.toList(jsonText, elementClass);
    }

    /**
     * 使用指定的TypeReference来转换json字符串
     *
     * @param jsonText
     * @param refer    类型引用
     * @param <T>
     * @return
     */
    public static <T> T toObjByReference(String jsonText, TypeReference<T> refer) {
        return jacksoner.toObjByReference(jsonText, refer);
    }

    /**
     * 使用嵌套 类型 转换json字符串, <br>
     * 举例: DataResult&lt;List&lt;UserRequest&gt;&gt;
     *
     * @param jsonText
     * @param objClass
     * @param nestParameterizedClasses
     * @param <T>
     * @return
     */
    public static <T> T toObjByNestRef(String jsonText, Class<T> objClass, Class<?>... nestParameterizedClasses) {
        return jacksoner.toObjByNestRef(jsonText, objClass, nestParameterizedClasses);
    }

    /**
     * 转换json字符串, 指定实体类和泛型类, <br>
     * 举例: DataResult&lt;UserRequest&gt;
     *
     * @param jsonText       json字符串
     * @param objClass       实体类
     * @param genericClasses 泛型类
     * @param <T>
     * @return
     */
    public static <T> T toObjByGenericType(String jsonText, Class<T> objClass, Class<?>... genericClasses) {
        return jacksoner.toObjByGenericType(jsonText, objClass, genericClasses);
    }

    public static <T> T readByJavaType(String jsonText, JavaType javaType) {
        return jacksoner.readByJavaType(jsonText, javaType);
    }


}
