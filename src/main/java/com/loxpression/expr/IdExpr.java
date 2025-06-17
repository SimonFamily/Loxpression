package com.loxpression.expr;

import com.loxpression.visitors.Visitor;

public class IdExpr extends Expr {
	private static final long serialVersionUID = 8798584586485932035L;
	public final String id;
	public IdExpr(String id) {
		this.id = id;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return this.id;
	}
}
