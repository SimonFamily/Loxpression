package com.loxpression.execution.chunk;

import java.nio.ByteBuffer;

import com.loxpression.Tracer;
import com.loxpression.execution.OpCode;
import com.loxpression.values.Value;

public class ChunkWriter {
	private ByteBuffer codeBuffer;
	private ConstantPool constPool;
	private Tracer tracer;
	
	public ChunkWriter(int capacity, Tracer tracer) {
		this.tracer = tracer;
		codeBuffer = ByteBuffer.allocate(Math.max(capacity, 128));
		constPool = new ConstantPool(capacity, tracer);
	}

	public ChunkWriter(Tracer tracer) {
		this.tracer = tracer;
		codeBuffer = ByteBuffer.allocate(1024);
		constPool = new ConstantPool(tracer);
	}

	public Chunk flush() {
		codeBuffer.flip();
		byte[] codeBytes = new byte[codeBuffer.remaining()];
		codeBuffer.get(codeBytes);
		
		byte[] constBytes = constPool.toBytes();
		
		codeBuffer.clear();
		return new Chunk(codeBytes, constBytes);
	}
	
	public void clear() {
		codeBuffer.clear();
		constPool.clear();
	}

	public void writeByte(byte value) {
		ensureCapacity(Byte.BYTES);
		codeBuffer.put(value);
	}

	public void writeShort(short value) {
		ensureCapacity(Short.BYTES);
		codeBuffer.putShort(value);
	}

	public void writeInt(int value) {
		ensureCapacity(Integer.BYTES);
		codeBuffer.putInt(value);
	}

	public void updateInt(int index, int value) {
		codeBuffer.putInt(index, value);
	}

	public void writeCode(OpCode opCode) {
		writeByte(opCode.getValue());
	}

	public int addContant(Value value) {
		return constPool.addConst(value);
	}
	
	public int position() {
		return codeBuffer.position();
	}

	private void ensureCapacity(int byteCount) {
		if (codeBuffer.remaining() < byteCount) {
			grow();
		}
	}

	private void grow() {
		int newCapacity = codeBuffer.capacity() * 2;
		ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
		codeBuffer.flip(); // 切换为读模式
		newBuffer.put(codeBuffer);
		codeBuffer = newBuffer;
	}
}
