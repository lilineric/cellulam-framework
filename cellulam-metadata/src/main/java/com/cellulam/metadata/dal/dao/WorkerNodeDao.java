package com.cellulam.metadata.dal.dao;

public interface WorkerNodeDao{

    long assign(String appName, String ip, String port);
}
