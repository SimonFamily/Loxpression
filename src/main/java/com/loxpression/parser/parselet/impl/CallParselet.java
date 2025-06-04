package com.loxpression.parser.parselet.impl;

import java.util.ArrayList;
import java.util.List;

import com.loxpression.expr.CallExpr;
import com.loxpression.expr.Expr;
import com.loxpression.parser.LoxParseError;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;
import com.loxpression.parser.TokenType;
import com.loxpression.parser.parselet.InfixParselet;

public class CallParselet implements InfixParselet {
	private int precedence;
	public CallParselet(int precedence) {
		this.precedence = precedence;
	}
	
	@Override
	public Expr parse(Parser parser, Expr lhs, Token token) {
		List<Expr> args = new ArrayList<Expr>();
		int argCount = 0;
		if (!parser.check(TokenType.RIGHT_PAREN)) {
			do {
				if (argCount == 255) {
			    	throw new LoxParseError(parser.peek(), "Can't have more than 255 arguments.");
				}
				Expr arg = parser.expression();
				args.add(arg);
				argCount++;
			} while (parser.match(TokenType.COMMA));
		}
		Token rParen = parser.consume(TokenType.RIGHT_PAREN, "Expect ')' after arguments.");
		return new CallExpr(lhs, args, rParen);
	}

	@Override
	public int getPrecedence() {
		return this.precedence;
	}

}
