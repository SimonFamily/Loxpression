package com.loxpression.functions.num;

import java.util.List;

import com.loxpression.LoxRuntimeError;
import com.loxpression.functions.Const;
import com.loxpression.functions.Function;
import com.loxpression.values.Value;

public class Abs extends Function {

	public Abs() {
		super("abs", "绝对值", Const.NUMBER_GROUP);
	}

	@Override
	public int arity() {
		return 1;
	}

	@Override
	public Value call(List<Value> arguments) {
		if (arguments == null || arguments.size() != 1 || !arguments.get(0).isNumber()) {
			throw new LoxRuntimeError("参数不合法！");
		}
		Value v = arguments.get(0);
		if (v.isDouble()) {
			return new Value(Math.abs(v.asDouble()));
		} else {
			return new Value(Math.abs(v.asInteger()));
		}
	}

}
