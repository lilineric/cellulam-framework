package com.cellulam.metadata.dal.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkerNodeDo {
    private Long id;
    private String appName;
    private Integer workerId;
    private String ip;
    private String port;
    private LocalDateTime heartbeat;
    private LocalDateTime modified;
    private LocalDateTime created;

    public String getInstanceId() {
        return appName + "-" + workerId;
    }
}
