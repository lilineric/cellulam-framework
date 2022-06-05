package com.cellulam.core.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public abstract class TtlExecutors {
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return ttlWrap(Executors.newFixedThreadPool(nThreads));
    }

    public static ExecutorService newWorkStealingPool(int parallelism) {
        return ttlWrap(Executors.newWorkStealingPool(parallelism));
    }

    public static ExecutorService newWorkStealingPool() {
        return ttlWrap(Executors.newWorkStealingPool());
    }

    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return ttlWrap(Executors.newFixedThreadPool(nThreads, threadFactory));
    }

    public static ExecutorService newSingleThreadExecutor() {
        return ttlWrap(Executors.newSingleThreadExecutor());
    }

    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return ttlWrap(Executors.newSingleThreadExecutor());
    }

    public static ExecutorService newCachedThreadPool() {
        return ttlWrap(Executors.newCachedThreadPool());
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return ttlWrap(Executors.newCachedThreadPool(threadFactory));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
        return ttlWrap(Executors.newSingleThreadScheduledExecutor());
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
        return ttlWrap(Executors.newSingleThreadScheduledExecutor(threadFactory));
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return ttlWrap(Executors.newScheduledThreadPool(corePoolSize));
    }

    public static ScheduledExecutorService newScheduledThreadPool(
            int corePoolSize, ThreadFactory threadFactory) {
        return ttlWrap(Executors.newScheduledThreadPool(corePoolSize, threadFactory));
    }

    public static ExecutorService unconfigurableExecutorService(ExecutorService executor) {
        return ttlWrap(Executors.unconfigurableExecutorService(executor));
    }

    public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService executor) {
        return ttlWrap(Executors.unconfigurableScheduledExecutorService(executor));
    }


    public static ExecutorService ttlWrap(ExecutorService executorService) {
        return com.alibaba.ttl.threadpool.TtlExecutors.getTtlExecutorService(executorService);
    }

    public static ScheduledExecutorService ttlWrap(ScheduledExecutorService executorService) {
        return com.alibaba.ttl.threadpool.TtlExecutors.getTtlScheduledExecutorService(executorService);
    }

}
