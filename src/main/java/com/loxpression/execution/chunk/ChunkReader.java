package com.loxpression.execution.chunk;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import com.loxpression.Tracer;
import com.loxpression.execution.OpCode;
import com.loxpression.values.Value;

public class ChunkReader { // read后指针会往前移动，效果和虚拟机ip指针一样
	private ByteBuffer codeBuffer;
	private ConstantPool constPool;
	private BitSet isVarConst;
	private Tracer tracer;
	
	public ChunkReader(Chunk chunk, Tracer tracer) {
		this.tracer = tracer;
		this.codeBuffer = ByteBuffer.wrap(chunk.codes);
		this.constPool = new ConstantPool(chunk.constants, tracer);
		this.isVarConst = BitSet.valueOf(chunk.vars);
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
	
	public Collection<String> getVariables() {
		this.tracer.startTimer();
		List<Value> values = constPool.getAllConsts();
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < values.size(); i++) {
			if (isVarConst.get(i)) {
				result.add(values.get(i).toString());
			}
		}
		this.tracer.endTimer("构造变量列表");
		return result;
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
