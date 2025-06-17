package com.loxpression.expr;

import com.loxpression.visitors.Visitor;

public class IfExpr extends Expr {
	
	private static final long serialVersionUID = -7174468682531226449L;
	public final Expr condition;
	public final Expr thenBranch;
	public final Expr elseBranch;

	public IfExpr(Expr condition, Expr thenBranch, Expr elseBranch) {
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}

}
