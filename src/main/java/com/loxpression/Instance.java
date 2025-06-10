package com.loxpression;

import java.util.*;

import com.loxpression.values.Value;

public class Instance {
	private Clazz clazz;
	private final Map<String, Value> fields = new HashMap<>();
	
	public Instance() {
		
	}

	public Instance(Clazz clazz) {
		this.clazz = clazz;
	}

	public Value get(String name) {
		if (fields.containsKey(name)) {
			return fields.get(name);
		}
		throw new LoxRuntimeError("Undefined property '" + name + "'.");
	}

	public void set(String name, Value value) {
		fields.put(name, value);
	}

	@Override
	public String toString() {
		return clazz.name + " instance";
	}
}
