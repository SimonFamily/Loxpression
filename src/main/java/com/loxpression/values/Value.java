package com.loxpression.values;

import com.loxpression.Instance;

public class Value {
	private Object v;
	private ValueType vt;
	
	public Object getValue() {
		return v;
	}
	
	public ValueType getValueType() {
		return vt;
	}
	
	public Value() {
		this.vt = ValueType.Null;
	}
	
	public Value(int v) {
		this.v = v;
		this.vt = ValueType.Integer;
	}
	
	public Value(double v) {
		this.v = v;
		this.vt = ValueType.Double;
	}
	
	public Value(String v) {
		this.v = v;
		this.vt = ValueType.String;
	}
	
	public Value(Instance v) {
		this.v = v;
		this.vt = ValueType.Instance;
	}
	
	public Value(boolean v) {
		this.v = v;
		this.vt = ValueType.Boolean;
	}
	
	public boolean isBoolean() {
		return vt == ValueType.Boolean;
	}
	
	public boolean isDouble() {
		return vt == ValueType.Double;
	}
	
	public boolean isInteger() {
		return vt == ValueType.Integer;
	}
	
	public boolean isNumber() {
		return vt == ValueType.Integer || vt == ValueType.Double;
	}
	
	public boolean isString() {
		return vt == ValueType.String;
	}
	
	public boolean isNull() {
		return vt == ValueType.Null;
	}
	
	public boolean isTruthy() {
		if (isNull()) {
			return false;
		} else if (isBoolean()) {
			return asBoolean();
		} else if (isString()) {
			return asString().length() > 0;
		}
		return true;
	}
	
	public boolean isInstance() {
		return vt == ValueType.Instance;
	}
	
	public boolean asBoolean() {
		return (boolean)v;
	}
	
	public int asInteger() {
		return ((Number)v).intValue();
	}
	
	public double asDouble() {
		return ((Number)v).doubleValue();
	}
	
	public String asString() {
		return (String)v;
	}
	
	public Instance asInstance() {
		return (Instance)v;
	}
	
	@Override
	public String toString() {
		if (v == null)
			return "null";
		return v.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Value))
			return false;
		Value other = (Value)o;
		if (this.vt != other.vt)
			return false;
		switch (vt) {
		case Null:
			return true;
		case Boolean:
			return asBoolean() == other.asBoolean();
		case Integer:
			return asInteger() == other.asInteger();
		case Double:
			return asDouble() == other.asDouble();
		case String:
			return asString().equals(other.asString());
		default:
			return false; // Unreachable.
		}
	}
}
