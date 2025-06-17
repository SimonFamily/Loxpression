package com.loxpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.execution.Chunk;
import com.loxpression.execution.VM;
import com.loxpression.expr.Expr;
import com.loxpression.ir.ExprInfo;
import com.loxpression.ir.ExprSorter;
import com.loxpression.parser.Parser;
import com.loxpression.values.Value;
import com.loxpression.visitors.Evaluator;
import com.loxpression.visitors.OpCodeCompiler;

public class LoxRunner {
	private boolean needSort;
	private ExecuteMode executeMode;
	private LoxContext context;
	
	public LoxRunner() {
		this.needSort = true;
		this.executeMode = ExecuteMode.Interprete;
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

	public ExecuteMode getExecuteMode() {
		return executeMode;
	}

	public void setExecuteMode(ExecuteMode executeMode) {
		this.executeMode = executeMode;
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
		List<Expr> exprs = parse(expressions);
		List<ExprInfo> exprInfos = analyze(exprs);
		Object[] result;
		if (executeMode == ExecuteMode.Compile) {
			List<Chunk> chunks = compile(exprInfos);
			result = run(chunks, env);
		} else {
			result = interprete(exprInfos, env);			
		}
		context.getTracer().endTimer("结束。");
		return result;
	}
	
	public Object[] interprete(List<ExprInfo> exprInfos, Environment env) {
		int n = exprInfos.size();
		Object[] result = new Object[n];
		context.getTracer().startTimer();
		boolean flag = env.beforeExecute(context.getExecContext());
		context.getTracer().endTimer("完成执行环境初始化。");
		if (!flag) return result;
		
		context.getTracer().startTimer();
		for (ExprInfo info : exprInfos) {
			Expr expr = info.getExpr();
			Evaluator evtor = new Evaluator(env);
			Value v =  evtor.execute(expr);
			Object r = v.getValue();
			result[info.getIndex()] = r;
		}
		context.getTracer().endTimer("执行完成。");
		return result;
	}
	
	public Object[] run(List<Chunk> chunks, Environment env) {
		int n = chunks.size();
		Object[] result = new Object[n];
		context.getTracer().startTimer();
		boolean flag = env.beforeExecute(context.getExecContext());
		context.getTracer().endTimer("完成执行环境初始化。");
		if (!flag) return result;
		
		context.getTracer().startTimer();
		for (Chunk chunk : chunks) {
			Value v = VM.instance().execute(chunk, env);
			Object r = v.getValue();
			result[chunk.getIndex()] = r;
		}
		context.getTracer().endTimer("执行完成。");
		return result;
	}
	
	public List<Expr> parse(List<String> expressions) { // 生成语法树
		context.getTracer().startTimer();
		List<Expr> result = new ArrayList<Expr>(expressions.size());
		for (int i = 0; i < expressions.size(); i++) {
			String src = expressions.get(i);
			Parser p = new Parser(src);
			Expr expr = p.expression();
			result.add(expr);
		}
		context.getTracer().endTimer("完成表达式解析。");
		return result;
	}
	
	public List<ExprInfo> analyze(List<Expr> exprs) { // 变量提取、环境准备、排序等
		context.getTracer().startTimer();
		int n = exprs.size();
		List<ExprInfo> exprInfos = new ArrayList<ExprInfo>(n);
		for (int i = 0; i < n; i++) {
			Expr expr = exprs.get(i);
			ExprInfo exprInfo = new ExprInfo(expr, i);
			exprInfos.add(exprInfo);
		}
		context.prepareExecute(exprInfos);
		exprInfos = sortExprs(exprInfos);
		context.getTracer().endTimer("完成表达式分析。");
		return exprInfos;
	}
	
	public List<Chunk> compile(List<ExprInfo> exprInfos) {
		context.getTracer().startTimer();
		int n = exprInfos.size();
		List<Chunk> result = new ArrayList<Chunk>(n);
		for (ExprInfo exprInfo : exprInfos) {
			Expr expr = exprInfo.getExpr();
			OpCodeCompiler compiler = new OpCodeCompiler();
			Chunk chunk = compiler.compile(expr);
			chunk.setIndex(exprInfo.getIndex());
			result.add(chunk);
		}
		context.getTracer().endTimer("完成表达式编译。");
		return result;
	}
	
	private List<ExprInfo> sortExprs(List<ExprInfo> exprInfos) {
		if (needSort && exprInfos.size() >= 1 && context.getExecContext().hasAssign()) {
			ExprSorter sorter = new ExprSorter(context);
			exprInfos = sorter.sort();
		}
		return exprInfos;
	}
}
