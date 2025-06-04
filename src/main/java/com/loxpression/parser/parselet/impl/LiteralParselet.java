package com.loxpression.parser.parselet.impl;

import com.loxpression.expr.Expr;
import com.loxpression.expr.LiteralExpr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;
import com.loxpression.parser.parselet.PrefixParselet;

public class LiteralParselet implements PrefixParselet {

	@Override
	public Expr parse(Parser parser, Token token) {
		return new LiteralExpr(token.literal);
	}
}
