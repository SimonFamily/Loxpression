package com.loxpression.expr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.loxpression.Instance;
import com.loxpression.LoxRunner;
import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.values.Value;

public class GetSetTest {
	@Test
	void test() {
		Environment env = new DefaultEnvironment();
		Instance t1 = new Instance();
		t1.set("a", new Value(1));
		Instance t2 = new Instance();
		t2.set("b", new Value(2));
		t2.set("c", new Value(3));
		
		env.put("t1", t1);
		env.put("t2", t2);
		LoxRunner runner = new LoxRunner();
		List<String> lines = new ArrayList<String>();
		lines.add("t1.x = t1.a + t2.b * t2.c + m");
		lines.add("m = t1.a + t2.b * t2.c");
		Object[] r = runner.execute(lines, env);
		assertEquals(7, env.get("m").getValue());
		assertEquals(14, env.get("t1").asInstance().get("x").asInteger());
		assertEquals(14, r[0]);
		assertEquals(7, r[1]);
	}
}
