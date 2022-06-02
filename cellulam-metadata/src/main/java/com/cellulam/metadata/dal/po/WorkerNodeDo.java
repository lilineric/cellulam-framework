package com.cellulam.metadata.dal.po;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkerNodeDo {
    private Long id;
    private String appName;
    private String ip;
    private String port;
    private LocalDateTime launchDate;
    private LocalDateTime modified;
    private LocalDateTime created;
}
