package com.loxpression;

import java.io.IOException;
import java.io.OutputStream;

import com.loxpression.execution.OpCode;
import com.loxpression.execution.chunk.Chunk;
import com.loxpression.execution.chunk.ChunkReader;
import com.loxpression.values.Value;

public class Disassembler {
	private OutputStream out;
	private ChunkReader chunkReader;

	public Disassembler() {
		this(System.out);
	}

	public Disassembler(OutputStream out) {
		this.out = out;
	}

	public void execute(Chunk chunk) {
		this.chunkReader = new ChunkReader(chunk, new Tracer());
		int expOrder = 0;
		OpCode op;
		println("POSITION", "CODE", "PARAMETER", "ORDER");
		for (;;) {
			String pos = String.valueOf(chunkReader.position());
			op = readCode();
			String param = "";
			switch (op) {
			case OP_BEGIN:
				expOrder = chunkReader.readInt();
				param = String.valueOf(expOrder);
				break;
			case OP_CONSTANT: {
				Value v = readConstant();
				param = v.toString();
				break;
			}
			case OP_GET_GLOBAL: {
				param = readString();
				break;
			}
			case OP_SET_GLOBAL: {
				param = readString();
				break;
			}
			case OP_GET_PROPERTY: {
				param = readString();
				break;
			}
			case OP_SET_PROPERTY: {
				param = readString();
				break;
			}
			case OP_CALL:
				param = readString();
				break;
			case OP_JUMP_IF_FALSE: {
				int offset = chunkReader.readInt();
				param = gotoOffset(offset);
				break;
			}
			case OP_JUMP: {
				int offset = chunkReader.readInt();
				param = gotoOffset(offset);
				break;
			}
			case OP_EXIT:
				println(pos, op.name(), "", String.valueOf(expOrder));
				return;
			default:
				break;
			}
			println(pos, op.name(), param, String.valueOf(expOrder));
		}
	}

	private String readString() {
		Value value = readConstant();
		return value.asString();
	}

	private Value readConstant() {
		int index = chunkReader.readInt();
		return chunkReader.readConst(index);
	}

	private OpCode readCode() {
		byte code = chunkReader.readByte();
		return OpCode.forValue(code);
	}
	
	private String gotoOffset(int offset) {
		int curPos = chunkReader.position();
		return String.format(":%s->to:%s", offset, curPos + offset);
	}

	private void println(String pos, String op, String param, String order) {
		try {
			this.out.write((String.format("%-10.10s", pos)).getBytes());
			this.out.write((String.format("%-20.18s", op)).getBytes());
			this.out.write((String.format("%-20.18s", param)).getBytes());
			this.out.write((order + "\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
