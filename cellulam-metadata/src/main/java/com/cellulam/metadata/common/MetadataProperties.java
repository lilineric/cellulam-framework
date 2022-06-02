package com.cellulam.metadata.common;

import com.cellulam.db.common.DataSourceType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

@Getter
public class MetadataProperties {
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private String appName;
    private String port;
    private DataSourceType dataSourceType;

    public static final MetadataProperties loadFromProperties(Properties properties) {
        return MetadataPropertiesBuilder
                .builder()
                .appName(properties.getProperty(Constants.APP_NAME))
                .port(properties.getProperty(Constants.SERVER_PORT))
                .jdbcUrl(properties.getProperty(Constants.JDBC_URL))
                .jdbcUsername(properties.getProperty(Constants.JDBC_USERNAME))
                .jdbcPassword(properties.getProperty(Constants.JDBC_PASSWORD))
                .dataSourceType(properties.getProperty(Constants.DATASOURCE_TYPE))
                .build();
    }

    public static final class MetadataPropertiesBuilder {
        private String jdbcUrl;
        private String jdbcUsername;
        private String jdbcPassword;
        private String appName;
        private String port;
        private DataSourceType dataSourceType;

        private MetadataPropertiesBuilder() {
        }

        public static MetadataPropertiesBuilder builder() {
            return new MetadataPropertiesBuilder();
        }

        public MetadataPropertiesBuilder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public MetadataPropertiesBuilder jdbcUsername(String jdbcUsername) {
            this.jdbcUsername = jdbcUsername;
            return this;
        }

        public MetadataPropertiesBuilder jdbcPassword(String jdbcPassword) {
            this.jdbcPassword = jdbcPassword;
            return this;
        }

        public MetadataPropertiesBuilder appName(String appName) {
            this.appName = appName;
            return this;
        }

        public MetadataPropertiesBuilder port(String port) {
            this.port = port;
            return this;
        }

        public MetadataPropertiesBuilder dataSourceType(String dataSourceType) {
            if (StringUtils.isNotEmpty(dataSourceType)) {
                this.dataSourceType = DataSourceType.valueOf(dataSourceType);
            }
            return this;
        }

        public MetadataProperties build() {
            MetadataProperties metadataProperties = new MetadataProperties();
            metadataProperties.jdbcUsername = this.jdbcUsername;
            metadataProperties.jdbcUrl = this.jdbcUrl;
            metadataProperties.appName = this.appName;
            metadataProperties.jdbcPassword = this.jdbcPassword;
            metadataProperties.port = this.port;
            metadataProperties.dataSourceType = this.dataSourceType;
            return metadataProperties;
        }
    }
}
