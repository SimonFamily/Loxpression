package com.loxpression.expr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.loxpression.LoxRunner;
import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;

public class LogicTest {
	@Test
	void test() {
		Environment env = new DefaultEnvironment();
		env.put("a", 1);
		env.put("b", 2);
		env.put("c", 3);
		LoxRunner runner = new LoxRunner();
		assertEquals(true, runner.execute("a == 1 || b == 0 || c == 0", env));
		assertEquals(true, runner.execute("a == 0 || b == 2 || c == 0", env));
		assertEquals(true, runner.execute("a == 0 || b == 0 || c == 3", env));
		assertEquals(false, runner.execute("a == 0 || b == 0 || c == 0", env));
		assertEquals(true, runner.execute("a == 1 && b == 2 && c == 3", env));
		assertEquals(false, runner.execute("a == 0 && b == 2 && c == 3", env));
		assertEquals(false, runner.execute("a == 1 && b == 0 && c == 3", env));
		assertEquals(false, runner.execute("a == 1 && b == 2 && c == 0", env));
	}
}
