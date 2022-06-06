package com.cellulam.core.exceptions;

/**
 * @author eric.li
 * @date 2022-06-06 16:52
 */
public class RuntimeIOException extends NestedRuntimeException {
    public RuntimeIOException(Throwable cause) {
        super(cause);
    }

    public RuntimeIOException(String msg) {
        super(msg);
    }

    public RuntimeIOException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
