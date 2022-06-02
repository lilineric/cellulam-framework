package com.cellulam.db.factories;

import com.cellulam.core.exceptions.DbException;
import com.cellulam.db.common.DataSourceType;
import com.cellulam.db.conf.DbProperties;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Map;

public class DbDataSourceFactory {
    private final static Logger logger = LoggerFactory.getLogger(DbDataSourceFactory.class);

    private static final Map<String, DataSource> dataSourceMap = Maps.newConcurrentMap();

    public static DataSource getDataSource(DbProperties properties) {
        return dataSourceMap.computeIfAbsent(properties.getDataSourceType().name(), key -> {
            try {
                return initDataSource(properties.getDataSourceType(), properties);
            } catch (Exception e) {
                logger.error("Failed to init data source: " + key, e);
                throw e;
            }
        });
    }

    private synchronized static DataSource initDataSource(DataSourceType type, DbProperties properties) {
        switch (type) {
            case HIKARI:
                return initHikariDataSource(properties);
            default:
                throw new DbException("Do not support data source: " + properties.getDataSourceType());
        }
    }

    private static DataSource initHikariDataSource(DbProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(properties.getJdbcUrl());
        dataSource.setUsername(properties.getJdbcUsername());
        dataSource.setPassword(properties.getJdbcPassword());
        return dataSource;
    }
}
