package com.loxpression.execution.chunk;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Collection;

import com.loxpression.Tracer;
import com.loxpression.execution.OpCode;
import com.loxpression.values.Value;

public class ChunkWriter {
	private ByteBuffer codeBuffer;
	private ConstantPool constPool;
	private BitSet isVarConst;
	
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
		byte[] varBytes = isVarConst.toByteArray();
		
		codeBuffer.clear();
		return new Chunk(codeBytes, constBytes, varBytes);
	}
	
	public void clear() {
		codeBuffer.clear();
		constPool.clear();
	}

	public void writeByte(byte value) {
		codeBuffer = ensureCapacity(Byte.BYTES, codeBuffer);
		codeBuffer.put(value);
	}

	public void writeShort(short value) {
		codeBuffer = ensureCapacity(Short.BYTES, codeBuffer);
		codeBuffer.putShort(value);
	}

	public void writeInt(int value) {
		codeBuffer = ensureCapacity(Integer.BYTES, codeBuffer);
		codeBuffer.putInt(value);
	}

	public void updateInt(int index, int value) {
		codeBuffer.putInt(index, value);
	}

	public void writeCode(OpCode opCode) {
		writeByte(opCode.getValue());
	}

	public int addConstant(Value value) {
		return constPool.addConst(value);
	}
	
	public void setVariables(Collection<String> vars) {
		int n = vars.size();
		isVarConst = new BitSet(n);
		for (String var : vars) {
			Integer index = constPool.getConstIndex(var);
			if (index == null) {
				index = constPool.addConst(new Value(var));
			}
			isVarConst.set(index);
		}
	}
	
	public int position() {
		return codeBuffer.position();
	}

	private ByteBuffer ensureCapacity(int byteCount, ByteBuffer buffer) {
		if (buffer.remaining() < byteCount) {
			return grow(buffer);
		}
		return buffer;
	}

	private ByteBuffer grow(ByteBuffer buffer) {
		int newCapacity = buffer.capacity() * 2;
		ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
		buffer.flip(); // 切换为读模式
		newBuffer.put(buffer);
		return newBuffer;
	}
}
