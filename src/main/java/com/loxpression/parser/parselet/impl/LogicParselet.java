package com.loxpression.parser.parselet.impl;

import com.loxpression.expr.BinaryExpr;
import com.loxpression.expr.Expr;
import com.loxpression.expr.LogicExpr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;
import com.loxpression.parser.parselet.InfixParselet;

public class LogicParselet implements InfixParselet {

	private int precedence;
	private boolean isRight;

	public LogicParselet(int precedence) {
		this.precedence = precedence;
		this.isRight = false;
	}

	public LogicParselet(int precedence, boolean isRight) {
		this.precedence = precedence;
		this.isRight = isRight;
	}

	@Override
	public Expr parse(Parser parser, Expr lhs, Token token) {
		Expr rhs = parser.expressionPrec(isRight ? precedence - 1 : precedence);
		return new LogicExpr(lhs, token, rhs);
	}

	@Override
	public int getPrecedence() {
		return precedence;
	}

}
