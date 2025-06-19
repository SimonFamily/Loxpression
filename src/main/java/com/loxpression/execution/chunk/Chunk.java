package com.loxpression.execution.chunk;

import java.io.Serializable;
import com.loxpression.values.Value;

public class Chunk implements Serializable {
	private static final long serialVersionUID = -4950147318953824044L;
	
	byte[] codes;
	Value[] constants;
	
	public Chunk() { }

	public Chunk(byte[] codes, Value[] constants) {
		this.codes = codes;
		this.constants = constants;
	}
}
