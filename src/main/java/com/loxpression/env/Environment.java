package com.loxpression.env;

import com.loxpression.Instance;
import com.loxpression.ir.ExecuteContext;
import com.loxpression.values.Value;

public abstract class Environment {
	
	public abstract boolean beforeExecute(ExecuteContext context);
	public abstract Value get(String id);
	public abstract Value getOrDefault(String id, Value defValue);
	public abstract void put(String id, Value value);
	public abstract int size();
	
	public void put(String id, int value) {
		put(id, new Value(value));
	}

	public void put(String id, double value) {
		put(id, new Value(value));
	}

	public void put(String id, String value) {
		put(id, new Value(value));
	}

	public void put(String id, boolean value) {
		put(id, new Value(value));
	}
	
	public void put(String id, Instance obj) {
		put(id, new Value(obj));
	}
}
