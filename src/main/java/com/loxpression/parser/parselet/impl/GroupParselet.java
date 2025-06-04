package com.loxpression.parser.parselet.impl;

import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Precedence;
import com.loxpression.parser.Token;
import com.loxpression.parser.TokenType;
import com.loxpression.parser.parselet.PrefixParselet;

public class GroupParselet implements PrefixParselet {

	@Override
	public Expr parse(Parser parser, Token token) {
		Expr expr = parser.expressionPrec(Precedence.PREC_NONE);
		parser.consume(TokenType.RIGHT_PAREN);
		return expr;
	}

}
