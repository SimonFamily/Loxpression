package com.loxpression.values;

import java.util.*;

public enum ValueType {
	Integer((byte)1), 
	Long((byte)2),
	Float((byte)3),
	Double((byte)4), 
	String((byte)5), 
	Boolean((byte)6), 
	Instance((byte)7), 
	Null((byte)8);
	
	private byte value;
	private static Map<Byte, ValueType> mappings;
	private synchronized static Map<Byte, ValueType> getMappings() {
		if (mappings == null) {
			mappings = new HashMap<Byte, ValueType>();
		}
		return mappings;
	}
	
	private ValueType(byte value) {
		this.value = value;
		getMappings().put(value, this);
	}
	
	public byte getValue() {
		return value;
	}
	
	public static ValueType valueOf(byte value) {
		return getMappings().get(value);
	}
}
