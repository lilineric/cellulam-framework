package com.cellulam.metadata.dal.dao.mappers;

import com.cellulam.metadata.dal.po.WorkerNodeDo;
import org.apache.ibatis.annotations.*;

public interface WorkerNodeMapper {
    @Select(value = "select id, app_name, ip, port, launch_date, modified, created from t_worker_node where app_name = #{appName} and ip = #{ip} and port = #{port}")
    WorkerNodeDo getWorkNode(@Param("appName") String appName,
                             @Param("ip") String ip,
                             @Param("port") String port);


    @Update(value = "update t_worker_node set launch_date = now(6) where id = #{id}")
    int updateLaunchTime(long id);

    @Insert(value = "insert t_worker_node (app_name, ip, port, launch_date) value (#{appName}, #{ip}, #{port}, now(6))")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(WorkerNodeDo workerNodeDo);
}
