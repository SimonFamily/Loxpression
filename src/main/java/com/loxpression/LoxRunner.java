package com.loxpression;

import java.util.ArrayList;
import java.util.List;

import com.loxpression.execution.ExprSorter;
import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;
import com.loxpression.values.Value;
import com.loxpression.visitors.Evaluator;

public class LoxRunner {
	private boolean needSort;
	private LoxContext context;
	
	public LoxRunner() {
		this.needSort = true;
		this.context = new LoxContext();
	}
	
	public boolean isNeedSort() {
		return needSort;
	}

	public void setNeedSort(boolean needSort) {
		this.needSort = needSort;
	}

	public boolean isTrace() {
		return context.getTracer().isEnable();
	}

	public void setTrace(boolean isTrace) {
		context.getTracer().setEnable(isTrace);
	}

	public Object execute(String expression) {
		return execute(expression, new Environment());
	} 
	
	public Object execute(String expression, Environment env) {
		Evaluator evtor = new Evaluator(env);
		Value v =  evtor.execute(expression);
		return v.getValue();
	}
	
	public void execute(List<String> exprs) {
		execute(exprs, new Environment());
	}
	
	public void execute(List<String> expressions, Environment env) {
		context.getTracer().startTimer("开始。公式总数：%s", expressions.size());
		List<Expr> exprs = parseExpressions(expressions);
		context.prepareExecute(exprs, expressions);
		if (needSort) {
			ExprSorter sorter = new ExprSorter(context);
			exprs = sorter.sort();
		}
		context.getTracer().startTimer();
		for (Expr expression : exprs) {
			Evaluator evtor = new Evaluator(env);
			Value v =  evtor.execute(expression);
		}
		context.getTracer().endTimer("完成求值。");
		context.getTracer().endTimer("结束。");
	}
	
	public List<Expr> parseExpressions(List<String> expressions) {
		context.getTracer().startTimer();
		List<Expr> exprs = new ArrayList<Expr>();
		for (String expression : expressions) {
			Parser p = new Parser(expression);
			Expr expr = p.expression();
			exprs.add(expr);
		}
		context.getTracer().endTimer("结束解析表达式。");
		return exprs;
	}
}
