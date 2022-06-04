package com.cellulam.core.json;

import com.cellulam.core.enums.CodeDescriptionFeature;
import com.cellulam.core.enums.EnumUtils;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.util.EnumResolver;

import java.util.HashMap;

/**
 * 忽略大小写枚举和code 反序列化
 */
public class CustomEnumDeserializers extends SimpleDeserializers {

    @Override
    @SuppressWarnings("unchecked")
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config, BeanDescription beanDesc) throws JsonMappingException {
        return createDeserializer((Class<Enum>) type);
    }

    private <T extends Enum<T>> JsonDeserializer<?> createDeserializer(Class<T> enumCls) {
        T[] enumValues = enumCls.getEnumConstants();
        HashMap<String, T> map = createEnumValuesMap(enumValues);
        return new EnumDeserializer(new EnumCaseInsensitiveResolver<T>(enumCls, enumValues, (HashMap<String, Enum<?>>) map, null), true);
    }

    private <T extends Enum<T>> HashMap<String, T> createEnumValuesMap(T[] enumValues) {
        HashMap<String, T> map = new HashMap<>();
        for (int i = enumValues.length; --i >= 0; ) {
            T e = enumValues[i];
            map.put(e.toString(), e);
            if (e instanceof CodeDescriptionFeature) {
                CodeDescriptionFeature cd = (CodeDescriptionFeature) e;
                map.put(String.valueOf(cd.getCode()), e);
            }
        }
        return map;
    }

    public static class EnumCaseInsensitiveResolver<T extends Enum<T>> extends EnumResolver {

        private Class<T> enumClass;

        protected EnumCaseInsensitiveResolver(Class<T> enumClass, Enum<?>[] enums, HashMap<String, Enum<?>> map, Enum<?> defaultValue) {
            super((Class<Enum<?>>) enumClass, enums, map, defaultValue);
            this.enumClass = enumClass;
        }

        @Override
        public T findEnum(String key) {
            return EnumUtils.textToEnum(key, enumClass);
        }
    }
}