package com.loxpression;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.execution.chunk.Chunk;

class BatchRunnerTest {
	private static final int FORMULA_BATCHES = 10000;
	@Test
	void testIR() {
		System.out.println("批量运算测试(解析执行)");
		List<String> lines = getExpressions();
		LoxRunner runner = new LoxRunner();
		runner.setTrace(true);
		Environment env = getEnv();
		runner.execute(lines, env);
		checkValues(env);
		System.out.println("==========");
	}
	
	@Test
	void testChunk() {
		System.out.println("批量运算测试(字节码执行)");
		List<String> lines = getExpressions();
		LoxRunner runner = new LoxRunner();
		runner.setTrace(true);
		Chunk chunk = runner.compileSrc(lines);
		Environment env = getEnv();
		runner.runChunk(chunk, env);
		checkValues(env);
		System.out.println("==========");
	}

	private List<String> getExpressions() {
		List<String> lines = new ArrayList<String>();
		String fml = "A! = 1 + 2 * 3 - 6 - 1 + B! + C! * (D! - E! + 10 ** 2 / 5 - (12 + 8)) - F! * G! +  100 / 5 ** 2 ** 1";
		String fml1 = "B! = C! + D! * 2 - 1";
		String fml2 = "C! = D! * 2 + 1";
		String fml3 = "D! = E! + F! * G!";
		String fml4 = "G! = M! + N!";
		
		for (int i = 1; i <= FORMULA_BATCHES; i++) {
			lines.add(fml.replaceAll("!", String.valueOf(i)));
			lines.add(fml1.replaceAll("!", String.valueOf(i)));
			lines.add(fml2.replaceAll("!", String.valueOf(i)));
			lines.add(fml3.replaceAll("!", String.valueOf(i)));
			lines.add(fml4.replaceAll("!", String.valueOf(i)));
		}
		return lines;
	}

	private Environment getEnv() {
		Environment env = new DefaultEnvironment();
		for (int i = 1; i <= FORMULA_BATCHES; i++) {
			env.put("E" + i, 2);
			env.put("F" + i, 3);
			env.put("M" + i, 4);
			env.put("N" + i, 5);
		}
		return env;
	}
	
	private void checkValues(Environment env) {
		assertEquals(1686.0, env.get("A1").getValue());
		assertEquals(116, env.get("B1").getValue());
		assertEquals(59, env.get("C1").getValue());
		assertEquals(29, env.get("D1").getValue());
		assertEquals(9, env.get("G1").getValue());
		assertEquals(1686.0, env.get("A5000").getValue());
		assertEquals(116, env.get("B5000").getValue());
		assertEquals(59, env.get("C5000").getValue());
		assertEquals(29, env.get("D5000").getValue());
		assertEquals(9, env.get("G5000").getValue());
		assertEquals(1686.0, env.get("A10000").getValue());
		assertEquals(116, env.get("B10000").getValue());
		assertEquals(59, env.get("C10000").getValue());
		assertEquals(29, env.get("D10000").getValue());
		assertEquals(9, env.get("G10000").getValue());
	}
}
