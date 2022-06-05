package com.cellulam.core.executor.delay;

import com.cellulam.core.executor.Executor;
import com.cellulam.core.utils.LocalDateUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.*;

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
        this(Executors.newCachedThreadPool());
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
    public void execute(Executor executor, int delayMills) {
        this.delayQueue.add(new DelayedTask(executor, delayMills));
    }

    @Override
    public void execute(Executor executor, LocalDateTime exceptedStartTime) {
        this.delayQueue.add(new DelayedTask(executor, exceptedStartTime));
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

        private Executor executor;

        public DelayedTask(Executor executor, int delayMills) {
            this.init(executor, delayMills + System.currentTimeMillis());
        }

        public DelayedTask(Executor executor, LocalDateTime exceptedStartTime) {
            this.init(executor, LocalDateUtils.toTimestamp(exceptedStartTime));
        }

        private void init(Executor executor, long expectedBeginTime) {
            this.executor = executor;
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
                this.executor.execute();
            } catch (Exception e) {
                log.error("Failed to execute.", e);
            }
        }
    }
}
