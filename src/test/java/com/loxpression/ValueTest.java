package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;

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

	@Test
	void seriDeseriTest() {
		// values 序列化为字节数组
		Value[] values = new Value[] { 
				new Value("abc123"),
				new Value(123),
				new Value(123.321),
				new Value("abcd"),
				new Value("你好你好！！"),
				new Value(123),
				new Value(12.3),
				new Value("再见bye-bye"),
		};
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		for (Value value : values) {
			value.writeTo(buffer);
		}
		buffer.flip();
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes); // 得到字节数组

		// 字节数组反序列化为values
		buffer = ByteBuffer.wrap(bytes);
		int i = 0;
		while (buffer.remaining() > 0) {
			Value newValue = Value.getFrom(buffer);
			assertTrue(newValue.equals(values[i]));
			i++;
		}
		assertEquals(i, values.length);
	}
}
