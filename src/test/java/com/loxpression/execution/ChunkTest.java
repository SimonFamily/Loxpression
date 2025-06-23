package com.loxpression.execution;

import org.junit.jupiter.api.Test;

import com.loxpression.Tracer;
import com.loxpression.execution.chunk.ChunkWriter;
import com.loxpression.values.Value;
import static com.loxpression.execution.OpCode.*;

import java.nio.ByteBuffer;

public class ChunkTest {
	@Test
	void test() {
		ChunkWriter chunkMaker = new ChunkWriter(new Tracer());
		int i = chunkMaker.addConstant(new Value(1));
		chunkMaker.writeCode(OP_CONSTANT);
		chunkMaker.writeByte((byte)i);
		
		i = chunkMaker.addConstant(new Value(2));
		chunkMaker.writeCode(OP_CONSTANT);
		chunkMaker.writeByte((byte)i);
		
		i = chunkMaker.addConstant(new Value(3));
		chunkMaker.writeCode(OP_CONSTANT);
		chunkMaker.writeByte((byte)i);
		
		chunkMaker.writeCode(OP_MULTIPLY);
		chunkMaker.writeCode(OP_ADD);
		
		//System.out.println("OK!" + Byte.MAX_VALUE);
	}
	
	@Test
	void buffTest() {
		byte[] byteArray = new byte[1024];
		ByteBuffer wrappedBuffer = ByteBuffer.wrap(byteArray);
		println(wrappedBuffer.position());
		println(wrappedBuffer.remaining());
		println(wrappedBuffer.limit());
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		println(buffer.position());
		buffer.putInt(123);
		println(buffer.position());
		buffer.putDouble(3.14);
		println(buffer.position());
		buffer.put("Hello".getBytes());
		println(buffer.position());
		buffer.put("你好呀！".getBytes());
		println(buffer.position());
		println(buffer.remaining());
		println("----------");
		
		// 读取数据示例
		buffer.flip();  // 切换为读模式
		println(buffer.limit());
		println(buffer.position());
		println(buffer.remaining());
		
		println(buffer.remaining());
		println(buffer.getInt());
		println(buffer.remaining());
		println(buffer.getDouble());
		println(buffer.remaining());
		byte[] strBytes = new byte[5];
		buffer.get(strBytes);
		println(new String(strBytes));
		println(buffer.remaining());
		println(buffer.limit());
		
		// 创建正确大小的数组
		buffer.flip();
		byte[] result = new byte[buffer.remaining()];
		buffer.get(result);
	}
	
	private static void println(Object x) {
		//System.out.println(x);
	}
}
