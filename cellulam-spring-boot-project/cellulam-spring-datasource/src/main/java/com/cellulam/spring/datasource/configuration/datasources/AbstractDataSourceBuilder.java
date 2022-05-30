package com.cellulam.spring.datasource.configuration.datasources;

import com.cellulam.spring.core.SmartEnvironment;
import com.cellulam.spring.datasource.configuration.DataSourceConstants;
import com.cellulam.spring.datasource.exceptions.DataSourceConfigurationException;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractDataSourceBuilder implements DataSourceBuilder{

    protected <T> T getDataSourceRootValue(String key, Class<T> tClass, T defaultValue) {
        return getDataSourceConfig(null, key, tClass, defaultValue);
    }

    protected <T> T getDataSourceRootValue(String key, Class<T> tClass) {
        return getDataSourceConfig(null, key, tClass);
    }

    protected int getDataSourceRootIntValue(String key, int defaultValue) {
        return getDataSourceConfig(null, key, Integer.class, defaultValue);
    }

    protected int getDataSourceRootIntValue(String key) {
        return getDataSourceConfig(null, key, Integer.class);
    }


    protected String getDataSourceConfig(String dataSource, String key) {
        String propertyKey = getDataSourceConfigKey(dataSource, key);
        String value = SmartEnvironment.getEnvironment().getProperty(propertyKey);
        if (StringUtils.isEmpty(value)) {
            throw new DataSourceConfigurationException(propertyKey + " must not be null");
        }
        return value;
    }

    protected String getDataSourceConfig(String dataSource, String key, String defaultValue) {
        return SmartEnvironment.getEnvironment().getProperty(getDataSourceConfigKey(dataSource, key)
                , defaultValue);
    }

    protected <T> T getDataSourceConfig(String dataSource, String key, Class<T> tClass, T defaultValue) {
        return SmartEnvironment.getEnvironment().getProperty(getDataSourceConfigKey(dataSource, key)
                , tClass, defaultValue);
    }

    protected <T> T getDataSourceConfig(String dataSource, String key, Class<T> tClass) {
        String propertyKey = getDataSourceConfigKey(dataSource, key);
        T value = SmartEnvironment.getEnvironment().getProperty(propertyKey, tClass);
        if (value == null) {
            throw new DataSourceConfigurationException(propertyKey + " must not be null");
        }
        return value;
    }

    protected String getDataSourceConfigKey(String dataSource, String key) {
        return StringUtils.isEmpty(dataSource) ?
                String.format("%s%s",
                        DataSourceConstants.DATASOURCE_PREFIX_KEY,
                        key)
                : String.format("%s%s.%s",
                DataSourceConstants.DATASOURCE_PREFIX_KEY,
                dataSource,
                key);
    }
}
