package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FieldTest {
	@Test
	void test() {
		String src = "a.b.c.d";
		Field field = Field.valueOf(src);
		assertEquals(src, field.toString());
		
		src = "table1";
		field = Field.valueOf(src);
		assertEquals(src, field.toString());
		
		field = new Field("field1", field);
		assertEquals("table1.field1", field.toString());
		
		field = new Field("f1");
		assertEquals("f1", field.toString());
	}
}
