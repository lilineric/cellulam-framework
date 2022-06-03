package com.cellulam.uid.snowflake;

import com.cellulam.core.utils.AssertUtils;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class SnowflakeConfig {
    public static final long DEFAULT_EPOCH = 1653749770656L;
    public static final long DEFAULT_TIMESTAMP_BIT = 41;
    public static final long DEFAULT_SEQUENCE_BIT = 12;
    public static final long DEFAULT_WORK_ID_BIT = 10;
    public static final long DEFAULT_CLOCK_MOVED_BACKWARDS_TIMEOUT = 20;

    private SnowflakeConfig() {
    }

    /**
     * start timestamp
     */
    private long epoch;


    private long maxTimestamp;
    private long maxSequence;

    private long workIdLeft;
    private long timestampLeft;

    private long workId;

    private long timestampBit;
    private long sequenceBit;
    private long workIdBit;

    private TimestampUnit timestampUnit;

    // unit is same as timestampUnit
    private long clockBackwardsTimeout;

    public static final class Builder {
        private long workId;
        private long epoch = DEFAULT_EPOCH;
        private long timestampBit = DEFAULT_TIMESTAMP_BIT;
        private long sequenceBit = DEFAULT_SEQUENCE_BIT;
        private long workIdBit = DEFAULT_WORK_ID_BIT;
        private long clockBackwardsTimeout = DEFAULT_CLOCK_MOVED_BACKWARDS_TIMEOUT;
        private TimestampUnit timestampUnit;

        private Builder(long workId) {
            this.workId = workId;
        }

        public static Builder builder(long workId) {
            return new Builder(workId);
        }

        public Builder epoch(long epoch) {
            this.epoch = epoch;
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

        public Builder timestampBit(long timestampBit) {
            this.timestampBit = timestampBit;
            return this;
        }

        public Builder clockBackwardsTimeout(long clockBackwardsTimeout) {
            this.clockBackwardsTimeout = clockBackwardsTimeout;
            return this;
        }

        /**
         * timestamp unit
         * only support SECONDS Or MILLISECONDS
         * default MILLISECONDS
         *
         * @param timestampUnit
         * @return
         */
        public Builder timestampUnit(TimestampUnit timestampUnit) {
            this.timestampUnit = timestampUnit;
            return this;
        }

        public SnowflakeConfig build() {
            SnowflakeConfig snowflakeConfig = new SnowflakeConfig();

            snowflakeConfig.maxSequence = -1L ^ (-1L << this.sequenceBit);
            snowflakeConfig.maxTimestamp = -1L ^ (-1L << this.timestampBit);
            long maxWorkIdNum = -1L ^ (-1L << this.workIdBit);

            AssertUtils.isTrue(workId <= maxWorkIdNum && workId >= 0,
                    "workId must less than or equals to "
                            + maxWorkIdNum);

            snowflakeConfig.epoch = this.epoch;
            snowflakeConfig.workId = workId;

            snowflakeConfig.workIdLeft = sequenceBit;
            snowflakeConfig.timestampLeft = sequenceBit + workIdBit;

            snowflakeConfig.clockBackwardsTimeout = clockBackwardsTimeout;

            snowflakeConfig.timestampBit = timestampBit;
            snowflakeConfig.sequenceBit = sequenceBit;
            snowflakeConfig.workIdBit = workIdBit;

            snowflakeConfig.timestampUnit = this.timestampUnit == null
                    ? TimestampUnit.MILLISECONDS
                    : this.timestampUnit;

            this.validateEpoch(snowflakeConfig.epoch, snowflakeConfig.timestampUnit);

            return snowflakeConfig;
        }

        private void validateEpoch(long epoch, TimestampUnit timestampUnit) {
            long currMillis = System.currentTimeMillis();
            try {
                switch (timestampUnit) {
                    case MILLISECONDS:
                        AssertUtils.isTrue(currMillis >= epoch);
                        break;
                    case SECONDS:
                        AssertUtils.isTrue(TimeUnit.MILLISECONDS.toSeconds(currMillis) >= epoch);
                        break;
                    default:
                        break;
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(String.format("Epoch must less than current time, epoch: %s, timestampUnit: %s, currMillis: %s",
                        epoch, timestampUnit, currMillis), e);
            }
        }
    }
}
