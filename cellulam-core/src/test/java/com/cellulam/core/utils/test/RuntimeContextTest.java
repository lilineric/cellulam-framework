package com.cellulam.core.utils.test;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.cellulam.core.context.RuntimeContext;
import com.cellulam.core.utils.UUIDUtils;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RuntimeContextTest {

    @Test
    public void test() {
        String traceId = UUIDUtils.randomUUID32();
        RuntimeContext.init(traceId);
        int v = 456;
        RuntimeContext.put("test", v);
        Assert.assertEquals(traceId, RuntimeContext.get(RuntimeContext.Keys.TRACE_ID));
        Assert.assertEquals(v, RuntimeContext.get("test"));
        System.out.println(RuntimeContext.getDigest());
        RuntimeContext.remove("test");
        Assert.assertNull(RuntimeContext.get("test"));
        RuntimeContext.clear();
        Assert.assertNull(RuntimeContext.get(RuntimeContext.Keys.TRACE_ID));
        System.out.println(RuntimeContext.getDigest());
    }

    @Test
    public void testThread() {
        String traceId = UUIDUtils.randomUUID32();
        RuntimeContext.init(traceId);
        int v = 456;
        RuntimeContext.put("test", v);
        Assert.assertEquals(traceId, RuntimeContext.get(RuntimeContext.Keys.TRACE_ID));
        Assert.assertEquals(v, RuntimeContext.get("test"));
        System.out.println(RuntimeContext.getDigest());

        AtomicBoolean finished = new AtomicBoolean(false);
        new Thread(() -> {
            try {
                Assert.assertEquals(traceId, RuntimeContext.get(RuntimeContext.Keys.TRACE_ID));
                Assert.assertEquals(v, RuntimeContext.get("test"));
                System.out.println(RuntimeContext.getDigest());
                RuntimeContext.put("test", "child");
                Assert.assertEquals("child", RuntimeContext.get("test"));
            } finally {
                finished.set(true);
            }
        }).start();

        while (!finished.get()) {

        }

        System.out.println(RuntimeContext.getDigest());
    }

    @Test
    public void testThreadPool() throws InterruptedException, ExecutionException {
        ExecutorService threadPool = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(3));

        String traceId = UUIDUtils.randomUUID32();
        RuntimeContext.init(traceId);
        int v = 456;
        RuntimeContext.put("test", v);
        Assert.assertEquals(traceId, RuntimeContext.get(RuntimeContext.Keys.TRACE_ID));
        Assert.assertEquals(v, RuntimeContext.get("test"));
        System.out.println(RuntimeContext.getDigest());

        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            Runnable runnable = () -> {
                try {
                    Assert.assertEquals(traceId, RuntimeContext.get(RuntimeContext.Keys.TRACE_ID));
                    Assert.assertEquals(v, RuntimeContext.get("test"));
                    System.out.println(RuntimeContext.getDigest());
                    String value = "child" + System.currentTimeMillis();
                    RuntimeContext.put("test", value);
                    Assert.assertEquals(value, RuntimeContext.get("test"));
                    System.out.println(RuntimeContext.getDigest());
                    sleep(100);
                    Assert.assertEquals(value, RuntimeContext.get("test"));
                    System.out.println(RuntimeContext.getDigest());
                } finally {
                    countDownLatch.countDown();
                }
            };


            threadPool.execute(runnable);
        }
        countDownLatch.await();

        System.out.println(RuntimeContext.getDigest());
    }

    @Test
    public void testTtl() throws ExecutionException, InterruptedException {
        ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(3));
        TransmittableThreadLocal<String> context = new TransmittableThreadLocal<>();
        // 在父线程中设置
        context.set("value-set-in-parent");

        List<Future<?>> futures = Lists.newCopyOnWriteArrayList();
        for (int i = 0; i < 10; i++) {
            Runnable runnable = () -> {
                Assert.assertEquals("value-set-in-parent", context.get());
                String v = "value-set-in-child-" + System.currentTimeMillis();
                context.set(v);
                sleep(200);
                Assert.assertEquals(v, context.get());
            };
            Future<?> future = executorService.submit(runnable);
            futures.add(future);
        }

        for (Future<?> future : futures) {
            future.get();
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
