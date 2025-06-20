package com.loxpression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.execution.ExResult;
import com.loxpression.execution.VM;
import com.loxpression.execution.chunk.Chunk;
import com.loxpression.expr.Expr;
import com.loxpression.ir.ExprInfo;
import com.loxpression.ir.ExprSorter;
import com.loxpression.parser.Parser;
import com.loxpression.values.Value;
import com.loxpression.visitors.Evaluator;
import com.loxpression.visitors.OpCodeCompiler;

public class LoxRunner {
	private boolean needSort;
	private ExecuteMode executeMode = ExecuteMode.SyntaxTree;
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

	public ExecuteMode getExecuteMode() {
		return executeMode;
	}

	public void setExecuteMode(ExecuteMode executeMode) {
		this.executeMode = executeMode;
	}

	public Object execute(String expression) {
		Object[] result = execute(Arrays.asList(expression), new DefaultEnvironment());
		return result == null ? null : result[0];
	}
	
	public Object execute(String expression, Environment env) {
		Object[] result = execute(Arrays.asList(expression), env);
		return result == null ? null : result[0];
	}
	
	public Object[] execute(List<String> exprs) {
		return execute(exprs, new DefaultEnvironment());
	}
	
	public Object[] execute(List<String> expressions, Environment env) {
		context.getTracer().startTimer("开始。公式总数：%s", expressions.size());
		List<Expr> exprs = parse(expressions);
		List<ExprInfo> exprInfos = analyze(exprs);
		Object[] result = null;
		if (executeMode == ExecuteMode.ChunkVM) { // 单元测试中使用
			Chunk chunk = compileIR(exprInfos);
			result  = runChunk(chunk, env);
		} else {
			result  = runIR(exprInfos, env);
		}
		context.getTracer().endTimer("结束。");
		return result;
	}
	
	// 执行中间表示
	public Object[] runIR(List<ExprInfo> exprInfos, Environment env) {
		context.getTracer().startTimer();
		boolean flag = env.beforeExecute(context.getExecContext());
		context.getTracer().endTimer("完成执行环境初始化。");
		if (!flag) return null;
		
		context.getTracer().startTimer("执行");
		int n = exprInfos.size();
		Object[] result = new Object[n];
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
	
	// 执行字节码
	public Object[] runChunk(Chunk chunk, Environment env) {
		context.getTracer().startTimer();
		boolean flag = env.beforeExecute(context.getExecContext());
		context.getTracer().endTimer("完成执行环境初始化。");
		if (!flag) return null;
		
		context.getTracer().startTimer("执行");
		VM vm = new VM(context.getTracer());
		List<ExResult> exResults = vm.execute(chunk, env);
		Object[] result = new Object[exResults.size()];
		for (ExResult res : exResults) {
			Value r = res.getResult();
			Object o = r.getValue();
			int index = res.getIndex();
			result[index] = o;				
		}
		context.getTracer().endTimer("执行完成。");
		return result;
	}
	
	// 解析生成语法树
	public List<Expr> parse(List<String> expressions) {
		context.getTracer().startTimer("解析");
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
	
	// 变量提取、环境准备、排序等，得到中间表示结构
	public List<ExprInfo> analyze(List<Expr> exprs) {
		context.getTracer().startTimer("分析");
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
	
	// 编译源码
	public Chunk compileSrc(List<String> expressions) {
		context.getTracer().startTimer("编译源码");
		List<Expr> exprs = parse(expressions);
		List<ExprInfo> exprInfos = analyze(exprs);
		Chunk chunk = compileIR(exprInfos);
		context.getTracer().endTimer("完成表达式编译。");
		return chunk;
	}
	
	// 编译中间表示
	public Chunk compileIR(List<ExprInfo> exprInfos) {
		context.getTracer().startTimer("编译中间表示");
		OpCodeCompiler compiler = new OpCodeCompiler(exprInfos.size(), context.getTracer());
		compiler.beginCompile();
		for (ExprInfo exprInfo : exprInfos) {
			Expr expr = exprInfo.getExpr();
			int index = exprInfo.getIndex();
			compiler.compile(expr, index);
		}
		Chunk result = compiler.endCompile();
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
