package com.loxpression.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.loxpression.LoxContext;
import com.loxpression.LoxRunner;
import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;

class ExprSorterTest {
	
	@Test
	void test1() {
		List<String> srcs = new ArrayList<>();
		System.out.println("拓扑排序测试：");
		srcs.add("x = y = a + b * c");
		srcs.add("a = m + n");
		srcs.add("b = a * 2");
		srcs.add("c = n + w + b");
		List<Expr> exprs = new ArrayList<Expr>();
		for (String expression : srcs) {
			Parser p = new Parser(expression);
			Expr expr = p.expression();
			exprs.add(expr);
			System.out.println(expression);
		}
		
		LoxContext context = new LoxContext();
		LoxRunner runner = new LoxRunner();
		List<ExprInfo> exprInfos = runner.parseExpressions(srcs);
		context.prepareExecute(exprInfos);
		ExprSorter sorter = new ExprSorter(context);
		exprInfos = sorter.sort();
		System.out.println("排序结果为：");
		for (ExprInfo info : exprInfos) {
			System.out.println(info.getSrc());
		}
		assertTrue(exprInfos != null && exprInfos.size() == 4);
		assertEquals("a = m + n", exprInfos.get(0).getSrc());
		assertEquals("b = a * 2", exprInfos.get(1).getSrc());
		assertEquals("c = n + w + b", exprInfos.get(2).getSrc());
		assertEquals("x = y = a + b * c", exprInfos.get(3).getSrc());
		
		runner = new LoxRunner();
		Environment env = new DefaultEnvironment();
		env.put("m", 2);
		env.put("n", 4);
		env.put("w", 6);
		runner.execute(srcs, env);
		assertEquals(270, env.get("x").getValue());
		assertEquals(270, env.get("y").getValue());
		assertEquals(6, env.get("a").getValue());
		assertEquals(12, env.get("b").getValue());
		assertEquals(22, env.get("c").getValue());
		System.out.println("==========");
	}

	@Test
	void test2() {
		List<String> srcs = new ArrayList<>();
		srcs.add("b * 2 + 1");
		srcs.add("a * b + c");
		srcs.add("x = y = a + b * c");
		srcs.add("a = m + n");
		srcs.add("b = a * 2");
		srcs.add("c = n + w + b");
		List<Expr> exprs = new ArrayList<Expr>();
		System.out.println("拓扑排序测试：");
		for (String expression : srcs) {
			Parser p = new Parser(expression);
			Expr expr = p.expression();
			exprs.add(expr);
			System.out.println(expression);
		}
		
		LoxContext context = new LoxContext();
		LoxRunner runner = new LoxRunner();
		List<ExprInfo> exprInfos = runner.parseExpressions(srcs);
		context.prepareExecute(exprInfos);
		ExprSorter sorter = new ExprSorter(context);
		exprInfos = sorter.sort();
		System.out.println("排序结果为：");
		for (ExprInfo info : exprInfos) {
			System.out.println(info.getSrc());
		}
		assertTrue(exprInfos != null && exprInfos.size() == 6);
		assertEquals("a = m + n", exprInfos.get(0).getSrc());
		assertEquals("b = a * 2", exprInfos.get(1).getSrc());
		assertEquals("c = n + w + b", exprInfos.get(2).getSrc());
		assertEquals("x = y = a + b * c", exprInfos.get(3).getSrc());
		assertEquals("b * 2 + 1", exprInfos.get(4).getSrc());
		assertEquals("a * b + c", exprInfos.get(5).getSrc());
		
		runner = new LoxRunner();
		Environment env = new DefaultEnvironment();
		env.put("m", 2);
		env.put("n", 4);
		env.put("w", 6);
		Object[] result = runner.execute(srcs, env);
		assertEquals(270, env.get("x").getValue());
		assertEquals(270, env.get("y").getValue());
		assertEquals(6, env.get("a").getValue());
		assertEquals(12, env.get("b").getValue());
		assertEquals(22, env.get("c").getValue());
		
		assertEquals(12 * 2 + 1, result[0]);
		assertEquals(6 * 12 + 22, result[1]);
		assertEquals(270, result[2]);
		assertEquals(6, result[3]);
		assertEquals(12, result[4]);
		assertEquals(22, result[5]);
		System.out.println("==========");
	}

}
