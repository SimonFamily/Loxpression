package com.loxpression.functions.sys;

import java.util.List;

import com.loxpression.expr.Expr;
import com.loxpression.functions.Const;
import com.loxpression.functions.Function;
import com.loxpression.values.Value;

public class Clock extends Function {

	public Clock() {
		super("clock", "当前毫秒数", Const.SYSTEM_GROUP);
	}

	@Override
	public int arity() {
		return 0;
	}

	@Override
	public Value call(List<Value> arguments) {
		Long t = System.currentTimeMillis();
		return new Value(String.valueOf(t));
	}

}
