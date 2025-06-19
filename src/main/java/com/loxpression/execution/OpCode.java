package com.loxpression.execution;

import java.util.*;

public enum OpCode {
	OP_CONSTANT((byte)0, "constant"),
	OP_NULL((byte)1, "null"), 
	OP_TRUE((byte)2, "true"), 
	OP_FALSE((byte)3, "false"),
	OP_POP((byte)4, "pop"),
	OP_GET_LOCAL((byte)5, "get_local"),
	OP_SET_LOCAL((byte)6, "set_local"),
	OP_GET_GLOBAL((byte)7, "get_global"),
	OP_DEFINE_GLOBAL((byte)8, "define_global"),
	OP_SET_GLOBAL((byte)9, "set_global"),
	OP_GET_PROPERTY((byte)10, "get_property"), 
	OP_SET_PROPERTY((byte)11, "set_property"),
	OP_EQUAL_EQUAL((byte)12, "=="),
	OP_BANG_EQUAL((byte)13, "!="),
	OP_GREATER((byte)14, ">"),
	OP_GREATER_EQUAL((byte)15, ">="),
	OP_LESS((byte)16, "<"),
	OP_LESS_EQUAL((byte)17, "<="),
	OP_ADD((byte)18, "+"),
	OP_SUBTRACT((byte)19, "-"),
	OP_MULTIPLY((byte)20, "*"),
	OP_DIVIDE((byte)21, "/"),
	OP_MODE((byte)22, "%"),
	OP_POWER((byte)23, "**"),
	OP_NOT((byte)24, "!"),
	OP_NEGATE((byte)25, "-"),
	OP_JUMP((byte)26, "jump"),
	OP_JUMP_IF_FALSE((byte)27, "jump_if_false"),
	OP_CALL((byte)28, "call"),
	OP_BEGIN((byte)29, "begin"),
	OP_END((byte)30, "end"),
	OP_RETURN((byte)31, "return"),
	OP_EXIT((byte)32, "exit");

	private byte code;
	private String title;
	private static Map<Byte, OpCode> mappings;
	private synchronized static Map<Byte, OpCode> getMappings() {
		if (mappings == null) {
			mappings = new HashMap<Byte, OpCode>();
		}
		return mappings;
	}
	
	private OpCode(byte code, String title) {
		this.code = code;
		this.title = title;
		getMappings().put(code, this);
	}
	
	public byte getValue() {
		return code;
	}
	
	public String getTitle() {
		return title;
	}
	
	public static OpCode forValue(byte value) {
		return getMappings().get(value);
	}
	
}
