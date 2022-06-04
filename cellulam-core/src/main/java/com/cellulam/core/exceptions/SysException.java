package com.cellulam.core.exceptions;

public class SysException extends NestedRuntimeException{
    public SysException(String msg) {
        super(msg);
    }

    public SysException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SysException(Throwable cause) {
        super(cause);
    }
}
