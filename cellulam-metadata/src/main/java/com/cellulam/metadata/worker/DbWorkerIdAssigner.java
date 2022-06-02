package com.cellulam.metadata.worker;

import com.cellulam.core.utils.AssertUtils;
import com.cellulam.core.utils.LocalDateUtils;
import com.cellulam.metadata.MetadataContext;
import com.cellulam.metadata.dal.dao.WorkerNodeDao;
import com.cellulam.metadata.dal.dao.impl.WorkerNodeDaoImpl;
import com.cellulam.metadata.dal.po.WorkerNodeDo;
import lombok.extern.slf4j.Slf4j;

/**
 * 通过数据库分配workerId
 */
@Slf4j
public class DbWorkerIdAssigner implements WorkerIdAssigner {
    private WorkerNodeDao workerNodeDao;

    public DbWorkerIdAssigner() {
        this.workerNodeDao = new WorkerNodeDaoImpl();
    }

    @Override
    public void assign() {
        WorkerNodeDo workerNodeDo = buildWorkerNodeFromContext();

        long nodeId = workerNodeDao.assign(workerNodeDo.getAppName(), workerNodeDo.getIp(), workerNodeDo.getPort());
        this.refreshMetadataContext(nodeId);
    }

    private void refreshMetadataContext(long nodeId) {
        WorkerNodeDo workerNode = workerNodeDao.getWorkerNode(nodeId);

        MetadataContext.context.setWorkerId(workerNode.getWorkerId());
        MetadataContext.context.setLastHeartbeat(workerNode.getHeartbeat());
        MetadataContext.context.setNodeId(workerNode.getId());
        MetadataContext.context.setInstanceId(workerNode.getInstanceId());
    }

    @Override
    public void heartbeat() {
        WorkerNodeDo workerNodeDo = this.buildWorkerNodeFromContext();
        if (this.workerNodeDao.heartbeat(workerNodeDo.getId())) {
            this.refreshMetadataContext(workerNodeDo.getId());
        } else {
            this.assign();
        }
    }

    private WorkerNodeDo buildWorkerNodeFromContext() {
        WorkerNodeDo workerNodeDo = new WorkerNodeDo();
        workerNodeDo.setId(MetadataContext.context.getNodeId());
        workerNodeDo.setAppName(MetadataContext.context.getAppName());
        workerNodeDo.setWorkerId(MetadataContext.context.getWorkerId());
        workerNodeDo.setIp(MetadataContext.context.getIp());
        workerNodeDo.setPort(MetadataContext.context.getPort());
        workerNodeDo.setHeartbeat(MetadataContext.context.getLastHeartbeat());

        AssertUtils.isNotEmpty(workerNodeDo.getAppName(), "AppMetaContext appName cannot be null");
        AssertUtils.isNotEmpty(workerNodeDo.getIp(), "AppMetaContext ip cannot be null");
        AssertUtils.isNotEmpty(workerNodeDo.getPort(), "AppMetaContext port cannot be null");

        return workerNodeDo;
    }
}
