package com.cellulam.core.executor.delay;

import com.cellulam.core.executor.Executor;

import java.time.LocalDateTime;

/**
 * delay execute
 */
public interface DelayExecutor {
    void execute(Executor executor, int delayMills);

    void execute(Executor executor, LocalDateTime exceptedStartTime);
}
