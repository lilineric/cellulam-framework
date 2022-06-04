package com.cellulam.script.executors;

import com.cellulam.script.exceptions.ScriptException;
import lombok.extern.slf4j.Slf4j;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Cached functions
 */
@Slf4j
public abstract class AbstractScriptExecutor implements ScriptExecutor {

    protected ScriptEngine engine;

    public AbstractScriptExecutor(String engineName) {
        this.engine = new ScriptEngineManager().getEngineByName(engineName);
    }

    protected String addFunction(String finalFuncName, String scripts) {
        try {
            this.engine.eval(scripts);
        } catch (javax.script.ScriptException e) {
            throw new ScriptException(e);
        }
        log.info("Add function [{}], scripts: {}", finalFuncName, scripts);
        return finalFuncName;
    }

    @Override
    public <T> T invoke(String funcName, Class<T> tClass, Object... args) {
        try {
            return (T) ((Invocable) engine).invokeFunction(funcName, args);
        } catch (javax.script.ScriptException e) {
            throw new ScriptException("Failed to invoke function " + funcName, e);
        } catch (NoSuchMethodException e) {
            throw new ScriptException("Cannot find the function: " + funcName, e);
        }
    }

    protected String generateFuncName(String funcName, String expression) {
        return String.format("%s_%s_%s",
                funcName, System.currentTimeMillis(), Math.abs(expression.hashCode()));
    }
}
