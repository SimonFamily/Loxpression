package com.loxpression.functions.num;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.loxpression.LoxRunner;

public class AbsTest {

	@Test
	void test() {
		LoxRunner runner = new LoxRunner();
		assertEquals(1, runner.execute("abs(-1)"));
		assertEquals(1, runner.execute("abs(1)"));
		assertEquals(1 + 2 * 3 + Math.abs(1 - 2 * 3), runner.execute("1 + 2 * 3 + abs(1 - 2 * 3)"));
		assertEquals(1 + 2 * 3 + Math.abs(1 + 2 * 3), runner.execute("1 + 2 * 3 + abs(1 + 2 * 3)"));
	}
}
