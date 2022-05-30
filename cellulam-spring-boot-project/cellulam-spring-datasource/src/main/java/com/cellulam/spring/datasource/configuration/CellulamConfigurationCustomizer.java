package com.cellulam.spring.datasource.configuration;

import com.cellulam.spring.datasource.handlers.DateTimeTypeHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;

import java.time.LocalDateTime;

public class CellulamConfigurationCustomizer implements ConfigurationCustomizer {
    @Override
    public void customize(Configuration configuration) {
        configuration.getTypeHandlerRegistry()
                .register(LocalDateTime.class, JdbcType.BIGINT,
                        new DateTimeTypeHandler());
    }
}
