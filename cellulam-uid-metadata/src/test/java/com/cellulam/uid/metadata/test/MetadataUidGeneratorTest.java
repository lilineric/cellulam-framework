package com.cellulam.uid.metadata.test;

import com.cellulam.core.utils.LocalDateUtils;
import com.cellulam.metadata.DefaultMetadataContextInitializer;
import com.cellulam.uid.UidGenerator;
import com.cellulam.uid.exceptions.UidGenerateException;
import com.cellulam.uid.metadata.MetadataUidGenerator;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MetadataUidGeneratorTest {

    @BeforeClass
    public static void beforeClass() {
        new DefaultMetadataContextInitializer();
    }

    @Test
    public void testNextId() {
        UidGenerator UidGenerator = new MetadataUidGenerator();
        long id1 = UidGenerator.nextId();
        long id2 = UidGenerator.nextId();
        Assert.assertNotEquals(id1, id2);
        System.out.println(id1);
        System.out.println(id2);
        Assert.assertNotEquals(id1 , id2);
    }

    @Test
    public void testNextId2() {
        UidGenerator UidGenerator = new MetadataUidGenerator(System.currentTimeMillis());
        long id1 = UidGenerator.nextId();
        long id2 = UidGenerator.nextId();
        System.out.println(id1);
        System.out.println(id2);
        Assert.assertNotEquals(id1 , id2);
    }

    @Test
    public void testNextId3() {
        long epoch = LocalDateUtils.toTimestamp(LocalDateTime.now().minusYears(17));
        UidGenerator UidGenerator = new MetadataUidGenerator(epoch);
        long id1 = UidGenerator.nextId();
        long id2 = UidGenerator.nextId();
        Assert.assertNotEquals(id1, id2);
        System.out.println(id1);
        System.out.println(id2);
    }

    @Test(expected = UidGenerateException.class)
    public void testException() {
        long epoch = LocalDateUtils.toTimestamp(LocalDateTime.now().minusYears(18));
        UidGenerator UidAppendGenerator = new MetadataUidGenerator(epoch);
        UidAppendGenerator.nextId();
    }

    @Test
    public void performanceTest() {
        int testNum = 100;

        Set<Long> ids = Sets.newHashSet();

        UidGenerator UidGenerator = new MetadataUidGenerator();
        long start = System.currentTimeMillis();
        for (int i = 0; i < testNum; i++) {
            ids.add(UidGenerator.nextId());
        }
        long endTime = System.currentTimeMillis();
        Assert.assertEquals(testNum, ids.size());
        System.out.println("共生成id[" + ids.size() + "] 个,共花费" + (endTime - start) + "ms");
    }

    @Test
    public void concurrentTest() throws InterruptedException {
        int testNum = 100;
        int threadCount = 10;

        Set<Long> ids = Sets.newConcurrentHashSet();

        CountDownLatch countDownLatch = new CountDownLatch(testNum);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        final UidGenerator UidGenerator = new MetadataUidGenerator();
        long start = System.currentTimeMillis();
        for (int i = 0; i < testNum; i++) {
            pool.execute(() -> {
                ids.add(UidGenerator.nextId());
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        Assert.assertEquals(testNum, ids.size());
        System.out.println("共生成id[" + ids.size() + "] 个,共花费" + (endTime - start) + "ms");
    }
}
