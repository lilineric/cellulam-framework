package com.cellulam.core.factories;

import com.cellulam.core.exceptions.ConfigurationException;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class PropertiesFactory {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesFactory.class);

    private static final Map<String, Properties> propertiesMap = Maps.newConcurrentMap();

    public static Properties getProperties(String resourceName) {
        return propertiesMap.computeIfAbsent(resourceName, key -> {
            Properties properties = new Properties();
            try {
                properties.load(PropertiesFactory.class.getClassLoader().getResourceAsStream(key));
            } catch (IOException e) {
                logger.error("Failed to load properties " + key, e);
                throw new ConfigurationException("Failed to load properties " + key, e);
            }
            return properties;
        });
    }
}
