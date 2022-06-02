package com.cellulam.metadata.dal.dao.impl;

import com.cellulam.core.utils.LocalDateUtils;
import com.cellulam.db.AbstractDao;
import com.cellulam.metadata.MetadataContext;
import com.cellulam.metadata.dal.dao.WorkerNodeDao;
import com.cellulam.metadata.dal.dao.mappers.WorkerNodeMapper;
import com.cellulam.metadata.dal.po.WorkerNodeDo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkerNodeDaoImpl extends AbstractDao<WorkerNodeMapper> implements WorkerNodeDao {

    @Override
    public long assign(String appName, String ip, String port) {

        return execute(mapper -> {
            WorkerNodeDo workerNodeDo = mapper.getWorkNode(appName, ip, port);
            if (workerNodeDo != null) {
                mapper.heartbeat(workerNodeDo.getId());
            } else {
                workerNodeDo = new WorkerNodeDo();
                workerNodeDo.setAppName(appName);
                workerNodeDo.setIp(ip);
                workerNodeDo.setPort(port);
                workerNodeDo.setWorkerId(mapper.getMaxWorkerId(appName) + 1);
                mapper.insert(workerNodeDo);
            }
            return workerNodeDo.getId();
        });
    }

    @Override
    public boolean heartbeat(long nodeId) {
        return execute(mapper -> mapper.heartbeat(nodeId) > 0);
    }

    @Override
    public WorkerNodeDo getWorkerNode(long nodeId) {
        return execute(workerNodeMapper -> workerNodeMapper.getWorkNodeById(nodeId));
    }
}
