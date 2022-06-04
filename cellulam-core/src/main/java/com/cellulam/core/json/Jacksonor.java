package com.cellulam.core.json;

import com.cellulam.core.exceptions.SysException;
import com.cellulam.core.reflect.LittleType;
import com.cellulam.core.reflect.TypeRef;
import com.cellulam.core.utils.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Jacksonor {

    private static final Logger logger = LoggerFactory.getLogger(Jacksonor.class);

    private static Jacksonor generic = new Jacksonor(JacksonUtils.createMapper());
    private static Jacksonor power;
    private static Jacksonor noNull;

    public static Jacksonor power() {
        if (power != null) {
            return power;
        }
        synchronized (logger) {
            if (power == null) {
                ObjectMapper mapper = JacksonUtils.createMapper();
                power = new Jacksonor(mapper);
            }
        }
        return power;
    }

    /**
     * 序列化时, 忽略null的字段
     *
     * @return
     */
    public static Jacksonor noNull() {
        if (noNull != null) {
            return noNull;
        }
        synchronized (logger) {
            if (noNull == null) {
                ObjectMapper mapper = JacksonUtils.createMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                noNull = new Jacksonor(mapper);
            }
        }
        return noNull;
    }

    public static Jacksonor generic() {
        return generic;
    }

    private ObjectMapper mapper;

    private Jacksonor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 转换一个对象为json字符串
     *
     * @param obj
     * @return
     */
    public String toJson(Object obj) {
        if (obj == null) {
            return "";
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new SysException(e);
        }
    }

    /**
     * 转换一个json字符串为一个对象(指定class)
     *
     * @param jsonText
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T toObj(String jsonText, Class<T> clz) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        try {
            return mapper.readValue(jsonText, clz);
        } catch (IOException e) {
            throw new SysException(e);
        }
    }

    /**
     * 转换 map 为一个对象(指定class)
     *
     * @param map
     * @param clz
     * @param <T>
     * @return
     */
    public <T> T toObj(Map map, Class<T> clz) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return mapper.convertValue(map, clz);
        } catch (Exception e) {
            throw new SysException(e);
        }
    }

    /**
     * 转换为Map结构
     *
     * @param jsonText
     * @return
     */
    public Map toMap(String jsonText) {
        return toObj(jsonText, LinkedHashMap.class);
    }

    /**
     * 转换为List结构
     *
     * @param jsonText
     * @return
     */
    public List toList(String jsonText) {
        return toObj(jsonText, ArrayList.class);
    }

    /**
     * 转换为数组结构
     *
     * @param jsonText
     * @return
     */
    public Object[] toArray(String jsonText) {
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
    public <K, V> Map<K, V> toMap(String jsonText, Class<K> keyClass, Class<V> valueClass) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructMapLikeType(LinkedHashMap.class, keyClass, valueClass);
        return readByJavaType(jsonText, type);
    }

    /**
     * 转换为数组结构, 指定数组类的元素类型
     *
     * @param jsonText
     * @param elementClass 数组内元素类型
     * @param <T>
     * @return
     */
    public <T> T[] toArray(String jsonText, Class<T> elementClass) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructArrayType(elementClass);
        return readByJavaType(jsonText, type);
    }

    /**
     * 转换为List结构, 指定List内的元素类型
     *
     * @param jsonText
     * @param elementClass 集合内元素类型
     * @param <T>
     * @return
     */
    public <T> List<T> toList(String jsonText, Class<T> elementClass) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        JavaType type = mapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, elementClass);
        return readByJavaType(jsonText, type);
    }

    /**
     * 使用指定的TypeReference来转换json字符串
     *
     * @param jsonText
     * @param refer    类型引用
     * @param <T>
     * @return
     */
    public <T> T toObjByReference(String jsonText, TypeReference<T> refer) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        try {
            return mapper.readValue(jsonText, refer);
        } catch (IOException e) {
            throw new SysException(e);
        }
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
    public <T> T toObjByNestRef(String jsonText, Class<T> objClass, Class<?>... nestParameterizedClasses) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }

        if (String.class.isAssignableFrom(objClass)) {
            return (T) jsonText;
        }

        try {
            LittleType type = null;

            if (ArrayUtils.isEmpty(nestParameterizedClasses)) {
                return toObj(jsonText, objClass);
            }

            if (nestParameterizedClasses.length == 1) {
                return toObjByGenericType(jsonText, objClass, nestParameterizedClasses[0]);
            }

            for (int i = nestParameterizedClasses.length - 1; i >= 0; i--) {
                if (type == null) {
                    type = LittleType.tiny(nestParameterizedClasses[i], nestParameterizedClasses[--i]);
                } else {
                    type = LittleType.tiny(type, nestParameterizedClasses[i]);
                }
            }
            type = LittleType.tiny(type, objClass);

            return mapper.readValue(jsonText, TypeRef.builder().type(type).build());
        } catch (IOException e) {
            throw new SysException(e);
        }
    }

    /**
     * 转换json字符串, 指定实体类和泛型类
     *
     * @param jsonText       json字符串
     * @param objClass       实体类
     * @param genericClasses 泛型类
     * @param <T>
     * @return
     */
    public <T> T toObjByGenericType(String jsonText, Class<T> objClass, Class<?>... genericClasses) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        if (String.class.isAssignableFrom(objClass)) {
            return (T) jsonText;
        }

        JavaType type = mapper.getTypeFactory().constructParametricType(objClass, genericClasses);
        return readByJavaType(jsonText, type);
    }

    public <T> T readByJavaType(String jsonText, JavaType javaType) {
        if (StringUtils.isEmpty(jsonText)) {
            return null;
        }
        try {
            return mapper.readValue(jsonText, javaType);
        } catch (Exception e) {
            logger.error("Parse Json by JavaType#{} error", javaType, e);
            return null;
        }
    }

}
