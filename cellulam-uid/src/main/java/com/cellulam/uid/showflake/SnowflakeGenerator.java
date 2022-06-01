package com.cellulam.uid.showflake;

import com.cellulam.uid.UidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参照Snowflake算法实现
 */
public class SnowflakeGenerator implements UidGenerator {

    private Logger logger = LoggerFactory.getLogger(SnowflakeGenerator.class);

    private SnowflakeConfig config;
    private long sequence = 0L; //序列号
    private long lastTimestamp = -1L;  //上一次时间戳

    public SnowflakeGenerator(SnowflakeConfig config) {
        this.config = config;
    }

    public synchronized long nextId() {
        long currTimeStamp = System.currentTimeMillis();
        if (currTimeStamp < lastTimestamp) {
            //时间回拨
            handleClockBack();
        }

        if (currTimeStamp == lastTimestamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & config.getMaxSequence();
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currTimeStamp = tilNextMillis();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTimestamp = currTimeStamp;

        long uid = (currTimeStamp - config.getStartTimestamp()) << config.getTimestampLeft() //时间戳部分
                | config.getDataCenterId() << config.getDataCenterIdLeft()       //数据中心部分
                | config.getMachineId() << config.getMachineIdLeft()             //机器标识部分
                | sequence;                             //序列号部分
        logger.debug("Generate UID: {}", uid);
        return uid;
    }

    /**
     * 处理时钟回拨问题
     */
    private void handleClockBack() {
        long mill = System.currentTimeMillis();
        while (mill < lastTimestamp) {
            // 放在while循环中判断，防止进入循环后时钟再次回拨
            if (lastTimestamp - mill > config.getClockBackwardsTimeout()) {
                throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
            }
            mill = System.currentTimeMillis();
        }
    }

    private long tilNextMillis() {
        long mill = System.currentTimeMillis();
        while (mill <= lastTimestamp) {
            mill = System.currentTimeMillis();
        }
        return mill;
    }
}
