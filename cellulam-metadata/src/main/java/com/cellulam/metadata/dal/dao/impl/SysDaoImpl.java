package com.cellulam.metadata.dal.dao.impl;

import com.cellulam.db.AbstractDao;
import com.cellulam.metadata.dal.dao.SysDao;
import com.cellulam.metadata.dal.dao.mappers.SysMapper;

import java.time.LocalDateTime;

public class SysDaoImpl extends AbstractDao<SysMapper> implements SysDao {
    @Override
    public LocalDateTime getSysTime() {
        return execute(mapper -> mapper.getSysTime());
    }
}
