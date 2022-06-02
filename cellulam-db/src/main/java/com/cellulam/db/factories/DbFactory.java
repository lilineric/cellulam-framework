package com.cellulam.db.factories;

import com.cellulam.db.conf.DbConfiguration;
import org.apache.ibatis.session.SqlSessionFactory;

public class DbFactory {
    private static final SqlSessionFactory sqlSessionFactory = DbConfiguration.getSqlSessionFactory();

    public static final SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
