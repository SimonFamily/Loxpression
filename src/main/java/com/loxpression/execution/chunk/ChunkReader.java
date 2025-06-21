package com.loxpression.execution.chunk;

import java.nio.ByteBuffer;

import com.loxpression.Tracer;
import com.loxpression.execution.OpCode;
import com.loxpression.values.Value;

public class ChunkReader { // read后指针会往前移动，效果和虚拟机ip指针一样
	private ByteBuffer codeBuffer;
	private ConstantPool constPool;
	private Tracer tracer;
	
	public ChunkReader(Chunk chunk, Tracer tracer) {
		this.tracer = tracer;
		this.codeBuffer = ByteBuffer.wrap(chunk.codes);
		this.constPool = new ConstantPool(chunk.constants, tracer);
	}
	
	public byte readByte() {
		return codeBuffer.get();
	}
	
	public short readShort() {
		return codeBuffer.getShort();
	}
	
	public int readInt() {
		return codeBuffer.getInt();
	}

	public OpCode readOpCode() {
		byte code = readByte();
		return OpCode.forValue(code);
	}

	public Value readConst(int index) {
		return constPool.readConst(index);
	}
	
	public int position() {
		return codeBuffer.position();
	}
	
	public void newPosition(int newPos) {
		codeBuffer.position(newPos);
	}

	public int codeSize() {
		return codeBuffer.limit();
	}
}
