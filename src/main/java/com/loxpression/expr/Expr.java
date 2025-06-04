package com.loxpression.expr;

import com.loxpression.visitors.Visitor;

public abstract class Expr {
	public abstract <R> R accept(Visitor<R> visitor);
}
