package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.loxpression.execution.ExprInfo;
import com.loxpression.execution.ExprSorter;
import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;
import com.loxpression.values.Value;

public class LoxRunnerTest {
	@Test
    void testSimple() {
		String str = "x = y = b + c * d - 100 / 5 ** 2 ** 1";
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
	void testBatch() {
		int cnt = 10000;
		List<String> lines = new ArrayList<String>();
		String fml = "A! = B! + C! * (D! - E! + 10 ** 2 / 5 - (12 + 8)) - F! * G!";
		String fml1 = "B! = C! + D! * 2 - 1";
		String fml2 = "C! = D! * 2 + 1";
		String fml3 = "D! = E! + F! * G!";
		String fml4 = "G! = M! + N!";
		
		for (int i = 1; i <= cnt; i++) {
			lines.add(fml.replaceAll("!", String.valueOf(i)));
			lines.add(fml1.replaceAll("!", String.valueOf(i)));
			lines.add(fml2.replaceAll("!", String.valueOf(i)));
			lines.add(fml3.replaceAll("!", String.valueOf(i)));
			lines.add(fml4.replaceAll("!", String.valueOf(i)));
		}
		
		Environment env = new Environment();
		for (int i = 1; i <= lines.size(); i++) {
			env.put("E" + i, 2);
			env.put("F" + i, 3);
			env.put("M" + i, 4);
			env.put("N" + i, 5);
		}
		
		LoxRunner runner = new LoxRunner();
		runner.setTrace(true);
		runner.execute(lines, env);
		System.out.println("变量总数: " + env.size());
		
		assertEquals(1682.0, env.get("A1").getValue());
		assertEquals(116, env.get("B1").getValue());
		assertEquals(59, env.get("C1").getValue());
		assertEquals(29, env.get("D1").getValue());
		assertEquals(9, env.get("G1").getValue());
		assertEquals(1682.0, env.get("A2").getValue());
		assertEquals(116, env.get("B2").getValue());
		assertEquals(59, env.get("C2").getValue());
		assertEquals(29, env.get("D2").getValue());
		assertEquals(9, env.get("G2").getValue());
		assertEquals(1682.0, env.get("A3").getValue());
		assertEquals(116, env.get("B3").getValue());
		assertEquals(59, env.get("C3").getValue());
		assertEquals(29, env.get("D3").getValue());
		assertEquals(9, env.get("G3").getValue());
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
