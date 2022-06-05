package com.cellulam.uid.snowflake;

import com.cellulam.uid.UidGenerator;
import com.cellulam.uid.exceptions.UidGenerateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * * <pre>{@code
 *  by default
 *  +------+----------------------+----------------+-----------+
 *  | sign |     timestamp    | worker id | sequence  |
 *  +------+----------------------+----------------+-----------+
 *    1bit          41bits       10bits       12bits
 *  }</pre>
 * @author eric.li
 */
public class SnowflakeGenerator implements UidGenerator {

    private Logger logger = LoggerFactory.getLogger(SnowflakeGenerator.class);

    private SnowflakeConfig config;
    private long sequence = 0L;
    private long lastTimestamp = -1L;

    public SnowflakeGenerator(SnowflakeConfig config) {
        this.config = config;
    }

    public synchronized long nextId() {
        long currTimeStamp = getCurrentTimestamp();
        if (currTimeStamp < lastTimestamp) {
            // clock backwards
            handleClockBack();
        }

        if (currTimeStamp == lastTimestamp) {
            // sequence increased in same timestamp
            sequence = (sequence + 1) & config.getMaxSequence();
            // sequence reach out the max value
            if (sequence == 0L) {
                currTimeStamp = tilNextTick();
            }
        } else {
            // init sequence to 0
            sequence = 0L;
        }

        lastTimestamp = currTimeStamp;

        long timestamp = currTimeStamp - config.getEpoch();

        if (timestamp > config.getMaxTimestamp()) {
            throw new UidGenerateException("Failed to generate UID, timestamp bits is exhausted: " + timestamp
                    + ". maxTimestampBit: " + config.getTimestampBit());
        }

        long uid = timestamp << config.getTimestampLeft() //timestamp
                | config.getWorkId() << config.getWorkIdLeft()       //worker id
                | sequence;                             //sequence
        logger.debug("Generate UID: {}", uid);
        return uid;
    }

    /**
     * handle clock backwards
     */
    protected void handleClockBack() {
        long timestamp = getCurrentTimestamp();
        while (timestamp < lastTimestamp) {
            // Put in the loop to prevent the clock backwards again after entering the loop
            if (lastTimestamp - timestamp > config.getClockBackwardsTimeout()) {
                throw new UidGenerateException("Clock moved backwards.  Refusing to generate id");
            }
            timestamp = getCurrentTimestamp();
        }
    }

    /**
     * waiting until the next time period
     *
     * @return
     */
    protected long tilNextTick() {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }

    protected long getCurrentTimestamp() {
        switch (config.getTimestampUnit()) {
            case SECONDS:
                return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            case MILLISECONDS:
                return System.currentTimeMillis();
            default:
                throw new UidGenerateException("Timestamp Unit not supported: " + config.getTimestampUnit());
        }
    }
}
