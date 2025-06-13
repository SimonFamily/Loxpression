package com.loxpression.env;

import java.util.HashMap;
import java.util.Map;

import com.loxpression.execution.ExecuteContext;
import com.loxpression.values.Value;

public class DefaultEnvironment extends Environment {
	private Map<String, Value> map = new HashMap<>();
	
	@Override
	public boolean beforeExecute(ExecuteContext context) {
		return true;
	}
	
	@Override
	public Value get(String id) {
		return map.get(id);
	}

	@Override
	public Value getOrDefault(String id, Value defValue) {
		return map.getOrDefault(id, defValue);
	}
	
	@Override
	public void put(String id, Value value) {
		map.put(id, value);
	}

	@Override
	public int size() {
		return map.size();
	}
}
