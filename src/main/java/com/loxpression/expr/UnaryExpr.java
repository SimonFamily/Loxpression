package com.loxpression.expr;

import com.loxpression.parser.Token;
import com.loxpression.visitors.Visitor;

public class UnaryExpr extends Expr {
	public final Token operator;
	public final Expr right;

	public UnaryExpr(Token operator, Expr right) {
		this.operator = operator;
		this.right = right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}

}
