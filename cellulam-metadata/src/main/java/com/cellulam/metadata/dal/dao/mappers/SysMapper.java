package com.cellulam.metadata.dal.dao.mappers;

import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

public interface SysMapper {
    /**
     * 获取系统时间
     *
     * @return
     */
    @Select("select current_timestamp(6) as dt")
    LocalDateTime getSysTime();
}
