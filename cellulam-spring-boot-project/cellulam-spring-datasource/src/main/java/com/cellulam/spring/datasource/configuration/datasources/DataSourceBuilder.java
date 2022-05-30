package com.cellulam.spring.datasource.configuration.datasources;

import javax.sql.DataSource;

public interface DataSourceBuilder {
    DataSource build();

    DataSourceBuilder dataSourceName(String dataSourceName);
}
