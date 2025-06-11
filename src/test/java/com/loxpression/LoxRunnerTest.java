package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;

public class LoxRunnerTest {
	@Test
    void testSimple() {
		Environment env = new DefaultEnvironment();  
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
		Environment env = new DefaultEnvironment();  
		env.put("a", 1);
		env.put("b", 2);
		env.put("c", 3);
		LoxRunner runner = new LoxRunner();
		Object r = runner.execute("a + b * c - 100 / 5 ** 2 ** 1", env);
		assertEquals(3.0, r);
		
		r = runner.execute("a + b * c >= 6", env);
		assertEquals(true, r);
    }
	
	@Test
    void testMuiltiEvaluate() {
		Environment env = new DefaultEnvironment();  
		env.put("a", 1);
		env.put("b", 2);
		env.put("c", 3);
		List<String> lines = new ArrayList<String>();
		lines.add("a + b * c - 100 / 5 ** 2 ** 1");
		lines.add("a + b * c >= 6");
		lines.add("1 + 2 - 3");
		lines.add("3 * (2 + 1)");
		lines.add("a + (b - c)");
		lines.add("a * 2 + (b - c)");
		lines.add("x = y = a + b * c");
		
		LoxRunner runner = new LoxRunner();
		Object[] r = runner.execute(lines, env);
		assertEquals(3.0, r[0]);
		assertEquals(true, r[1]);
		assertEquals(0, r[2]);
		assertEquals(9, r[3]);
		assertEquals(0, r[4]);
		assertEquals(1, r[5]);
		assertEquals(7, r[6]);
		assertEquals(7, env.get("x").getValue());
		assertEquals(7, env.get("y").getValue());
    }

	@Test
	void testCalculation() {
		List<String> srcs = new ArrayList<>();
		srcs.add("x = a + b * c");
		srcs.add("a = m + n");
		srcs.add("b = a * 2");
		srcs.add("c = n + w");
		
		LoxRunner runner = new LoxRunner();
		Environment env = new DefaultEnvironment();
		env.put("m", 2);
		env.put("n", 4);
		env.put("w", 6);
		Object[] results = runner.execute(srcs, env);
		assertEquals(126, env.get("x").getValue());
		assertEquals(6, env.get("a").getValue());
		assertEquals(12, env.get("b").getValue());
		assertEquals(10, env.get("c").getValue());
		
		assertEquals(126, results[0]);
		assertEquals(6, results[1]);
		assertEquals(12, results[2]);
		assertEquals(10, results[3]);
	}
}
