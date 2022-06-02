package com.cellulam.uid.exceptions;

import com.cellulam.core.exceptions.NestedRuntimeException;

public class UidGenerateException extends NestedRuntimeException {
    public UidGenerateException(String msg) {
        super(msg);
    }

    public UidGenerateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
