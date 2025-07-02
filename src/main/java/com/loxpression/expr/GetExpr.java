package com.loxpression.expr;

import com.loxpression.parser.Token;
import com.loxpression.visitors.Visitor;

public class GetExpr extends Expr {
    private static final long serialVersionUID = -4847959043024514878L;
	public final Expr object;
    public final Token name;
	
	public GetExpr(Expr object, Token name) {
		this.object = object;
		this.name = name;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visit(this);
	}
}
