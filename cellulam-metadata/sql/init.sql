CREATE TABLE t_worker_node
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
    app_name VARCHAR(64) NOT NULL COMMENT 'application name',
    worker_id int not null comment 'worker_id',
    ip VARCHAR(64) NOT NULL COMMENT 'IP',
    port VARCHAR(64) NOT NULL COMMENT 'port',
    heartbeat datetime(6) NOT NULL COMMENT 'last heartbeat time',
    modified datetime(6) NOT NULL default current_timestamp(6) COMMENT 'modified time',
    created datetime(6) NOT NULL default current_timestamp(6) on update current_timestamp(6) COMMENT 'created time',
    unique key idx_app_name_ip_port (app_name, ip, port),
    unique key idx_app_name_worker_id (app_name, worker_id),
    PRIMARY KEY(ID)
)
 COMMENT='WorkerID Assigner',ENGINE = INNODB;