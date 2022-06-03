package com.cellulam.metadata.common;

public class Constants {
    public static final String DEFAULT_PROPERTIES_RESOURCE_NAME = "cellulam-metadata.properties";
    public static final String CELLULAM_META_PREFIX = "cellulam.metadata.";
    public static final String JDBC_URL = CELLULAM_META_PREFIX + "jdbc.url";
    public static final String JDBC_USERNAME = CELLULAM_META_PREFIX + "jdbc.username";
    public static final String JDBC_PASSWORD = CELLULAM_META_PREFIX + "jdbc.password";
    public static final String APP_NAME = CELLULAM_META_PREFIX + "app-name";
    public static final String SERVER_PORT = CELLULAM_META_PREFIX + "port";
    public static final String DATASOURCE_TYPE = CELLULAM_META_PREFIX + "data-source-type";
    public static final String AUTO_INIT = CELLULAM_META_PREFIX + "auto-init";
    public static final String HEARTBEAT_SECOND = CELLULAM_META_PREFIX + "heartbeat";

    public static final int DEFAULT_HEARTBEAT_SECOND = 30 * 60; //30min
    public static final int TIME_ERROR_RANGE = 1000; //allow time error range
}
