package com.loxpression.execution;

import java.util.ArrayList;

import com.loxpression.CompileError;
import com.loxpression.values.Value;

public class ChunkMaker {
	private ArrayList<Byte> codes;
	private ArrayList<Value> constants;

	public ChunkMaker() {
		codes = new ArrayList<Byte>();
		constants = new ArrayList<Value>();
	}
	
	public Chunk flush() {
		byte[] codeArr = new byte[codes.size()];
		for (int i = 0; i < codes.size(); i++) codeArr[i] = codes.get(i);
		Value[] constArr = constants.toArray(new Value[constants.size()]);
		return new Chunk(codeArr, constArr);
	}

	public void writeCode(byte code) {
		if (codes.size() == Byte.MAX_VALUE) {
			throw new CompileError(0, "字节码数量超出最大范围。");
		}
		codes.add(code);
	}

	public void writeCode(OpCode opCode) {
		writeCode(opCode.getValue());
	}
	
	public void updateCode(int index, byte code) {
		codes.set(index, code);
	}

	public byte addContant(Value value) {
		if (constants.size() == Byte.MAX_VALUE) {
			throw new CompileError(0, "常量池超出最大范围。");
		}
		constants.add(value);
		return (byte)(constants.size() - 1); // 返回索引
	}
}
