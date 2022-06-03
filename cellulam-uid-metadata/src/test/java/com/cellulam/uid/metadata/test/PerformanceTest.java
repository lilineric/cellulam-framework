package com.cellulam.uid.metadata.test;

import com.cellulam.metadata.DefaultMetadataContextInitializer;
import com.cellulam.uid.UidGenerator;
import com.cellulam.uid.metadata.MetadataAppendUidGenerator;
import com.cellulam.uid.metadata.MetadataUidGenerator;
import com.cellulam.uid.snowflake.SnowflakeConfig;
import com.cellulam.uid.snowflake.SnowflakeGenerator;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PerformanceTest {

    @BeforeClass
    public static void beforeClass() {
        new DefaultMetadataContextInitializer();
    }

    @Test
    public void test() throws InterruptedException {
        performance(new MetadataAppendUidGenerator());
        performance(new SnowflakeGenerator(SnowflakeConfig.Builder
                .builder(1)
                .build()));

        performance(new MetadataUidGenerator());
    }

    public void performance(UidGenerator generator) throws InterruptedException {
        int testNum = 100;
        int threadCount = 10;

        Set<Long> ids = Sets.newConcurrentHashSet();

        CountDownLatch countDownLatch = new CountDownLatch(testNum);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        long start = System.currentTimeMillis();
        for (int i = 0; i < testNum; i++) {
            pool.execute(() -> {
                ids.add(generator.nextId());
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        Assert.assertEquals(testNum, ids.size());
        System.out.println(generator.getClass() + "共生成id[" + ids.size() + "] 个,共花费" + (endTime - start) + "ms");
    }
}
