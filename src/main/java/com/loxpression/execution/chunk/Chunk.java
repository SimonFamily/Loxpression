package com.loxpression.execution.chunk;

import java.io.Serializable;
import java.nio.ByteBuffer;

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
	
	public byte[] toBytes() {
		int sz = getByteSize() + 3 * 4;
		ByteBuffer buf = ByteBuffer.allocate(sz);
		buf.putInt(getCodesSize());
		buf.put(codes);
		buf.putInt(getConstsSize());
		buf.put(constants);
		buf.putInt(getVarsSize());
		buf.put(vars);
		
		buf.flip();
		byte[] bytes = new byte[sz];
		buf.get(bytes);
		return bytes;
	}

	public static Chunk valueOf(byte[] bytes) {
		ByteBuffer buf = ByteBuffer.wrap(bytes);
		Chunk chunk = new Chunk();
		int codeSz = buf.getInt();
		chunk.codes = new byte[codeSz];
		buf.get(chunk.codes);
		
		int constSz = buf.getInt();
		chunk.constants = new byte[constSz];
		buf.get(chunk.constants);
		
		int varSz = buf.getInt();
		chunk.vars = new byte[varSz];
		buf.get(chunk.vars);
		return chunk;
	}
}
