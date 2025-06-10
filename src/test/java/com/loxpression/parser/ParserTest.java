package com.loxpression.parser;

import org.junit.jupiter.api.Test;

import com.loxpression.expr.Expr;

public class ParserTest {

	@Test
	void test() {
		String src = "form1.table1.field1 = table2.field2 + table3.field3 * form2.table2.field2";
		Parser p = new Parser(src);
		Expr expr = p.expression();
		System.out.println(expr + " success!");
	}
}
