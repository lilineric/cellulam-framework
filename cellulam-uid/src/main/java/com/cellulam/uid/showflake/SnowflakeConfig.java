package com.cellulam.uid.showflake;

import com.cellulam.core.utils.AssertUtils;

public class SnowflakeConfig {
    public static final long DEFAULT_START_TIMESTAMP = 1653749770656L;
    public static final long DEFAULT_SEQUENCE_BIT = 12;   //序列号占用的位数
    public static final long DEFAULT_WORK_ID_BIT = 10;     //工作机器ID占用位数
    public static final long DEFAULT_CLOCK_MOVED_BACKWARDS_TIMEOUT = 20;     //默认时间回拨超时时间(毫秒)

    private SnowflakeConfig() {
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getMaxSequence() {
        return maxSequence;
    }

    public long getWorkIdLeft() {
        return workIdLeft;
    }

    public long getTimestampLeft() {
        return timestampLeft;
    }

    public long getClockBackwardsTimeout() {
        return clockBackwardsTimeout;
    }

    public long getWorkId() {
        return workId;
    }

    /**
     * 起始的时间戳
     */
    private long startTimestamp;


    /**
     * 每一部分的最大值
     */
    private long maxSequence;

    /**
     * 每一部分向左的位移
     */
    private long workIdLeft;
    private long timestampLeft;

    // 工作机器ID
    private long workId;

    //时钟回拨超时时间（毫秒）
    private long clockBackwardsTimeout;

    public static final class Builder {
        private long workId;
        private long startTimestamp = DEFAULT_START_TIMESTAMP;
        private long sequenceBit = DEFAULT_SEQUENCE_BIT;   //序列号占用的位数
        private long workIdBit = DEFAULT_WORK_ID_BIT;
        private long clockBackwardsTimeout = DEFAULT_CLOCK_MOVED_BACKWARDS_TIMEOUT;

        private Builder(long workId) {
            this.workId = workId;
        }

        public static Builder builder(long workId) {
            return new Builder(workId);
        }

        public Builder startTimestamp(long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public Builder sequenceBit(long sequenceBit) {
            this.sequenceBit = sequenceBit;
            return this;
        }

        public Builder workIdBit(long workIdBit) {
            this.workIdBit = workIdBit;
            return this;
        }

        public Builder clockBackwardsTimeout(long clockBackwardsTimeout) {
            this.clockBackwardsTimeout = clockBackwardsTimeout;
            return this;
        }

        public SnowflakeConfig build() {
            SnowflakeConfig snowflakeConfig = new SnowflakeConfig();

            snowflakeConfig.maxSequence = -1L ^ (-1L << this.sequenceBit);
            long maxWorkIdNum = -1L ^ (-1L << this.workIdBit);

            AssertUtils.isTrue(workId <= maxWorkIdNum && workId >= 0,
                    "workId must less than or equals to "
                            + maxWorkIdNum);

            snowflakeConfig.startTimestamp = this.startTimestamp;
            snowflakeConfig.workId = workId;
            /**
             * 每一部分向左的位移
             */
            snowflakeConfig.workIdLeft = sequenceBit;
            snowflakeConfig.timestampLeft = sequenceBit + workIdBit;

            snowflakeConfig.clockBackwardsTimeout = clockBackwardsTimeout;

            return snowflakeConfig;
        }
    }
}
