package com.cellulam.spring.datasource.configuration.datasources;


import com.cellulam.spring.datasource.configuration.DataSourceConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikariDataSourceBuilder extends AbstractDataSourceBuilder {
    private HikariDataSourceBuilder() {
    }

    private String dataSourceName;

    @Override
    public DataSourceBuilder dataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        return this;
    }

    public static HikariDataSourceBuilder builder() {
        return new HikariDataSourceBuilder();
    }

    @Override
    public DataSource build() {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setDriverClassName(getDataSourceConfig(this.dataSourceName,
                DataSourceConstants.DATASOURCE_DRIVER_CLASSNAME_PARTIAL_KEY));
        hikariConfig.setJdbcUrl(getDataSourceConfig(this.dataSourceName,
                DataSourceConstants.DATASOURCE_JDBC_URL_PARTIAL_KEY));
        hikariConfig.setUsername(getDataSourceConfig(this.dataSourceName,
                DataSourceConstants.DATASOURCE_USERNAME_PARTIAL_KEY));
        hikariConfig.setPassword(getDataSourceConfig(this.dataSourceName,
                DataSourceConstants.DATASOURCE_PASSWORD_PARTIAL_KEY));
        hikariConfig.setReadOnly(getDataSourceConfig(this.dataSourceName,
                DataSourceConstants.DATASOURCE_READONLY_PARTIAL_KEY, Boolean.class, false));

        hikariConfig.setMinimumIdle(getDataSourceRootIntValue(DataSourceConstants.DATASOURCE_MINIMUM_IDLE_KEY,
                DataSourceConstants.DEFAULT_DATASOURCE_MINIMUM_IDLE));
        hikariConfig.setMaximumPoolSize(getDataSourceRootIntValue(DataSourceConstants.DATASOURCE_MAXIMUM_POOL_SIZE_KEY,
                DataSourceConstants.DEFAULT_DATASOURCE_MAXIMUM_POOL_SIZE));
        hikariConfig.setAutoCommit(getDataSourceRootValue(DataSourceConstants.DATASOURCE_AUTO_COMMIT_KEY,
                Boolean.class, DataSourceConstants.DEFAULT_DATASOURCE_AUTO_COMMIT));
        hikariConfig.setIdleTimeout(getDataSourceRootIntValue(DataSourceConstants.DATASOURCE_IDLE_TIMEOUT_KEY,
                DataSourceConstants.DEFAULT_DATASOURCE_IDLE_TIMEOUT));
        hikariConfig.setMaxLifetime(getDataSourceRootIntValue(DataSourceConstants.DATASOURCE_MAX_LIFETIME_KEY,
                DataSourceConstants.DEFAULT_DATASOURCE_MAX_LIFETIME));
        hikariConfig.setConnectionTimeout(getDataSourceRootIntValue(DataSourceConstants.DATASOURCE_CONNECTION_TIMEOUT_KEY,
                DataSourceConstants.DEFAULT_DATASOURCE_CONNECTION_TIMEOUT));
        hikariConfig.setConnectionTestQuery(getDataSourceRootValue(DataSourceConstants.DATASOURCE_CONNECTION_TEST_QUERY_KEY,
                String.class, DataSourceConstants.DEFAULT_DATASOURCE_CONNECTION_TEST_QUERY));

        return new HikariDataSource(hikariConfig);
    }

}
