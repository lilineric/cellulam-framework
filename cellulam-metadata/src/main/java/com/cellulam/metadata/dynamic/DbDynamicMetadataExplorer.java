package com.cellulam.metadata.dynamic;

import com.cellulam.metadata.dal.dao.SysDao;
import com.cellulam.metadata.dal.dao.impl.SysDaoImpl;

import java.time.LocalDateTime;

public class DbDynamicMetadataExplorer implements DynamicMetadataExplorer{

    private SysDao sysDao;

    public DbDynamicMetadataExplorer() {
        this.sysDao = new SysDaoImpl();
    }

    @Override
    public LocalDateTime getSysTime() {
        return sysDao.getSysTime();
    }
}
