package com.loxpression.parser.parselet.impl;

import com.loxpression.expr.Expr;
import com.loxpression.expr.IfExpr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Precedence;
import com.loxpression.parser.Token;
import com.loxpression.parser.TokenType;
import com.loxpression.parser.parselet.PrefixParselet;

public class IfParselet implements PrefixParselet {

	@Override
	public Expr parse(Parser parser, Token token) {
		parser.consume(TokenType.LEFT_PAREN, "Expect '(' after if.");
		Expr condition = parser.expressionPrec(Precedence.PREC_NONE);
		parser.consume(TokenType.COMMA, "Expect ',' after condition expression.");
		Expr thenExpr = parser.expressionPrec(Precedence.PREC_NONE);
		Expr elseExpr = null;
		if (parser.match(TokenType.COMMA)) {
			elseExpr = parser.expressionPrec(Precedence.PREC_NONE);
		}
		parser.consume(TokenType.RIGHT_PAREN, "Expect ')' at end of if expression.");
		return new IfExpr(condition, thenExpr, elseExpr);
	}

}
