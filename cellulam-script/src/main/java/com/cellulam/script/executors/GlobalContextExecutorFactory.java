package com.cellulam.script.executors;

public final class GlobalContextExecutorFactory {
    private GlobalContextExecutorFactory() {
    }

    private static ScriptExecutor groovyScriptExecutor = new GroovyScriptExecutor();

    public static ScriptExecutor getGroovyInstance() {
        return groovyScriptExecutor;
    }
}
