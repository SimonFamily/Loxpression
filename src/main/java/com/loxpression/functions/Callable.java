package com.loxpression.functions;

import java.util.List;

import com.loxpression.expr.Expr;
import com.loxpression.values.Value;

public interface Callable {
	int arity(); 
	Value call(List<Value> arguments);
}
