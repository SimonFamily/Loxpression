package com.loxpression.expr;

import com.loxpression.values.Value;
import com.loxpression.visitors.Visitor;

public class LiteralExpr extends Expr {
	public final Value value;

	public LiteralExpr(Value value) {
		this.value = value;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
