package com.loxpression.parser.parselet.impl;

import com.loxpression.expr.BinaryExpr;
import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;
import com.loxpression.parser.parselet.InfixParselet;

public class BinaryParselet implements InfixParselet {
	private int precedence;
	private boolean isRight;

	public BinaryParselet(int precedence) {
		this.precedence = precedence;
		this.isRight = false;
	}

	public BinaryParselet(int precedence, boolean isRight) {
		this.precedence = precedence;
		this.isRight = isRight;
	}

	@Override
	public Expr parse(Parser parser, Expr lhs, Token token) {
		Expr rhs = parser.expressionPrec(isRight ? precedence - 1 : precedence);
		return new BinaryExpr(lhs, token, rhs);
	}

	@Override
	public int getPrecedence() {
		return precedence;
	}

}
