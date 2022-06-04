package com.cellulam.script.exceptions;

import com.cellulam.core.exceptions.NestedRuntimeException;

public class ScriptException extends NestedRuntimeException {
    public ScriptException(Throwable cause) {
        super(cause);
    }

    public ScriptException(String msg) {
        super(msg);
    }

    public ScriptException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
