package com.loxpression.parser.parselet;

import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;
import com.loxpression.parser.Token;

public interface PrefixParselet {
	Expr parse(Parser parser, Token token);
}
