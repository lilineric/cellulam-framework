package com.cellulam.uid.metadata;

import com.cellulam.metadata.MetadataContext;
import com.cellulam.uid.UidGenerator;
import com.cellulam.uid.exceptions.UidGenerateException;
import com.cellulam.uid.snowflake.SnowflakeConfig;
import com.cellulam.uid.snowflake.SnowflakeGenerator;

/**
 * UID generator base on {@link com.cellulam.uid.snowflake.SnowflakeGenerator}
 * The workerId is taken from {@link com.cellulam.metadata.MetadataContext}
 * <p>
 * Total 64bits
 * * <pre>{@code
 *  +------+----------------------+----------------+-----------+
 *   | sign |timestamp | worker id | sequence  |
 *   +------+----------------------+----------------+-----------+
 *     1bit     39bits      12bits     12bits
 *  }</pre>
 * 1bits sign. Always be 0
 * 39bits timestamp. Time unit is milliseconds, could support about 17 years.
 * 12bits worker id. Maximum value will be 4096. {@link com.cellulam.metadata.MetadataContext} will generate workerId for each instance.
 * 12bits sequence. Maximum value will be 4096. Represents sequence within the one mills, maximum is 4096 per mills.
 */
public class MetadataUidGenerator extends SnowflakeGenerator implements UidGenerator {
    public static final long TIMESTAMP_BIT = 39;
    public static final long WORKER_ID_BIT = 13;
    public static final long SEQUENCE_BIT = 11;

    public static final long DEFAULT_EPOCH = 1653749770656L;

    public MetadataUidGenerator() {
       this(DEFAULT_EPOCH);
    }
    public MetadataUidGenerator(long epoch) {
        super(buildSnowflakeConfig(epoch));
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
                .build();
    }
}
