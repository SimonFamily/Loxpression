package com.loxpression.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;
import com.loxpression.values.Value;
import com.loxpression.visitors.OpCodeCompiler;

public class VMTest {
	@Test
	void simpleTest() {
		Environment env = new DefaultEnvironment();
		assertEquals(0, execute("1 + 2 - 3", env));
		assertEquals(7, execute("1 + 2 * 3", env));
		assertEquals(9, execute("3 * (2 + 1)", env));
		assertEquals(19.0, execute("1 + 2 * 3 ** 2 ** 1", env));
		assertEquals(9.0, execute("3 * (2 + 1.0)", env));
		assertEquals(true, execute("3 * (2 + 1.0) > 7", env));
		
		env.put("a", 1);
		env.put("b", 2);
		env.put("c", 3);
		assertEquals(3.0, execute("a + b * c - 100 / 5 ** 2 ** 1", env));
		assertEquals(true, execute("a + b * c >= 6", env));
		assertEquals(7, execute("x = y = a + b * c", env));
		assertEquals(7, env.get("x").getValue());
		assertEquals(7, env.get("y").getValue());
	}
	
	private Object execute(String src, Environment env) {
		Parser p = new Parser(src);
		Expr expr = p.expression();
		
		OpCodeCompiler compiler = new OpCodeCompiler();
		Chunk chunk = compiler.compile(expr);
		
		Value v = VM.instance().execute(chunk, env);
		return v.getValue();
	}
}
