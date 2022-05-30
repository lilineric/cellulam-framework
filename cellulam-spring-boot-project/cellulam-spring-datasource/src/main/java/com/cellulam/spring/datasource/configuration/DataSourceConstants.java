package com.cellulam.spring.datasource.configuration;

import com.cellulam.spring.datasource.configuration.datasources.DataSourceType;

public abstract class DataSourceConstants {
    public final static String DATASOURCE_PREFIX_KEY = "cellulam.datasource.";

    public final static String DATASOURCE_NAMES_KEY = DATASOURCE_PREFIX_KEY + "names";
    public final static String DATASOURCE_TYPE_KEY = DATASOURCE_NAMES_KEY + "type";

    public final static String DATASOURCE_MINIMUM_IDLE_KEY = "minimum-idle";
    public final static String DATASOURCE_MAXIMUM_POOL_SIZE_KEY = "maximum-pool-size";
    public final static String DATASOURCE_AUTO_COMMIT_KEY = "auto-commit";
    public final static String DATASOURCE_IDLE_TIMEOUT_KEY = "idle-timeout";
    public final static String DATASOURCE_MAX_LIFETIME_KEY = "max-lifetime";
    public final static String DATASOURCE_CONNECTION_TIMEOUT_KEY = "connection-timeout";
    public final static String DATASOURCE_CONNECTION_TEST_QUERY_KEY = "connection-test-query";

    public final static String DATASOURCE_DRIVER_CLASSNAME_PARTIAL_KEY = "driver-class-name";
    public final static String DATASOURCE_JDBC_URL_PARTIAL_KEY = "url";
    public final static String DATASOURCE_USERNAME_PARTIAL_KEY = "username";
    public final static String DATASOURCE_PASSWORD_PARTIAL_KEY = "password";
    public final static String DATASOURCE_READONLY_PARTIAL_KEY = "read-only";


    public static final int DEFAULT_DATASOURCE_MINIMUM_IDLE = 5;
    public static final int DEFAULT_DATASOURCE_MAXIMUM_POOL_SIZE = 20;
    public static final boolean DEFAULT_DATASOURCE_AUTO_COMMIT = true;
    public static final int DEFAULT_DATASOURCE_IDLE_TIMEOUT = 600000;
    public static final int DEFAULT_DATASOURCE_MAX_LIFETIME = 1800000;
    public static final int DEFAULT_DATASOURCE_CONNECTION_TIMEOUT = 1000;
    public static final String DEFAULT_DATASOURCE_CONNECTION_TEST_QUERY = "select 1";

    public final static String DEFAULT_DATASOURCE_TYPE = DataSourceType.HIKARI.name();
}
