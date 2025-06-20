package com.loxpression.execution.chunk;

import java.io.Serializable;

public class Chunk implements Serializable {
	private static final long serialVersionUID = -4950147318953824044L;
	
	byte[] codes;
	byte[] constants;
	
	public Chunk() { }

	public Chunk(byte[] codes, byte[] constants) {
		this.codes = codes;
		this.constants = constants;
	}
	
	public int getByteSize() {
		return codes.length + constants.length;
	}
}
