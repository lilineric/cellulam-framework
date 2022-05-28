package com.cellulam.uid.test;

import com.cellulam.uid.UidGenerator;
import com.cellulam.uid.showflake.SnowflakeConfig;
import com.cellulam.uid.showflake.SnowflakeGenerator;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SnowflakeGeneratorTest {

    @Test
    public void testNextId() {
        UidGenerator uidGenerator = new SnowflakeGenerator(SnowflakeConfig.Builder
                .builder(2, 5)
                .build());
        long id1 = uidGenerator.nextId();
        long id2 = uidGenerator.nextId();
        Assert.assertNotEquals(id1, id2);
    }

    @Test
    public void performanceTest() {
        int testNum = 100;

        Set<Long> ids = Sets.newHashSet();

        UidGenerator uidGenerator = new SnowflakeGenerator(SnowflakeConfig.Builder
                .builder(10, 8)
                .build());
        long start = System.currentTimeMillis();
        for (int i = 0; i < testNum; i++) {
            ids.add(uidGenerator.nextId());
        }
        long endTime = System.currentTimeMillis();
        Assert.assertEquals(testNum, ids.size());
        System.out.println("共生成id[" + ids.size() + "] 个,共花费" + (endTime - start) + "ms");
    }

    @Test
    public void concurrentTest () throws InterruptedException {
        int testNum = 100;
        int threadCount = 10;

        Set<Long> ids = Sets.newConcurrentHashSet();

        CountDownLatch countDownLatch = new CountDownLatch(testNum);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        final UidGenerator uidGenerator = new SnowflakeGenerator(SnowflakeConfig.Builder
                .builder(10, 8)
                .build());
        long start = System.currentTimeMillis();
        for (int i = 0; i < testNum; i++) {
            pool.execute(() -> {
                ids.add(uidGenerator.nextId());
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        Assert.assertEquals(testNum, ids.size());
        System.out.println("共生成id[" + ids.size() + "] 个,共花费" + (endTime - start) + "ms");
    }
}
