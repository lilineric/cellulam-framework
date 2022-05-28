package com.cellulam.uid;

public interface UidGenerator {
    /**
     * 生成分布式ID，需要保证线程安全
     * @return
     */
    long nextId();
}
