package com.loxpression.execution;

import java.util.Set;
import com.loxpression.expr.Expr;

public class ExprInfo {
	public Set<String> precursors; // 依赖的变量
	public Set<String> successors; // 被赋值的变量
	public Expr expr;
	public String src;
	public ExprInfo(Expr expr, Set<String> precursors, Set<String> successors) {
		this.expr = expr;
		this.precursors = precursors;
		this.successors = successors;
	}
}
