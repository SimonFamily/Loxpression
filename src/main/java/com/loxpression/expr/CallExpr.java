package com.loxpression.expr;

import java.util.List;

import com.loxpression.parser.Token;
import com.loxpression.visitors.Visitor;

public class CallExpr extends Expr {
	private static final long serialVersionUID = -1180324340117318674L;
	public final Expr callee;
	public final List<Expr> arguments;
	public final Token rParen;
	public CallExpr(Expr callee, List<Expr> arguments, Token rParen) {
		this.callee = callee;
		this.arguments = arguments;
		this.rParen = rParen;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}

}
