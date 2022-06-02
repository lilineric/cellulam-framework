package com.cellulam.metadata.worker;

import com.cellulam.core.utils.AssertUtils;
import com.cellulam.metadata.MetadataContext;
import com.cellulam.metadata.dal.dao.WorkerNodeDao;
import com.cellulam.metadata.dal.dao.impl.WorkerNodeDaoImpl;

/**
 * 通过数据库分配workerId
 */
public class DbWorkerIdAssigner implements WorkerIdAssigner {
    private WorkerNodeDao workerNodeDao;

    public DbWorkerIdAssigner() {
        this.workerNodeDao = new WorkerNodeDaoImpl();
    }

    @Override
    public long assign() {
        String appName = MetadataContext.context.getAppName();
        String ip = MetadataContext.context.getIp();
        String port = MetadataContext.context.getPort();

        AssertUtils.isNotEmpty(appName, "AppMetaContext appName cannot be null");
        AssertUtils.isNotEmpty(ip, "AppMetaContext ip cannot be null");
        AssertUtils.isNotEmpty(port, "AppMetaContext port cannot be null");

        return workerNodeDao.assign(appName, ip, port);
    }
}
