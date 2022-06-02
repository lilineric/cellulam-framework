package com.cellulam.metadata.exceptions;

import com.cellulam.core.exceptions.NestedRuntimeException;

public class MetadataException extends NestedRuntimeException {
    public MetadataException(String msg) {
        super(msg);
    }

    public MetadataException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
