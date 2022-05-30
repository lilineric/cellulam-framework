package com.cellulam.spring.datasource.configuration;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SqlSessionFactoryBeanCustomizer;

import javax.sql.DataSource;

public class CellulamSqlSessionFactoryBeanCustomizer implements SqlSessionFactoryBeanCustomizer {

    private DataSource routingDataSource;

    public CellulamSqlSessionFactoryBeanCustomizer(DataSource routingDataSource) {
        this.routingDataSource = routingDataSource;
    }

    @Override
    public void customize(SqlSessionFactoryBean factoryBean) {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(routingDataSource);

        // 读取配置
        sessionFactoryBean.setFailFast(true);

        Interceptor[] plugins = new Interceptor[]{new DataSourceSqlInterceptor()};
        sessionFactoryBean.setPlugins(plugins);

    }
}
