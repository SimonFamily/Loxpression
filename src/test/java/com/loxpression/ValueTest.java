package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import com.loxpression.values.Value;

public class ValueTest {

	@Test
	void test() {
		assertEquals(new Value(), new Value());
		assertEquals(new Value(123), new Value(123));
		assertEquals(new Value(123.4), new Value(123.4));
		assertEquals(new Value("aaa"), new Value("aaa"));
		assertEquals(new Value(true), new Value(true));
		assertEquals(new Value(false), new Value(false));
		assertNotEquals(new Value(true), new Value(123));
		assertNotEquals(new Value(false), new Value(true));
	}
}
