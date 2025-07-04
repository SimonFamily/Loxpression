package com.loxpression.execution.chunk;

import java.io.Serializable;

public class Chunk implements Serializable {
	private static final long serialVersionUID = -4950147318953824044L;
	
	byte[] codes; // 字节码
	byte[] constants; // 常量池
	byte[] vars; // 变量信息
	
	public Chunk() { }

	public Chunk(byte[] codes, byte[] constants, byte[] vars) {
		this.codes = codes;
		this.constants = constants;
		this.vars = vars;
	}
	
	public int getByteSize() {
		return codes.length + constants.length + vars.length;
	}
	
	public int getCodesSize() {
		return codes.length;
	}
	
	public int getConstsSize() {
		return constants.length;
	}
	
	public int getVarsSize() {
		return vars.length;
	}
}
