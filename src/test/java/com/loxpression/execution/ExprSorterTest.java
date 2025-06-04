package com.loxpression.execution;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

import org.junit.jupiter.api.Test;

import com.loxpression.Environment;
import com.loxpression.LoxContext;
import com.loxpression.LoxRunner;
import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;

class ExprSorterTest {
	
	@Test
	void test1() {
		List<String> srcs = new ArrayList<>();
		srcs.add("y = x = a + b * c");
		srcs.add("a = m + n");
		srcs.add("b = a * 2");
		srcs.add("c = n + w + b");
		List<Expr> exprs = new ArrayList<Expr>();
		for (String expression : srcs) {
			Parser p = new Parser(expression);
			Expr expr = p.expression();
			exprs.add(expr);
		}
		
		LoxContext context = new LoxContext();
		context.preExecute(exprs, srcs);
		ExprSorter sorter = new ExprSorter(context);
		List<ExprInfo> exprInfos = sorter.sortX();
		List<String> result = new ArrayList<String>(exprInfos.size());
		for (ExprInfo info : exprInfos) {
			result.add(info.src);
			System.out.println(info.src);
		}
		
		LoxRunner runner = new LoxRunner();
		runner.setTrace(true);
		Environment env = new Environment();
		env.put("m", 2);
		env.put("n", 4);
		env.put("w", 6);
		runner.execute(srcs, env);
		assertEquals(270, env.get("x").getValue());
		assertEquals(270, env.get("y").getValue());
		assertEquals(6, env.get("a").getValue());
		assertEquals(12, env.get("b").getValue());
		assertEquals(22, env.get("c").getValue());
		
	}

	@Test
	void test2() {
		List<String> srcs = new ArrayList<>();
		srcs.add("x = y = a + b * c");
		srcs.add("a = m + n");
		srcs.add("b = a * 2");
		srcs.add("c = n + w + b");
		List<Expr> exprs = new ArrayList<Expr>();
		for (String expression : srcs) {
			Parser p = new Parser(expression);
			Expr expr = p.expression();
			exprs.add(expr);
		}
		
		LoxContext context = new LoxContext();
		context.preExecute(exprs, srcs);
		ExprSorter sorter = new ExprSorter(context);
		List<ExprInfo> exprInfos = sorter.sortX();
		List<String> result = new ArrayList<String>(exprInfos.size());
		for (ExprInfo info : exprInfos) {
			result.add(info.src);
			System.out.println(info.src);
		}
		
		LoxRunner runner = new LoxRunner();
		runner.setTrace(true);
		Environment env = new Environment();
		env.put("m", 2);
		env.put("n", 4);
		env.put("w", 6);
		runner.execute(srcs, env);
		assertEquals(270, env.get("x").getValue());
		assertEquals(270, env.get("y").getValue());
		assertEquals(6, env.get("a").getValue());
		assertEquals(12, env.get("b").getValue());
		assertEquals(22, env.get("c").getValue());
		
	}

}
