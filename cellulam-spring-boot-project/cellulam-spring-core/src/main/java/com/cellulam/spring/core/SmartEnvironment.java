package com.cellulam.spring.core;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertyResolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * spring-environment, 在 <code>ApplicationEnvironmentPreparedEvent</code> 事件 之后, 设置 environment
 */
public final class SmartEnvironment implements PropertyResolver {

    private static final Logger logger = LoggerFactory.getLogger(SmartEnvironment.class);

    public static final SmartEnvironment env = new SmartEnvironment();

    private static ConfigurableEnvironment environment;

    private SmartEnvironment() {
    }

    /**
     * 初始化 spring-environment. 若已存在, 则重置
     *
     * @param environment
     */
    static void initOrResetEnvironment(ConfigurableEnvironment environment) {
        if (SmartEnvironment.environment == null) {
            System.out.println("init set Environment#" + environment + ", hash#" + System.identityHashCode(environment));
        } else {
            if (SmartEnvironment.environment != environment) {
                System.out.println("override set Environment to " + environment + ", hash#" + System.identityHashCode(environment));
            }
        }

        SmartEnvironment.environment = environment;

        // 此时logger还未准备
        logger.info("set spring-env#{} for smart env", environment);
    }

    @Override
    public boolean containsProperty(String key) {
        return environment.containsProperty(key);
    }

    @Override
    public String getProperty(String key) {
        return environment.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return environment.getRequiredProperty(key);
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
        return environment.getRequiredProperty(key, targetType);
    }

    @Override
    public String resolvePlaceholders(String text) {
        return environment.resolvePlaceholders(text);
    }

    @Override
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        return environment.resolveRequiredPlaceholders(text);
    }

    /**
     * 解析 占位符 , 如果字符串格式符合${\S+}
     *
     * @param value
     * @return
     */
    public String resolveIfNeed(String value) {
        if (StringUtils.isNotBlank(value) && value.trim().startsWith("${")
                && value.trim().endsWith("}")) {
            return env.resolvePlaceholders(value);
        }

        return value;
    }

    /**
     * 解析 占位符 , 如果字符串格式符合${\S+}
     *
     * @param value
     * @return
     */
    public List<String> resolveIfNeed(String[] value) {
        if (value == null || value.length <= 0) {
            return null;
        }

        return Arrays.stream(value)
                .map(this::resolveIfNeed)
                .collect(Collectors.toList());
    }

    /**
     * 解析 占位符 , 如果字符串格式符合${\S+}
     *
     * @param value
     * @return
     */
    public Integer resolveIntegerIfNeed(String value) {
        if (StringUtils.isNotBlank(value) && value.trim().startsWith("${")
                && value.trim().endsWith("}")) {
            value = env.resolvePlaceholders(value);
        }

        return Integer.parseInt(value);
    }

    public static ConfigurableEnvironment getEnvironment() {
        return environment;
    }
}
