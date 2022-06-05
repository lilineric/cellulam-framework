package com.cellulam.core.executor.delay;

import com.cellulam.core.concurrent.TtlExecutors;
import com.cellulam.core.utils.LocalDateUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Memory-based delay queue
 */
@Slf4j
public class MemoryQueueDelayExecutor implements DelayExecutor {
    private DelayQueue<DelayedTask> delayQueue = new DelayQueue<>();

    private final ExecutorService threadPool;

    /**
     * New CachedThreadPool to execute delay tasks
     */
    public MemoryQueueDelayExecutor() {
        this(TtlExecutors.newCachedThreadPool());
    }

    /**
     * init
     *
     * @param executePool Thread pool to execute delay tasks.
     */
    public MemoryQueueDelayExecutor(ExecutorService executePool) {
        this.threadPool = executePool;
        this.startDelayConsumer();
    }

    @Override
    public void execute(Runnable runnable, int delayMills) {
        this.delayQueue.add(new DelayedTask(runnable, delayMills));
    }

    @Override
    public void execute(Runnable runnable, LocalDateTime exceptedStartTime) {
        this.delayQueue.add(new DelayedTask(runnable, exceptedStartTime));
    }

    private void startDelayConsumer() {
        new Thread(() -> {
            while (true) {
                try {
                    DelayedTask item = this.delayQueue.take();
                    threadPool.execute(() -> item.execute());
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }).start();
    }

    private static class DelayedTask implements Delayed {

        private long expectedBeginTime;

        private Runnable runnable;

        public DelayedTask(Runnable runnable, int delayMills) {
            this.init(runnable, delayMills + System.currentTimeMillis());
        }

        public DelayedTask(Runnable runnable, LocalDateTime exceptedStartTime) {
            this.init(runnable, LocalDateUtils.toTimestamp(exceptedStartTime));
        }

        private void init(Runnable runnable, long expectedBeginTime) {
            this.runnable = runnable;
            this.expectedBeginTime = expectedBeginTime;
        }


        @Override
        public long getDelay(TimeUnit unit) {
            long diff = expectedBeginTime - System.currentTimeMillis();
            long convert = unit.convert(diff, TimeUnit.MILLISECONDS);
            log.debug("getDelay convert is {}", convert);
            return convert;
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        public void execute() {
            try {
                this.runnable.run();
            } catch (Exception e) {
                log.error("Failed to execute.", e);
            }
        }
    }
}
