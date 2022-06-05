package com.cellulam.core.context;

import com.cellulam.core.utils.JacksonUtils;
import com.cellulam.core.utils.UUIDUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;

import java.util.Map;

/**
 * It's a runtime context for each thread
 */
@Slf4j
public final class RuntimeContext {
    private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<>();

    public static Map<String, Object> init() {
        return init(UUIDUtils.randomUUID32());
    }


    public static Map<String, Object> init(String traceId) {
        Map<String, Object> data = Maps.newConcurrentMap();
        context.set(data);
        setTraceId(traceId);
        log.info("init runtime context");
        return data;
    }

    public static Object get(String key) {
        return getContextData().get(key);
    }

    public static void put(String key, Object value) {
        getContextData().put(key, value);
    }

    public static void remove(String key) {
        getContextData().remove(key);
    }

    public static void clear() {
        log.info("clear runtime context");
        MDC.clear();
        context.remove();
    }

    public static String getDigest() {
        return JacksonUtils.toJson(getContextData());
    }

    public static void setTraceId(String traceId) {
        put(Keys.TRACE_ID, traceId);
        MDC.put(Keys.TRACE_ID, traceId);
    }

    public static Map<String, Object> getContextData() {
        Map<String, Object> data = context.get();
        if (data == null) {
            data = init();
        }
        return data;
    }

    public static void init(Map<String, Object> contextData) {
        if (contextData != null && contextData.containsKey(Keys.TRACE_ID)) {
            init(contextData.get(Keys.TRACE_ID).toString());
        } else {
            init();
        }

        if (!MapUtils.isEmpty(contextData)) {
            for (String key : contextData.keySet()) {
                Object value = contextData.get(key);
                if (Keys.TRACE_ID.equals(key)) {
                    setTraceId(value.toString());
                } else {
                    put(key, value);
                }
            }
        }
    }

    public final static class Keys {
        public final static String TRACE_ID = "traceId";
    }
}
