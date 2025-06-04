package com.loxpression;

import java.util.*;

import com.loxpression.values.Value;

public class Environment {
	private Map<String, Value> map = new HashMap<>();

	public Environment() {

	}

	public Value get(String id) {
		return map.get(id);
	}

	public Value getOrDefault(String id, Value defValue) {
		return map.getOrDefault(id, defValue);
	}

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
	
	public void put(String id, Value value) {
		map.put(id, value);
	}

	public int size() {
		return map.size();
	}

}
