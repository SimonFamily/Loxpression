package com.loxpression;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.loxpression.execution.chunk.Chunk;

public class DisassemblerTest {
	//@Test
	void test() {
		List<String> lines = new ArrayList<String>();
		lines.add("a + b * c - 100 / 5 ** 2 ** 1");
		lines.add("a + b * c >= 6");
		lines.add("1 + 2 - 3");
		lines.add("3 * (2 + 1)");
		lines.add("a + (b - c)");
		lines.add("a * 2 + (b - c)");
		lines.add("x = y = a + b * c");
		lines.add("a > 1 || b > 1 || c > 1 || d > 1");
		lines.add("aa > 11 && bb > 11 && cc > 11 && dd > 11");
		
		LoxRunner runner = new LoxRunner();
		Chunk chunk = runner.compileSource(lines);
		Disassembler disassembler = new Disassembler(System.out);
		disassembler.execute(chunk);
	}
}
