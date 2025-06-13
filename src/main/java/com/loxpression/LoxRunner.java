package com.loxpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.execution.ExprInfo;
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
		Object[] result = execute(Arrays.asList(expression), new DefaultEnvironment());
		return result[0];
	}
	
	public Object execute(String expression, Environment env) {
		Object[] result = execute(Arrays.asList(expression), env);
		return result[0];
	}
	
	public Object[] execute(List<String> exprs) {
		return execute(exprs, new DefaultEnvironment());
	}
	
	public Object[] execute(List<String> expressions, Environment env) {
		context.getTracer().startTimer("开始。公式总数：%s", expressions.size());
		List<ExprInfo> exprInfos = parseExpressions(expressions);
		context.prepareExecute(exprInfos);
		exprInfos = sortExprs(exprInfos);
		
		Object[] result = new Object[expressions.size()];
		context.getTracer().startTimer();
		boolean flag = env.beforeExecute(context.getExecContext());
		context.getTracer().endTimer("完成执行环境初始化。");
		if (!flag) return result;
		
		context.getTracer().startTimer();
		for (ExprInfo exprInfo : exprInfos) {
			Evaluator evtor = new Evaluator(env);
			Value v =  evtor.execute(exprInfo.getExpr());
			Object r = v.getValue();
			result[exprInfo.getIndex()] = r;
		}
		context.getTracer().endTimer("完成求值。");
		context.getTracer().endTimer("结束。");
		return result;
	}
	
	private List<ExprInfo> sortExprs(List<ExprInfo> exprInfos) {
		if (needSort && exprInfos.size() >= 1 && context.getExecContext().hasAssign()) {
			ExprSorter sorter = new ExprSorter(context);
			exprInfos = sorter.sort();
		}
		return exprInfos;
	}
	
	public List<ExprInfo> parseExpressions(List<String> expressions) {
		context.getTracer().startTimer();
		List<ExprInfo> result = new ArrayList<ExprInfo>(expressions.size());
		for (int i = 0; i < expressions.size(); i++) {
			String src = expressions.get(i);
			Parser p = new Parser(src);
			Expr expr = p.expression();
			ExprInfo info = new ExprInfo(expr, src, i);
			result.add(info);
		}
		context.getTracer().endTimer("结束解析表达式。");
		return result;
	}
}
