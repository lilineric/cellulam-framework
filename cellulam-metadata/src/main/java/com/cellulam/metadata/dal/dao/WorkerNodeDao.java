package com.cellulam.metadata.dal.dao;

import com.cellulam.metadata.dal.po.WorkerNodeDo;

public interface WorkerNodeDao{

    long assign(String appName, String ip, String port);

    boolean heartbeat(long nodeId);

    WorkerNodeDo getWorkerNode(long nodeId);
}
