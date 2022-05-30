package com.cellulam.spring.datasource.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * 数据源 context
 */
public class DataSourceContextHolder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceContextHolder.class);

    /**
     * 默认DataSource
     */
    private static String DEFAULT_DATASOURCE_NAME = null;
    private static DataSource DEFAULT_DATASOURCE = null;

    public static DataSource getDefaultDataSource() {
        return DEFAULT_DATASOURCE;
    }

    public static String getDefaultDataSourceName() {
        return DEFAULT_DATASOURCE_NAME;
    }

    public static void setDefaultDataSource(String dataSourceName, DataSource dataSource) {
        DEFAULT_DATASOURCE_NAME = dataSourceName;
        DEFAULT_DATASOURCE = dataSource;
    }

    //线程本地环境
    private static final ThreadLocal<String> local = new ThreadLocal<>();

    public static ThreadLocal<String> getLocal() {
        return local;
    }

    public static String getDataSource() {
        return local.get();
    }

    public static void clear() {
        local.remove();
    }

    public static void switchDefaultDataSource() {
        switchDataSource(DEFAULT_DATASOURCE_NAME);
    }

    public static void switchDataSource(String dataSource) {
        LOGGER.debug("Switch DataSource: {}", dataSource);
        local.set(dataSource);
    }
}

