package com.loxpression;

import java.util.List;

import com.loxpression.expr.Expr;
import com.loxpression.ir.ExecuteContext;
import com.loxpression.ir.ExprInfo;

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
