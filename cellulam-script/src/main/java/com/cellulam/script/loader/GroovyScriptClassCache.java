package com.cellulam.script.loader;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.function.Function;

public class GroovyScriptClassCache {
    private static final Map<String, Class<?>> GROOVY_SCRIPT_CLASS_CACHE = Maps.newConcurrentMap();

    private GroovyScriptClassCache() {
    }

    public static GroovyScriptClassCache INSTANCE = new GroovyScriptClassCache();

    public Class<?> computeIfAbsent(String key, Function<String, Class<?>> mappingFunction) {
        return GROOVY_SCRIPT_CLASS_CACHE.computeIfAbsent(key, mappingFunction);
    }

    public Class<?> get(String key) {
        return GROOVY_SCRIPT_CLASS_CACHE.get(key);
    }

    public void put(String key, Class<?> clazz) {
        GROOVY_SCRIPT_CLASS_CACHE.put(key, clazz);
    }

    public boolean containsKey(String key) {
        return GROOVY_SCRIPT_CLASS_CACHE.containsKey(key);
    }
}
