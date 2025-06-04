package com.loxpression.parser.parselet.impl;

import com.loxpression.expr.Expr;
import com.loxpression.expr.UnaryExpr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;
import com.loxpression.parser.parselet.PrefixParselet;

public class PreUnaryParselet implements PrefixParselet {
	private int precedence;
	public PreUnaryParselet(int precedence) {
		this.precedence = precedence;
	}

	@Override
	public Expr parse(Parser parser, Token token) {
		Expr rhs = parser.expressionPrec(precedence);
		return new UnaryExpr(token, rhs);
	}
}
