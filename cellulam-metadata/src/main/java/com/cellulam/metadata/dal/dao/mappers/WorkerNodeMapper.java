package com.cellulam.metadata.dal.dao.mappers;

import com.cellulam.metadata.dal.po.WorkerNodeDo;
import org.apache.ibatis.annotations.*;

public interface WorkerNodeMapper {
    @Select(value = "select id, app_name, worker_id, ip, port, heartbeat, modified, created from t_worker_node where app_name = #{appName} and ip = #{ip} and port = #{port}")
    @Result(column = "app_name", property = "appName")
    @Result(column = "worker_id", property = "workerId")
    WorkerNodeDo getWorkNode(@Param("appName") String appName,
                                 @Param("ip") String ip,
                                 @Param("port") String port);

    @Select(value = "select id, app_name, worker_id, ip, port, heartbeat, modified, created from t_worker_node where id = #{id}")
    @Result(column = "worker_id", property = "workerId")
    @Result(column = "app_name", property = "appName")
    WorkerNodeDo getWorkNodeById(long id);


    @Update(value = "update t_worker_node set heartbeat = now(6) where id = #{id}")
    int heartbeat(long id);

    @Insert(value = "insert t_worker_node (app_name, ip, port, heartbeat, worker_id) value (#{appName}, #{ip}, #{port}, now(6), #{workerId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(WorkerNodeDo workerNodeDo);

    @Select(value = "select ifnull(max(worker_id), 0) from t_worker_node where app_name = #{appName}")
    int getMaxWorkerId(String appName);
}
