package com.cellulam.script.loader;

import com.cellulam.script.exceptions.ScriptException;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

/**
 * cached the class
 */
public class GroovyScriptUtils {
    /**
     * load class
     *
     * @param script
     * @return
     */
    public static Class<?> loadClass(String script) {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        String classKey = String.valueOf(script.hashCode());
        return GroovyScriptClassCache.INSTANCE.computeIfAbsent(classKey,
                x -> groovyClassLoader.parseClass(script));
    }

    public static GroovyObject newInstance(String script) {
        try {
            return (GroovyObject) loadClass(script).newInstance();
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }
}
