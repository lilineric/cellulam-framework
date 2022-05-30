package com.cellulam.spring.datasource.configuration;

import com.cellulam.spring.core.SmartEnvironment;
import com.cellulam.spring.datasource.configuration.datasources.DataSourceBuilder;
import com.cellulam.spring.datasource.configuration.datasources.DataSourceType;
import com.cellulam.spring.datasource.configuration.datasources.HikariDataSourceBuilder;
import com.cellulam.spring.datasource.context.DataSourceContextHolder;
import com.cellulam.spring.datasource.exceptions.DataSourceConfigurationException;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableTransactionManagement(order = Ordered.HIGHEST_PRECEDENCE + 100)
@Order(value = Ordered.HIGHEST_PRECEDENCE + 100)
public class MybatisConfiguration implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisConfiguration.class);

    private DataSource defaultDataSource;

    /**
     * 把所有数据库都放在路由中
     *
     * @return
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource() {

        Map<Object, Object> targetDataSources = buildDataSources();


        //路由类，寻找对应的数据源
        AbstractRoutingDataSource proxy = new AbstractRoutingDataSource() {
            /**
             * 这是AbstractRoutingDataSource类中的一个抽象方法，
             * 而它的返回值是你所要用的数据源dataSource的key值，有了这个key值，
             * targetDataSources就从中取出对应的DataSource，如果找不到，就用配置默认的数据源。
             */
            @Override
            protected Object determineCurrentLookupKey() {
                String typeKey = DataSourceContextHolder.getDataSource();
                LOGGER.debug("Current DataSource: {}", typeKey);
                return typeKey;
            }
        };

        proxy.setDefaultTargetDataSource(defaultDataSource);//默认库
        proxy.setTargetDataSources(targetDataSources);
        return proxy;
    }

    private Map<Object, Object> buildDataSources() {
        String dataSourceNames = SmartEnvironment.getEnvironment().getProperty(DataSourceConstants.DATASOURCE_NAMES_KEY);
        if (StringUtils.isEmpty(dataSourceNames)) {
            throw new DataSourceConfigurationException(DataSourceConstants.DATASOURCE_NAMES_KEY);
        }

        String[] names = dataSourceNames.split(",");
        if (names.length <= 0) {
            throw new DataSourceConfigurationException(DataSourceConstants.DATASOURCE_NAMES_KEY + ":" + dataSourceNames);
        }

        String dataSourceType = SmartEnvironment.getEnvironment()
                .getProperty(DataSourceConstants.DATASOURCE_TYPE_KEY, DataSourceConstants.DEFAULT_DATASOURCE_TYPE);

        DataSourceBuilder dataSourceBuilder;
        switch (DataSourceType.valueOf(dataSourceType.toUpperCase())) {
            case HIKARI:
                dataSourceBuilder = HikariDataSourceBuilder.builder();
                break;
            default:
                throw new DataSourceConfigurationException("Not support datasource type: " + dataSourceType);
        }

        Map<Object, Object> dataSources = Maps.newHashMap();
        for (String name : names) {
            DataSource dataSource = dataSourceBuilder.dataSourceName(name.trim())
                    .build();
            dataSources.put(name, dataSource);
            if (defaultDataSource == null) {
                defaultDataSource = dataSource;
                DataSourceContextHolder.setDefaultDataSource(name, dataSource);
            }
        }
        return dataSources;
    }


    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new CellulamConfigurationCustomizer();
    }

    @Bean
    public SqlSessionFactoryBeanCustomizer sqlSessionFactoryBeanCustomizer(DataSource dataSource) {
        return new CellulamSqlSessionFactoryBeanCustomizer(dataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}

