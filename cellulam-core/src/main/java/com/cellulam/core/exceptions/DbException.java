package com.cellulam.core.exceptions;

public class DbException extends NestedRuntimeException{
    public DbException(String msg) {
        super(msg);
    }

    public DbException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
