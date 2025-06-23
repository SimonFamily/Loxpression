package com.loxpression.env;

import java.util.Collection;

import com.loxpression.Instance;
import com.loxpression.values.Value;

public abstract class Environment {
	
	public abstract boolean beforeExecute(Collection<String> vars);
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
