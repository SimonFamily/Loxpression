package com.loxpression;

import java.util.List;

import com.loxpression.execution.ExecuteContext;
import com.loxpression.execution.ExprInfo;
import com.loxpression.expr.Expr;

public class LoxContext {
	private Tracer tracer;
	private ExecuteContext execContext;

	public LoxContext() {
		tracer = new Tracer();
		execContext = new ExecuteContext(this);
	}
	
	public Tracer getTracer() {
		return tracer;
	}
	
	public ExecuteContext getExecContext() {
		return this.execContext;
	}
	
	public void prepareExecute(List<ExprInfo> exprInfos) {
		this.execContext.preExecute(exprInfos);
	}
}
