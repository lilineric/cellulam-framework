package com.cellulam.core.exceptions;

public class ConfigurationException extends NestedRuntimeException{
    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
