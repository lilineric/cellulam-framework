package com.cellulam.db.conf;

import com.cellulam.core.factories.PropertiesFactory;
import com.cellulam.db.factories.DbDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.util.Properties;

public class DbConfiguration {
    private static SqlSessionFactory sqlSessionFactory = null;

    public static void initConfiguration() {
        initConfiguration(Constants.DEFAULT_PROPERTIES_RESOURCE_NAME);
    }

    public static void initConfiguration(Properties properties) {
        initConfiguration(DbProperties.loadFromProperties(properties));
    }

    public static void initConfiguration(String resourceName) {
        initConfiguration(PropertiesFactory.getProperties(resourceName));
    }

    public static void initConfiguration(DbProperties properties) {
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, DbDataSourceFactory.getDataSource(properties));
        Configuration configuration = new Configuration(environment);
        configuration.addMappers(properties.getMapperPackage());
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
