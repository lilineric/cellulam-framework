package com.cellulam.script.test;

import com.cellulam.script.executors.GroovyScriptExecutor;
import com.cellulam.script.executors.ScriptExecutor;
import org.junit.Assert;
import org.junit.Test;

public class GroovyScriptExecutorTest {
    private ScriptExecutor scriptExecutor = new GroovyScriptExecutor();

    @Test
    public void testAddExpression() {
        String functionName = scriptExecutor.addExpression("test", "'t_user_' + (234 % 2 + 1)");
        String result = scriptExecutor.invoke(functionName, String.class);
        System.out.println(result);
        Assert.assertEquals("t_user_1", result);
    }

    @Test
    public void testAddExpression2() {
        String funcName = scriptExecutor.addExpression("test", "452 % 2 + 1");
        int result = scriptExecutor.invoke(funcName, Integer.class);
        System.out.println(result);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testAddFunction() {
        String funcName = scriptExecutor.addExpression("test",
                " 't_test_' + (lastDigital % 2 + 1)",
                "lastDigital");
        String result = scriptExecutor.invoke(funcName, String.class, 452);
        System.out.println(result);
        Assert.assertEquals("t_test_1", result);
    }
}
