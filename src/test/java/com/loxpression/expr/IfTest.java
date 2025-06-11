package com.loxpression.expr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.loxpression.LoxRunner;
import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.parser.LoxParseError;

public class IfTest {
	@Test
	void testSimple() {
		Environment env = new DefaultEnvironment();  
		env.put("a", 1);
		env.put("b", 2);
		env.put("c", 3);
		LoxRunner runner = new LoxRunner();
		assertEquals(36.0, runner.execute("if(a + b * c >= 6, 6 ** 2, -6 * 2)", env));
		assertEquals(-12, runner.execute("if(a + b * c < 6, 6 ** 2, -6 * 2)", env));
		assertEquals(null, runner.execute("if(a + b * c < 6, 6 ** 2)", env));
		assertEquals(36.0, runner.execute("if(a + b * c >= 6, 6 ** 2)", env));
	}
	
	@Test
	void testTest() {
		Environment env = new DefaultEnvironment();
		LoxRunner runner = new LoxRunner();
		String str1 = "if(score >= 85, \"A\", if(score >= 70, \"B\", if(score >= 60, \"C\", \"D\")))";
		String str2 = "if(score >= 70, if(score < 85, \"B\",\"A\"), if(score >= 60, \"C\", \"D\"))";
		env.put("score", 90);
		assertEquals("A", runner.execute(str1, env));
		assertEquals("A", runner.execute(str2, env));
		env.put("score", 65);
		assertEquals("C", runner.execute(str1, env));
		assertEquals("C", runner.execute(str2, env));
		env.put("score", 50);
		assertEquals("D", runner.execute(str1, env));
		assertEquals("D", runner.execute(str2, env));
	}
	
	@Test
	void testError() {
		Environment env = new DefaultEnvironment();
		env.put("a", 1);
		env.put("b", 2);
		env.put("c", 3);
		LoxRunner runner = new LoxRunner();
		assertThrows(LoxParseError.class, () -> runner.execute("if()", env));
		assertThrows(LoxParseError.class, () -> runner.execute("if(a + b * c >= 6)", env));
		assertThrows(LoxParseError.class, () -> runner.execute("if(a + b * c >= 6,)", env));
	}
}
