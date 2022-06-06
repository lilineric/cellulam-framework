package com.cellulam.core.lock;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * segment lock
 * <p>
 * Objects with the same hash value will get the same lock.
 * The lock might not be released if the object hash changes.
 *
 * @author eric.li
 * @date 2022-06-06 01:33
 */
@Slf4j
public class SegmentLock {

    private static final int DEFAULT_SEGMENTS = 16;

    private final int segments;

    private final Map<Integer, ReentrantLock> lockMap = Maps.newHashMap();

    public SegmentLock() {
        this(false);
    }

    public SegmentLock(boolean fair) {
        this(DEFAULT_SEGMENTS, fair);
    }

    public SegmentLock(int segments, boolean fair) {
        this.segments = segments;
        for (int i = 0; i < this.segments; i++) {
            lockMap.put(i, new ReentrantLock(fair));
        }
    }

    private ReentrantLock getLock(Object object) {
        return lockMap.get((object.hashCode() >>> 1) % this.segments);
    }

    public void lock(Object object) {
        ReentrantLock lock = this.getLock(object);
        lock.lock();
        log.debug("lock: {}, lockId: {}, hashcode: {}", object, lock, object.hashCode());
    }

    public void lockInterruptibly(Object object) throws InterruptedException {
        getLock(object).lockInterruptibly();
    }

    public boolean tryLock(Object object) {
        return getLock(object).tryLock();
    }

    public boolean tryLock(Object object, long time, TimeUnit unit) throws InterruptedException {
        return getLock(object).tryLock(time, unit);
    }

    public void unlock(Object object) {
        ReentrantLock lock = getLock(object);
        log.debug("unlock: {}, lockId: {}, hashcode: {}", object, lock, object.hashCode());
        lock.unlock();
    }

}
