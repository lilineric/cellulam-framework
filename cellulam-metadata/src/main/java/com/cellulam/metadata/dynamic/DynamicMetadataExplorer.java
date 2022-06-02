package com.cellulam.metadata.dynamic;

import com.cellulam.core.utils.LocalDateUtils;

import java.time.LocalDateTime;

public interface DynamicMetadataExplorer {
    LocalDateTime getSysTime();

    default long getSysTimestamp() {
        return LocalDateUtils.toTimestamp(getSysTime());
    }
}
