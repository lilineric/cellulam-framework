package com.cellulam.metadata.worker;

public interface WorkerIdAssigner {
    void assign();

    void heartbeat();
}
