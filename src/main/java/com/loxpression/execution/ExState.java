package com.loxpression.execution;

import java.util.HashMap;
import java.util.Map;

public enum ExState {
	OK(0),
	ERROR(1);
	
	private int value;
	private static Map<Integer, ExState> mappings;
	private synchronized static Map<Integer, ExState> getMappings() {
		if (mappings == null) {
			mappings = new HashMap<Integer, ExState>();
		}
		return mappings;
	}
	
	private ExState(int value) {
		this.value = value;
		getMappings().put(value, this);
	}
	
	public int getValue() {
		return value;
	}
	
	public static ExState forValue(int value) {
		return getMappings().get(value);
	}
}
