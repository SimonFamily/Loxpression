package com.loxpression.expr;

import java.io.Serializable;

import com.loxpression.visitors.Visitor;

public abstract class Expr implements Serializable {
	private static final long serialVersionUID = -277471376085171228L;

	public abstract <R> R accept(Visitor<R> visitor);
}
