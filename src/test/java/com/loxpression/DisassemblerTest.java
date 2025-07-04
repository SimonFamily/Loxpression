package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.loxpression.execution.chunk.Chunk;

public class DisassemblerTest extends TestBase {
	private static final String Directory = "DisassemblerTest";
	
	@Test
	void test() throws IOException {
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
		Chunk chunk = runner.compileSource(lines); // 编译
		
		String fileName = "AssemblyOut.txt";
		Path path = getPath(Directory, fileName);
		createParentIfNotExist(path);
        try (FileOutputStream fos = new FileOutputStream(path.toString())) {
        	Disassembler disassembler = new Disassembler(fos);
        	disassembler.execute(chunk);
        }
        
        assertEquals(chunk.getByteSize(), 441); // 所有公式编译出来大小为441字节
	}
}
