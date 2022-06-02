package com.cellulam.db.conf;

public class Constants {
    public static final String DEFAULT_PROPERTIES_RESOURCE_NAME = "cellulam-db.properties";
    public static final String CELLULAM_META_PREFIX = "cellulam.metadata.";
    public static final String JDBC_URL = CELLULAM_META_PREFIX + "jdbc.url";
    public static final String JDBC_USERNAME = CELLULAM_META_PREFIX + "jdbc.username";
    public static final String JDBC_PASSWORD = CELLULAM_META_PREFIX + "jdbc.password";
    public static final String MAPPER_PACKAGE_NAME = CELLULAM_META_PREFIX + "mapper.package-name";
    public static final String DATASOURCE_TYPE = CELLULAM_META_PREFIX + "data-source-type";
}
