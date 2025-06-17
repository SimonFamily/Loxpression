package com.loxpression.execution;

import org.junit.jupiter.api.Test;

import com.loxpression.values.Value;
import static com.loxpression.execution.OpCode.*;

public class ChunkTest {
	@Test
	void test() {
		ChunkMaker chunkMaker = new ChunkMaker();
		int i = chunkMaker.addContant(new Value(1));
		chunkMaker.writeCode(OP_CONSTANT);
		chunkMaker.writeCode((byte)i);
		
		i = chunkMaker.addContant(new Value(2));
		chunkMaker.writeCode(OP_CONSTANT);
		chunkMaker.writeCode((byte)i);
		
		i = chunkMaker.addContant(new Value(3));
		chunkMaker.writeCode(OP_CONSTANT);
		chunkMaker.writeCode((byte)i);
		
		chunkMaker.writeCode(OP_MULTIPLY);
		chunkMaker.writeCode(OP_ADD);
		
		System.out.println("OK!" + Byte.MAX_VALUE);
	}
}
