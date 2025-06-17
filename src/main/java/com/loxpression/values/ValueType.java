package com.loxpression.values;

import java.util.*;

public enum ValueType {
	Integer(0), 
	Double(1), 
	String(2), 
	Boolean(3), 
	Instance(4), 
	Null(5);
	
	private int value;
	private static Map<Integer, ValueType> mappings;
	private synchronized static Map<Integer, ValueType> getMappings() {
		if (mappings == null) {
			mappings = new HashMap<Integer, ValueType>();
		}
		return mappings;
	}
	
	private ValueType(int value) {
		this.value = value;
		getMappings().put(value, this);
	}
	
	public int getValue() {
		return value;
	}
	
	public static ValueType forValue(int value) {
		return getMappings().get(value);
	}
}
