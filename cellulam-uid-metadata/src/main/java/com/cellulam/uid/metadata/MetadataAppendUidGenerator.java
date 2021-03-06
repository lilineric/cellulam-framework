package com.cellulam.uid.metadata;

import com.cellulam.core.utils.AssertUtils;
import com.cellulam.metadata.MetadataContext;
import com.cellulam.uid.UidGenerator;
import com.cellulam.uid.exceptions.UidGenerateException;
import com.cellulam.uid.snowflake.SnowflakeConfig;
import com.cellulam.uid.snowflake.SnowflakeGenerator;
import com.cellulam.uid.snowflake.TimestampUnit;

/**
 * UID generator base on {@link SnowflakeGenerator}
 * The workerId is taken from {@link MetadataContext}
 * For performance reason, nextId() is generated by {@link MetadataUidGenerator}
 * <p>
 * Total 64bits
 * nextId(long pid) :
 * * <pre>{@code
 *  +------+----------------------+----------------+---------------------+
 *  | sign |timestamp(seconds) | worker id | sequence  | additional pid |
 *  +------+----------------------+----------------+----------------------+
 *    1bit          29bits        12bits      15bits        7bits
 *  }</pre>
 * 1bits sign. Always be 0
 * 29bits timestamp. Time unit is seconds, could support about 17 years.
 * 12bits worker id. Maximum value will be 4096. {@link MetadataContext} will generate workerId for each instance.
 * 11bits sequence. Maximum value will be 32768. Represents sequence within the one second, maximum is 32768 per second.
 * <p>
 * Recommended to use a different MetadataAppendUidGenerator instance for each different service (these services should allow the same UID)
 * For example:
 * <p>
 * <pre>{@code
 *     UidGenerator userIdGenerator = new MetadataUidGenerator();
 *     UidAppendGenerator orderIdGenerator = new MetadataAppendUidGenerator();
 *     UidAppendGenerator payIdGenerator = new MetadataAppendUidGenerator();
 *
 *     long userId = userIdGenerator.nextId();
 *     long orderId = orderIdGenerator.nextId(userId);
 *     long payId = payIdGenerator.nextId(orderId);
 *     }</pre>
 * </p>
 * @author eric.li
 */
public class MetadataAppendUidGenerator extends SnowflakeGenerator implements UidAppendGenerator {
    public static final long TIMESTAMP_BIT = 29;
    public static final long WORKER_ID_BIT = 12;
    public static final long SEQUENCE_BIT = 15;

    /**
     * The pid can be appended to allow only 10 bits, i.e. the maximum value is 1024.
     * So the maximum number of digits can only be taken in hundredths, which means MAX_PID_SUB_DIGITS = 3
     */
    public static final int MAX_PID_SUB_DIGITS = 2;
    public static final int DEFAULT_PID_SUB_DIGITS = 2;
    public static final long DEFAULT_EPOCH = 1654238882L;

    private final int scaleRate;

    private UidGenerator uidGenerator;

    public MetadataAppendUidGenerator() {
        this(DEFAULT_EPOCH);
    }

    /**
     * @param epoch unit seconds
     */
    public MetadataAppendUidGenerator(long epoch) {
        this(epoch, DEFAULT_PID_SUB_DIGITS);
    }

    /**
     * @param epoch        unit seconds
     * @param pidSubDigits max value is 2
     */
    public MetadataAppendUidGenerator(long epoch, int pidSubDigits) {
        super(buildSnowflakeConfig(epoch));
        AssertUtils.isTrue(pidSubDigits <= MAX_PID_SUB_DIGITS,
                String.format("The pidSubDigits must be less than or equals %s, currently is %s",
                        MAX_PID_SUB_DIGITS, pidSubDigits));
        this.scaleRate = (int) Math.pow(10, pidSubDigits);

        this.uidGenerator = new MetadataUidGenerator(epoch * 1000);
    }

    private static SnowflakeConfig buildSnowflakeConfig(long epoch) {
        int workerId = MetadataContext.context.getWorkerId();
        if (workerId <= 0) {
            throw new UidGenerateException("WorkerId wasn't initialized: " + workerId);
        }
        return SnowflakeConfig.Builder
                .builder(workerId)
                .epoch(epoch)
                .timestampBit(TIMESTAMP_BIT)
                .workIdBit(WORKER_ID_BIT)
                .sequenceBit(SEQUENCE_BIT)
                .timestampUnit(TimestampUnit.SECONDS)
                .build();
    }

    /**
     * generated by {@link MetadataUidGenerator}
     *
     * @return
     */
    @Override
    public synchronized long nextId() {
        return this.uidGenerator.nextId();
    }

    /**
     * generated 53bits uid and append last digital of pid
     *
     * @param pid
     * @return
     */
    @Override
    public long nextId(long pid) {
        // cannot use this.nextId().
        // For performance reason this.nextId() will call MetadataUidGenerator.nextId(), it will return 64bits UID.
        long id = super.nextId();
        return id * this.scaleRate + (pid % scaleRate);
    }
}
