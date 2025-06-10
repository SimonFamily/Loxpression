package com.loxpression.expr;

import com.loxpression.parser.Token;
import com.loxpression.visitors.Visitor;

public class SetExpr extends Expr {
    public final Expr object;
    public final Token name;
    public final Expr value;
    
	public SetExpr(Expr object, Token name, Expr value) {
		this.object = object;
		this.name = name;
		this.value = value;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}

}
