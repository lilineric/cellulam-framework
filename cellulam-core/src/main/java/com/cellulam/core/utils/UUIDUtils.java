package com.cellulam.core.utils;

import java.util.UUID;

public final class UUIDUtils {
    public static final String randomUUID32() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}
