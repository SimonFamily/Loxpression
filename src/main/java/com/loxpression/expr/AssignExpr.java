package com.loxpression.expr;

import com.loxpression.parser.Token;
import com.loxpression.visitors.Visitor;

public class AssignExpr extends Expr {
	private static final long serialVersionUID = -137979351705670773L;
	public final Expr left;
	public final Token operator;
	public final Expr right;

	public AssignExpr(Expr left, Token operator, Expr right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}
}
