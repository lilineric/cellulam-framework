package com.cellulam.uid.metadata.test;

import com.cellulam.core.utils.LocalDateUtils;
import com.cellulam.metadata.DefaultMetadataContextInitializer;
import com.cellulam.uid.exceptions.UidGenerateException;
import com.cellulam.uid.metadata.MetadataAppendUidGenerator;
import com.cellulam.uid.metadata.UidAppendGenerator;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MetadataUidAppendGeneratorTest {

    @BeforeClass
    public static void beforeClass() {
        new DefaultMetadataContextInitializer();
    }

    @Test
    public void testNextId() {
        UidAppendGenerator UidAppendGenerator = new MetadataAppendUidGenerator();
        long id1 = UidAppendGenerator.nextId();
        long id2 = UidAppendGenerator.nextId(id1);
        long id3 = UidAppendGenerator.nextId(id2);
        Assert.assertNotEquals(id1, id2);
        Assert.assertNotEquals(id2, id3);
        System.out.println(id1);
        System.out.println(id2);
        System.out.println(id3);
        Assert.assertEquals(id1 % 100, id2 % 100);
        Assert.assertEquals(id1 % 100, id3 % 100);
    }

    @Test
    public void testNextId2() {
        UidAppendGenerator UidAppendGenerator = new MetadataAppendUidGenerator(System.currentTimeMillis() / 1000, 1);
        long id1 = UidAppendGenerator.nextId();
        long id2 = UidAppendGenerator.nextId(id1);
        System.out.println(id1);
        System.out.println(id2);
        Assert.assertEquals(id1 % 10, id2 % 10);
    }

    @Test
    public void testNextId3() {
        long epoch = LocalDateUtils.toTimestamp(LocalDateTime.now().minusYears(17));
        UidAppendGenerator UidAppendGenerator = new MetadataAppendUidGenerator(TimeUnit.MILLISECONDS.toSeconds(epoch));
        long id1 = UidAppendGenerator.nextId();
        long id2 = UidAppendGenerator.nextId(id1);
        Assert.assertNotEquals(id1, id2);
        System.out.println(id1);
        System.out.println(id2);
        Assert.assertEquals(id1 % 100, id2 % 100);
    }

    @Test(expected = UidGenerateException.class)
    public void testException() {
        long epoch = LocalDateUtils.toTimestamp(LocalDateTime.now().minusYears(18));
        UidAppendGenerator UidAppendGenerator = new MetadataAppendUidGenerator(TimeUnit.MILLISECONDS.toSeconds(epoch));
        UidAppendGenerator.nextId();
    }

    @Test
    public void performanceTest() {
        int testNum = 100;

        Set<Long> ids = Sets.newHashSet();

        UidAppendGenerator UidAppendGenerator = new MetadataAppendUidGenerator();
        long start = System.currentTimeMillis();
        for (int i = 0; i < testNum; i++) {
            ids.add(UidAppendGenerator.nextId());
        }
        long endTime = System.currentTimeMillis();
        Assert.assertEquals(testNum, ids.size());
        System.out.println("共生成id[" + ids.size() + "] 个,共花费" + (endTime - start) + "ms");
    }

    @Test
    public void concurrentTest() throws InterruptedException {
//        int testNum = 100;
        int testNum = 100;
        int threadCount = 10;

        Set<Long> ids = Sets.newConcurrentHashSet();

        CountDownLatch countDownLatch = new CountDownLatch(testNum);

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);

        final UidAppendGenerator uidAppendGenerator = new MetadataAppendUidGenerator();
        long baseId = uidAppendGenerator.nextId();
        long start = System.currentTimeMillis();
        for (int i = 0; i < testNum; i++) {
            pool.execute(() -> {
                ids.add(uidAppendGenerator.nextId(baseId));
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        long endTime = System.currentTimeMillis();
        Assert.assertEquals(testNum, ids.size());
        System.out.println("共生成id[" + ids.size() + "] 个,共花费" + (endTime - start) + "ms");
    }
}
