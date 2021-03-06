package com.cellulam.core.exceptions;

public abstract class NestedRuntimeException extends RuntimeException {
    public NestedRuntimeException(Throwable cause) {
        super(cause);
    }

    public NestedRuntimeException(String msg) {
        super(msg);
    }

    public NestedRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
