package com.loxpression.execution;

import java.io.Serializable;
import com.loxpression.values.Value;

public class Chunk implements Serializable {
	private static final long serialVersionUID = -4950147318953824044L;
	
	private byte[] codes;
	private Value[] constants;
	private int index;
	
	public Chunk() {
		
	}

	public Chunk(byte[] codes, Value[] constants) {
		this.codes = codes;
		this.constants = constants;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public byte readCode(int index) {
		return codes[index];
	}

	public OpCode readOpCode(int index) {
		int code = readCode(index);
		return OpCode.forValue((byte)code);
	}

	public Value readContant(int index) {
		return constants[index];
	}

	public int codeSize() {
		return codes.length;
	}

	public int constSize() {
		return constants.length;
	}
}
