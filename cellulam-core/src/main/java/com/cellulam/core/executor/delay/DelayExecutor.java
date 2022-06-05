package com.cellulam.core.executor.delay;

import java.time.LocalDateTime;

/**
 * delay execute
 */
public interface DelayExecutor {
    void execute(Runnable runnable, int delayMills);

    void execute(Runnable runnable, LocalDateTime exceptedStartTime);
}
