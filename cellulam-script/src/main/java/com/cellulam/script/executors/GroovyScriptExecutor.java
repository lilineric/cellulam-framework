package com.cellulam.script.executors;

import lombok.extern.slf4j.Slf4j;

/**
 * Executor for groovy scripts
 * Cached functions
 */
@Slf4j
public final class GroovyScriptExecutor extends AbstractScriptExecutor implements ScriptExecutor {

    private final static String ENGINE_NAME = "groovy";

    public GroovyScriptExecutor() {
        super(ENGINE_NAME);
    }

    @Override
    public String addExpression(String funcName, String expression, String... argNames) {
        String finalFuncName = this.generateFuncName(funcName, expression);
        return addFunction(finalFuncName, String.format("def %s(%s) { return %s }",
                finalFuncName,
                argNames.length > 0 ? String.join(",", argNames) : "",
                expression));
    }

    public String getEngineName() {
        return ENGINE_NAME;
    }
}
