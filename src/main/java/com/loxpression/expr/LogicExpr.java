package com.loxpression.expr;

import com.loxpression.parser.Token;
import com.loxpression.visitors.Visitor;

public class LogicExpr extends Expr {
	public final Expr left;
	public final Token operator;
	public final Expr right;

	public LogicExpr(Expr left, Token operator, Expr right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}

}
