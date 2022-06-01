package com.cellulam.uid.showflake;

import com.cellulam.core.utils.AssertUtils;

public class SnowflakeConfig {
    public static final long DEFAULT_START_TIMESTAMP = 1653749770656L;
    public static final long DEFAULT_SEQUENCE_BIT = 12;   //序列号占用的位数
    public static final long DEFAULT_DATA_CENTER_BIT = 5;       //数据中心占用位数
    public static final long DEFAULT_MACHINE_BIT = 5;     //机器标识占用的位数
    public static final long DEFAULT_CLOCK_MOVED_BACKWARDS_TIMEOUT = 20;     //默认时间回拨超时时间(毫秒)

    private SnowflakeConfig() {
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public long getMaxSequence() {
        return maxSequence;
    }

    public long getMachineIdLeft() {
        return machineIdLeft;
    }

    public long getDataCenterIdLeft() {
        return dataCenterIdLeft;
    }

    public long getTimestampLeft() {
        return timestampLeft;
    }

    public long getClockBackwardsTimeout() {
        return clockBackwardsTimeout;
    }

    public long getMachineId() {
        return machineId;
    }

    /**
     * 起始的时间戳
     */
    private long startTimestamp;
    private long dataCenterId;  //数据中心


    /**
     * 每一部分的最大值
     */
    private long maxSequence;

    /**
     * 每一部分向左的位移
     */
    private long machineIdLeft;
    private long dataCenterIdLeft;
    private long timestampLeft;

    private long machineId;

    //时钟回拨超时时间（毫秒）
    private long clockBackwardsTimeout;

    public static final class Builder {
        private long machineId;  
        private long dataCenterId;  
        private long startTimestamp = DEFAULT_START_TIMESTAMP;
        private long sequenceBit = DEFAULT_SEQUENCE_BIT;   //序列号占用的位数
        private long dataCenterIdBit = DEFAULT_DATA_CENTER_BIT;       //数据中心占用位数
        private long machineIdBit = DEFAULT_MACHINE_BIT;     //机器标识占用的位数
        private long clockBackwardsTimeout = DEFAULT_CLOCK_MOVED_BACKWARDS_TIMEOUT;

        private Builder(long dataCenterId, long machineId) {
            this.dataCenterId = dataCenterId;
            this.machineId = machineId;
        }

        public static Builder builder(long dataCenterId, long machineId) {
            return new Builder(dataCenterId, machineId);
        }

        public Builder startTimestamp(long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public Builder sequenceBit(long sequenceBit) {
            this.sequenceBit = sequenceBit;
            return this;
        }

        public Builder dataCenterIdBit(long dataCenterIdBit) {
            this.dataCenterIdBit = dataCenterIdBit;
            return this;
        }

        public Builder machineIdBit(long machineIdBit) {
            this.machineIdBit = machineIdBit;
            return this;
        }

        public Builder clockBackwardsTimeout(long clockBackwardsTimeout) {
            this.clockBackwardsTimeout = clockBackwardsTimeout;
            return this;
        }

        public SnowflakeConfig build() {
            SnowflakeConfig snowflakeConfig = new SnowflakeConfig();

            snowflakeConfig.maxSequence = -1L ^ (-1L << this.sequenceBit);
            long maxDataCenterIdNum = -1L ^ (-1L << this.dataCenterIdBit);
            long maxMachineIdNum = -1L ^ (-1L << this.machineIdBit);

            AssertUtils.isTrue(dataCenterId <= maxDataCenterIdNum && dataCenterId >= 0,
                    "dataCenterId must less than or equals to "
                            + maxDataCenterIdNum);

            AssertUtils.isTrue(machineId <= maxMachineIdNum && machineId >= 0,
                    "MachineId must less than or queals to "
                            + maxMachineIdNum);

            snowflakeConfig.startTimestamp = this.startTimestamp;
            snowflakeConfig.dataCenterId = this.dataCenterId;
            snowflakeConfig.machineId = machineId;
            /**
             * 每一部分向左的位移
             */
            snowflakeConfig.machineIdLeft = sequenceBit;
            snowflakeConfig.dataCenterIdLeft = sequenceBit + machineIdBit;
            snowflakeConfig.timestampLeft = snowflakeConfig.dataCenterIdLeft + dataCenterIdBit;

            snowflakeConfig.clockBackwardsTimeout = clockBackwardsTimeout;

            return snowflakeConfig;
        }
    }
}
