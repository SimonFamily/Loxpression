package com.loxpression.visitors;

import java.util.ArrayList;
import java.util.List;
import com.loxpression.expr.Expr;
import com.loxpression.parser.Parser;

public abstract class VisitorBase<R> implements Visitor<R> {
	
	public R execute(String str) {
		Parser p = new Parser(str);
		Expr expr = p.expression();
		return expr.accept(this);
	}
	
	public abstract R execute(Expr expr);

	public List<R> execute(List<Expr> exprs) {
		if (exprs == null || exprs.size() == 0) return new ArrayList<>();
		List<R> results = new ArrayList<R>(exprs.size());
		for (Expr expr : exprs) {
			R v = execute(expr);
			results.add(v);
		}
		return results;
	}
}
