package com.loxpression.parser.parselet.impl;

import com.loxpression.expr.Expr;
import com.loxpression.expr.IdExpr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;
import com.loxpression.parser.parselet.PrefixParselet;

public class IdParselet implements PrefixParselet {

	@Override
	public Expr parse(Parser parser, Token token) {
		return new IdExpr(token.lexeme);
	}

}
