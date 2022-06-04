package com.cellulam.script.executors;

public interface ScriptExecutor {

    String addExpression(String funcName, String expression, String... argNames);

    <T> T invoke(String funcName, Class<T> tClass, Object... args);

    String getEngineName();
}
