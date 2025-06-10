package com.loxpression.parser.parselet.impl;

import com.loxpression.expr.Expr;
import com.loxpression.expr.GetExpr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;
import com.loxpression.parser.TokenType;
import com.loxpression.parser.parselet.InfixParselet;

public class GetParselet implements InfixParselet {
	private int precedence;
	public GetParselet(int precedence) {
		this.precedence = precedence;
	}

	@Override
	public Expr parse(Parser parser, Expr lhs, Token token) {
		Token name = parser.consume(TokenType.IDENTIFIER, "Expect property name after '.'.");
		Expr expr = new GetExpr(lhs, name);
		return expr;
	}

	@Override
	public int getPrecedence() {
		return this.precedence;
	}

}
