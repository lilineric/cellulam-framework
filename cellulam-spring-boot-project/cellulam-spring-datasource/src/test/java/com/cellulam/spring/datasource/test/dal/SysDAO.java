package com.cellulam.spring.datasource.test.dal;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface SysDAO {
    LocalDateTime getSysDate();
}
