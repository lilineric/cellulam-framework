package com.cellulam.core.utils;

import com.cellulam.core.exceptions.SysException;

public abstract class ThreadUtils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new SysException(e);
        }
    }
}
