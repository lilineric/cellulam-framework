package com.cellulam.metadata.dal.dao.impl;

import com.cellulam.core.utils.LocalDateUtils;
import com.cellulam.db.AbstractDao;
import com.cellulam.metadata.dal.dao.WorkerNodeDao;
import com.cellulam.metadata.dal.dao.mappers.WorkerNodeMapper;
import com.cellulam.metadata.dal.po.WorkerNodeDo;
import com.cellulam.metadata.exceptions.MetadataException;

public class WorkerNodeDaoImpl extends AbstractDao<WorkerNodeMapper> implements WorkerNodeDao {

    @Override
    public long assign(String appName, String ip, String port) {

        return execute(mapper -> {
            WorkerNodeDo workerNodeDo = mapper.getWorkNode(appName, ip, port);
            if (workerNodeDo != null) {
                if (LocalDateUtils.toTimestamp(workerNodeDo.getLaunchDate()) > System.currentTimeMillis()) {
                    throw new MetadataException("Assign workerId failed: clock moved backwards.");
                }
                mapper.updateLaunchTime(workerNodeDo.getId());
            } else {
                workerNodeDo = new WorkerNodeDo();
                workerNodeDo.setAppName(appName);
                workerNodeDo.setIp(ip);
                workerNodeDo.setPort(port);
                mapper.insert(workerNodeDo);
            }
            return workerNodeDo.getId();
        });
    }
}
