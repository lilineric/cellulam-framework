package com.cellulam.core.utils.test;

import com.cellulam.core.lock.SegmentLock;
import com.cellulam.core.utils.ThreadUtils;
import com.cellulam.core.utils.UUIDUtils;
import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author eric.li
 * @date 2022-06-06 01:55
 */
public class SegmentLockTest {
    @Test
    public void test() throws InterruptedException {
        List<Order> orders = initOrders();

        long start = System.currentTimeMillis();
        Map<Long, Long> userAmountSerialize = this.calcAmount(orders);
        System.out.println("Serialize: " + (System.currentTimeMillis() - start) / 1000 + " seconds");

        start = System.currentTimeMillis();
        Map<Long, Long> userAmountLock = this.calcAmountLock(orders);
        System.out.println("lock: " + (System.currentTimeMillis() - start) / 1000 + " seconds");

        start = System.currentTimeMillis();
        Map<Long, Long> userAmountSync = this.calcAmountSync(orders);
        System.out.println("sync: " + (System.currentTimeMillis() - start) / 1000 + " seconds");

        start = System.currentTimeMillis();
        Map<Long, Long> userAmountSegmentLock = this.calcAmountSegment(orders);
        System.out.println("segment: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
//        Map<Long, Long> userAmountNoLock = this.calcAmountNoLock(orders);

        for (Long uid : userAmountSerialize.keySet()) {
            System.out.println(uid + ": " + userAmountSerialize.get(uid));
            Assert.assertEquals(userAmountSerialize.get(uid), userAmountLock.get(uid));
            Assert.assertEquals(userAmountSerialize.get(uid), userAmountSync.get(uid));
            Assert.assertEquals(userAmountSerialize.get(uid), userAmountSegmentLock.get(uid));
//            Assert.assertEquals(userAmountSerialize.get(uid), userAmountNoLock.get(uid));
        }
    }

    public Map<Long, Long> calcAmountNoLock(List<Order> orders) throws InterruptedException {
        Map<Long, Long> map = Maps.newConcurrentMap();
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        CountDownLatch countDownLatch = new CountDownLatch(orders.size());
        for (Order order : orders) {
            executorService.execute(() -> {
                try {
                    addAmount(map, order);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        return map;
    }

    private Map<Long, Long> calcAmountSync(List<Order> orders) throws InterruptedException {
        Map<Long, Long> map = Maps.newConcurrentMap();
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        Object lock = new Object();

        CountDownLatch countDownLatch = new CountDownLatch(orders.size());
        for (Order order : orders) {
            executorService.execute(() -> {
                try {
                    synchronized (lock) {
                        addAmount(map, order);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        return map;
    }

    private Map<Long, Long> calcAmountLock(List<Order> orders) throws InterruptedException {
        Map<Long, Long> map = Maps.newConcurrentMap();
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        ReentrantLock reentrantLock = new ReentrantLock();

        CountDownLatch countDownLatch = new CountDownLatch(orders.size());
        for (Order order : orders) {
            executorService.execute(() -> {
                try {
                    reentrantLock.lock();
                    addAmount(map, order);
                    reentrantLock.unlock();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        return map;
    }

    private Map<Long, Long> calcAmountSegment(List<Order> orders) throws InterruptedException {
        Map<Long, Long> map = Maps.newConcurrentMap();
        ExecutorService executorService = Executors.newFixedThreadPool(50);

        SegmentLock segmentLock = new SegmentLock();

        CountDownLatch countDownLatch = new CountDownLatch(orders.size());
        for (Order order : orders) {
            executorService.execute(() -> {
                try {
                    segmentLock.lock(order.getUid());
                    addAmount(map, order);
                    segmentLock.unlock(order.getUid());
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        return map;
    }

    private Map<Long, Long> calcAmount(List<Order> orders) {
        Map<Long, Long> map = Maps.newConcurrentMap();
        for (Order order : orders) {
            addAmount(map, order);
        }
        return map;
    }

    private void addAmount(Map<Long, Long> map, Order order) {
        if (map.containsKey(order.getUid())) {
            Long amount = map.get(order.getUid());
            ThreadUtils.sleep(5);
            Assert.assertEquals(amount, map.get(order.getUid()));
            map.put(order.getUid(), amount + order.getAmount());
        } else {
            map.put(order.getUid(), order.getAmount());
        }
    }

    private List<Order> initOrders() {
        List<Order> orders = Lists.newArrayList();
        for (int i = 0; i < 100; i++) {
            orders.add(Order.builder()
                    .orderId(System.currentTimeMillis())
                    .uid(RandomUtils.nextLong(100, 120))
                    .amount(RandomUtils.nextLong(10, 1000))
                    .title("title-" + UUIDUtils.randomUUID32())
                    .build());
        }
        return orders;
    }

    @Data
    @Builder
    static class Order {
        private Long orderId;
        private Long uid;
        private String title;
        private Long amount;
    }
}
