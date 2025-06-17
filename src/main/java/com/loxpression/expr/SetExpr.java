package com.loxpression.expr;

import com.loxpression.parser.Token;
import com.loxpression.visitors.Visitor;

public class SetExpr extends Expr {
    private static final long serialVersionUID = -1216856023276152716L;
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
