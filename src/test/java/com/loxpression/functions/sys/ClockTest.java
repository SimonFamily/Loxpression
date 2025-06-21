package com.loxpression.functions.sys;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.loxpression.LoxRunner;

class ClockTest {

	@Test
	void test() {
		LoxRunner runner = new LoxRunner();
		assertEquals(0, runner.execute("1 + 2 - 3"));
		System.out.println("Clock测试：");
		System.out.println(runner.execute("clock()"));
		System.out.println(runner.execute("1 + 2 * 3 - 5 + \" abc \" + clock() + \" \" + 123"));
		System.out.println("==========");
	}

}
