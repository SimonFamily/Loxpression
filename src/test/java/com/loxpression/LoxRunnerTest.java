package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class LoxRunnerTest {
	@Test
    void testSimple() {
		Environment env = new Environment();  
		LoxRunner runner = new LoxRunner();
		assertEquals(0, runner.execute("1 + 2 - 3", env));
		assertEquals(7, runner.execute("1 + 2 * 3", env));
		assertEquals(9, runner.execute("3 * (2 + 1)", env));
		assertEquals(19.0, runner.execute("1 + 2 * 3 ** 2 ** 1", env));
		assertEquals(9.0, runner.execute("3 * (2 + 1.0)", env));
		assertEquals(true, runner.execute("3 * (2 + 1.0) > 7", env));
		
		env.put("a", 1);
		env.put("b", 2);
		env.put("c", 3);
		assertEquals(7, runner.execute("x = y = a + b * c", env));
		assertEquals(7, env.get("x").getValue());
		assertEquals(7, env.get("y").getValue());
    }
	
	@Test
    void testEvaluate() {
		Environment env = new Environment();  
		env.put("a", 1);
		env.put("b", 2);
		env.put("c", 3);
		LoxRunner runner = new LoxRunner();
		Object r = runner.execute("a + b * c - 100 / 5 ** 2 ** 1", env);
		System.out.println(r);
		
		r = runner.execute("a + b * c >= 6", env);
		System.out.println(r);
    }

	@Test
	void testCalculation() {
		List<String> srcs = new ArrayList<>();
		srcs.add("x = a + b * c");
		srcs.add("a = m + n");
		srcs.add("b = a * 2");
		srcs.add("c = n + w");
		
		LoxRunner runner = new LoxRunner();
		Environment env = new Environment();
		env.put("m", 2);
		env.put("n", 4);
		env.put("w", 6);
		runner.execute(srcs, env);
		
		System.out.println(env.get("x"));
		System.out.println(env.get("a"));
		System.out.println(env.get("b"));
		System.out.println(env.get("c"));
		
	}
}
