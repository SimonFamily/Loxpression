package com.loxpression.values;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.loxpression.Instance;
import com.loxpression.LoxException;

public class Value implements Serializable {
	private static final long serialVersionUID = -7529873590511413244L;
	private Object v;
	private byte vt;
	
	public Object getValue() {
		return v;
	}
	
	public ValueType getValueType() {
		return ValueType.valueOf(vt);
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
	
	public static Value getFrom(ByteBuffer buffer) {
		byte tag = buffer.get();
		ValueType type = ValueType.valueOf(tag);
		switch (type) {
		case Integer:
			return new Value(buffer.getInt());
		case Double:
			return new Value(buffer.getDouble());
		case String:
			short len = buffer.getShort();
			byte[] bytes = new byte[len];
			buffer.get(bytes);
			String s = new String(bytes);
			return new Value(s);
		default:
			throw new LoxException(0, "暂不支持的类型：" + type);
		}
	}
	
	public short getByteSize() {
		ValueType type = getValueType();
		switch (type) {
		case Integer:
			return Integer.BYTES + 1;
		case Double:
			return (short)Double.BYTES + 1;
		case String:
			byte[] bytes = asString().getBytes();
			if (bytes.length > Short.MAX_VALUE) {
				throw new LoxException(0, "字符串超出最大长度：" + bytes.length);
			}
			return (short)(bytes.length + 3);
		default:
			throw new LoxException(0, "暂不支持的类型：" + getValueType());
		}
	}
	
	public void writeTo(ByteBuffer buffer) {
		ValueType type = getValueType();
		switch (type) {
		case Integer:
			buffer.put(vt);
			buffer.putInt(asInteger());
			break;
		case Double:
			buffer.put(vt);
			buffer.putDouble(asDouble());
			break;
		case String:
			byte[] bytes = asString().getBytes();
			buffer.put(vt);
			buffer.putShort((short)bytes.length);
			buffer.put(bytes);
			break;
		default:
			throw new LoxException(0, "暂不支持的类型：" + getValueType());
		}
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
		
		ValueType valueType = ValueType.valueOf(vt);
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
			return false;
		}
	}
}
