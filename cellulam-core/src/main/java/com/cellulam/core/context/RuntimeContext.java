package com.cellulam.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.cellulam.core.utils.JacksonUtils;
import com.cellulam.core.utils.MapCopyUtils;
import com.cellulam.core.utils.UUIDUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * It's a runtime context for each thread
 * In order to automatically pass contexts across threads, {@link TransmittableThreadLocal} is used to store the value of the context.
 * {@link TransmittableThreadLocal} inherits from {@link InheritableThreadLocal}.
 * If the passed object is of reference type, then it will be shared multiple threads.
 * Therefore the value of the TransmittableThreadLocal must be deep-copied when passed, passing a copy of it instead of reference.
 *
 * @author eric.li
 */
@Slf4j
public final class RuntimeContext {
    private static final TransmittableThreadLocal<HashMap<String, Object>> context = new TransmittableThreadLocal<>();

    public static HashMap<String, Object> init() {
        return init(UUIDUtils.randomUUID32());
    }


    public static HashMap<String, Object> init(String traceId) {
        HashMap<String, Object> data = Maps.newHashMap();
        context.set(data);
        setTraceId(traceId);
        log.info("init runtime context");
        return data;
    }

    public static Object get(String key) {
        HashMap<String, Object> context = getData();
        return context == null ? null : context.get(key);
    }

    public static void put(String key, Object value) {
        HashMap<String, Object> data = getData(true);
        data.put(key, value);
        context.set(data);
    }

    public static void remove(String key) {
        HashMap<String, Object> data = getData();
        if (data != null) {
            data.remove(key);
        }
        context.set(data);
    }

    public static void clear() {
        log.info("clear runtime context");
        MDC.clear();
        context.remove();
    }

    public static String getDigest() {
        Map<String, Object> values = getData();
        return "[" + Thread.currentThread().getName() + "]" + " "
                + (values == null ? null : JacksonUtils.toJson(values));
    }

    public static void setTraceId(String traceId) {
        put(Keys.TRACE_ID, traceId);
        MDC.put(Keys.TRACE_ID, traceId);
    }

    public static HashMap<String, Object> getData(boolean autoInit) {
        HashMap<String, Object> data = MapCopyUtils.deepCopy(context.get());
        if (data == null && autoInit) {
            data = init();
        }
        return data;
    }

    public static HashMap<String, Object> getData() {
        return getData(false);
    }

    public static void init(HashMap<String, Object> contextData) {
        if (contextData != null && contextData.containsKey(Keys.TRACE_ID)) {
            init(contextData.get(Keys.TRACE_ID).toString());
        } else {
            init();
        }

        if (!MapUtils.isEmpty(contextData)) {
            context.set(MapCopyUtils.deepCopy(contextData));
        }
    }

    public final static class Keys {
        public final static String TRACE_ID = "traceId";
    }
}
