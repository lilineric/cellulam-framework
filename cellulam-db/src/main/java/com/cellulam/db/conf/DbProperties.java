package com.cellulam.db.conf;

import com.cellulam.db.common.DataSourceType;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

@Getter
public class DbProperties {
    private DataSourceType dataSourceType;
    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;
    private String mapperPackage;

    public static final DbProperties loadFromProperties(Properties properties) {
        String dataSourceType = properties.getProperty(Constants.DATASOURCE_TYPE);
        return Builder
                .builder()
                .jdbcUrl(properties.getProperty(Constants.JDBC_URL))
                .jdbcUsername(properties.getProperty(Constants.JDBC_USERNAME))
                .jdbcPassword(properties.getProperty(Constants.JDBC_PASSWORD))
                .mapperPackage(properties.getProperty(Constants.MAPPER_PACKAGE_NAME))
                .dataSourceType(StringUtils.isEmpty(dataSourceType) ? DataSourceType.getDefaultType() : DataSourceType.valueOf(dataSourceType))
                .build();
    }


    public static final class Builder {
        private String jdbcUrl;
        private String jdbcUsername;
        private String jdbcPassword;
        private String mapperPackage;
        private DataSourceType dataSourceType = DataSourceType.getDefaultType();

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public Builder jdbcUsername(String jdbcUsername) {
            this.jdbcUsername = jdbcUsername;
            return this;
        }

        public Builder jdbcPassword(String jdbcPassword) {
            this.jdbcPassword = jdbcPassword;
            return this;
        }

        public Builder mapperPackage(String mapperPackage) {
            this.mapperPackage = mapperPackage;
            return this;
        }

        public Builder dataSourceType(DataSourceType dataSourceType) {
            if (dataSourceType != null) {
                this.dataSourceType = dataSourceType;
            }
            return this;
        }

        public DbProperties build() {
            DbProperties dbProperties = new DbProperties();
            dbProperties.jdbcPassword = this.jdbcPassword;
            dbProperties.mapperPackage = this.mapperPackage;
            dbProperties.jdbcUrl = this.jdbcUrl;
            dbProperties.jdbcUsername = this.jdbcUsername;
            dbProperties.dataSourceType = this.dataSourceType;
            return dbProperties;
        }
    }
}
