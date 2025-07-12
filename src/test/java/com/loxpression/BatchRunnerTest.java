package com.loxpression;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.execution.chunk.Chunk;

class BatchRunnerTest extends TestBase {
	private static final int FORMULA_BATCHES = 1000;
	private static final String Directory = "BatchRunnerTest";
	
	@Test
	void testIR() {
		System.out.println("批量运算测试(解析执行)");
		List<String> lines = getExpressions();
		LoxRunner runner = new LoxRunner();
		runner.setExecuteMode(ExecuteMode.SyntaxTree);
		runner.setTrace(true);
		Environment env = getEnv();
		runner.execute(lines, env);
		checkValues(env);
		System.out.println("==========");
	}
	
	@Test
	void testCompileChunk() throws IOException {
		System.out.println("批量运算测试(编译+字节码执行)");
		long start = System.currentTimeMillis();
		List<String> lines = getExpressions();
		LoxRunner runner = new LoxRunner();
		runner.setTrace(true);
		Chunk chunk = runner.compileSource(lines);
		Environment env = getEnv();
		runner.runChunk(chunk, env);
		checkValues(env);
		System.out.println("总耗时(ms)：" + (System.currentTimeMillis() - start));
		System.out.println("==========");
	}

	@Test
	void testFileChunk() {
		System.out.println("字节码编译到文件再从文件读取执行");
		Path filePath = getPath(Directory, "Chunks.pb");
		Chunk chunk = createAndGetChunk(filePath);
		System.out.println("字节码大小(KB)：" + (chunk.getByteSize() / 1024));

		long start = System.currentTimeMillis();
		int cnt = 1;
		for (int i = 0; i < cnt; i++) {
			LoxRunner runner = new LoxRunner();
			Environment env = getEnv();
			runner.runChunk(chunk, env);
			checkValues(env);
		}
		System.out.println(String.format("执行完成，执行次数：%s。 总耗时(ms):%s", cnt, (System.currentTimeMillis() - start), null));	
		System.out.println("==========");
	}

	

	private Chunk createAndGetChunk(Path path) {
		List<String> lines = getExpressions();
		LoxRunner runner = new LoxRunner();
		long start = System.currentTimeMillis();
		Chunk chunk = runner.compileSource(lines);
		System.out.println("编译完成，耗时(ms)：" + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		writeChunkFile(chunk, path);
		System.out.println("序列化到文件完成，耗时(ms)：" + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		chunk = readChunkFile(path);
		System.out.println("从文件反序列化完成，耗时(ms)：" + (System.currentTimeMillis() - start));
		return chunk;
	}

	private List<String> getExpressions() {
		List<String> lines = new ArrayList<String>();
		String fml = "A! = 1 + 2 * 3 - 6 - 1 + B! + C! * (D! - E! + 10 ** 2 / 5 - (12 + 8)) - F! * G! +  100 / 5 ** 2 ** 1";
		String fml1 = "B! = C! + D! * 2 - 1";
		String fml2 = "C! = D! * 2 + 1";
		String fml3 = "D! = E! + F! * G!";
		String fml4 = "G! = M! + N!";
		
		for (int i = 0; i < FORMULA_BATCHES; i++) {
			lines.add(fml.replaceAll("!", String.valueOf(i)));
			lines.add(fml1.replaceAll("!", String.valueOf(i)));
			lines.add(fml2.replaceAll("!", String.valueOf(i)));
			lines.add(fml3.replaceAll("!", String.valueOf(i)));
			lines.add(fml4.replaceAll("!", String.valueOf(i)));
		}
		return lines;
	}

	private Environment getEnv() {
		Environment env = new DefaultEnvironment();
		for (int i = 0; i < FORMULA_BATCHES; i++) {
			env.put("E" + i, 2);
			env.put("F" + i, 3);
			env.put("M" + i, 4);
			env.put("N" + i, 5);
		}
		return env;
	}
	
	private void checkValues(Environment env) {
		Random rand = new Random();
		for (int i = 0; i < 10; i++) {
			int index = rand.nextInt(FORMULA_BATCHES);
			assertEquals(1686.0, env.get("A" + index).getValue());
			assertEquals(116, env.get("B" + index).getValue());
			assertEquals(59, env.get("C" + index).getValue());
			assertEquals(29, env.get("D" + index).getValue());
			assertEquals(9, env.get("G" + index).getValue());
		}
	}
}
