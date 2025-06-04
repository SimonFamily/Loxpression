package com.loxpression.parser.parselet;

import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;

public interface InfixParselet {
	Expr parse(Parser parser, Expr lhs, Token token);
	int getPrecedence();
}