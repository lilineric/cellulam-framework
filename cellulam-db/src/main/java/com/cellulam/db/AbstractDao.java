package com.cellulam.db;

import com.cellulam.core.exceptions.DbException;
import com.cellulam.core.utils.ClassUtil;
import com.cellulam.db.factories.DbFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.function.Function;

public abstract class AbstractDao<MAPPER> {
    protected <R> R execute(Function<MAPPER, R> executor) {
        SqlSession sqlSession = this.getSqlSessionFactory().openSession(false);
        MAPPER mapper = sqlSession.getMapper(getMapper());
        try {
            R result = executor.apply(mapper);
            sqlSession.commit();
            return result;
        } catch (Exception e) {
            sqlSession.rollback();
            throw new DbException("Failed execute sql.", e);
        } finally {
            sqlSession.close();
        }
    }

    protected SqlSessionFactory getSqlSessionFactory() {
        return DbFactory.getSqlSessionFactory();
    }

    protected Class<MAPPER> getMapper() {
        return ClassUtil.getSuperClassGenericType(getClass(), 0);
    }
}
