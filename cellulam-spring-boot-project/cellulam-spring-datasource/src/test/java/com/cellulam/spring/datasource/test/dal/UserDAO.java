package com.cellulam.spring.datasource.test.dal;

import com.cellulam.spring.datasource.test.po.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDAO {
    int addUser(UserDO user);
    UserDO getById(long id);
    int updateUser(UserDO user);
}
