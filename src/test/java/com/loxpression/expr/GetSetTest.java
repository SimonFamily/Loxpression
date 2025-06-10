package com.loxpression.expr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.loxpression.Environment;
import com.loxpression.Instance;
import com.loxpression.LoxRunner;
import com.loxpression.values.Value;

public class GetSetTest {
	@Test
	void test() {
		Environment env = new Environment();
		Instance t1 = new Instance();
		t1.set("a", new Value(1));
		Instance t2 = new Instance();
		t2.set("b", new Value(2));
		t2.set("c", new Value(3));
		
		env.put("t1", new Value(t1));
		env.put("t2", new Value(t2));
		LoxRunner runner = new LoxRunner();
		Object r = runner.execute("t1.x = t1.a + t2.b * t2.c", env);
		assertEquals(7, r);
		assertEquals(7, env.get("t1").asInstance().get("x").asInteger());
	}
}
