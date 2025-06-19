package com.loxpression.values;

import java.io.Serializable;

import com.loxpression.Instance;

public class Value implements Serializable {
	private static final long serialVersionUID = -7529873590511413244L;
	private Object v;
	private byte vt;
	
	public Object getValue() {
		return v;
	}
	
	public ValueType getValueType() {
		return ValueType.forValue(vt);
	}
	
	public Value() {
		this.vt = ValueType.Null.getValue();
	}
	
	public Value(int v) {
		this.v = v;
		this.vt = ValueType.Integer.getValue();
	}
	
	public Value(double v) {
		this.v = v;
		this.vt = ValueType.Double.getValue();
	}
	
	public Value(String v) {
		this.v = v;
		this.vt = ValueType.String.getValue();
	}
	
	public Value(Instance v) {
		this.v = v;
		this.vt = ValueType.Instance.getValue();
	}
	
	public Value(boolean v) {
		this.v = v;
		this.vt = ValueType.Boolean.getValue();
	}
	
	public boolean isBoolean() {
		return vt == ValueType.Boolean.getValue();
	}
	
	public boolean isDouble() {
		return vt == ValueType.Double.getValue();
	}
	
	public boolean isInteger() {
		return vt == ValueType.Integer.getValue();
	}
	
	public boolean isNumber() {
		return vt == ValueType.Integer.getValue() || vt == ValueType.Double.getValue();
	}
	
	public boolean isString() {
		return vt == ValueType.String.getValue();
	}
	
	public boolean isNull() {
		return vt == ValueType.Null.getValue();
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
		return vt == ValueType.Instance.getValue();
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
		
		ValueType valueType = ValueType.forValue(vt);
		switch (valueType) {
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
