package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.execution.chunk.Chunk;
import com.loxpression.expr.Expr;
import com.loxpression.ir.ExprInfo;

public class SerializeTest extends TestBase {
	private static final int FORMULA_BATCHES = 1000;
	private static final String Directory = "SerializeTest";
	
	@Test
	void test() throws IOException {
		System.out.println("序列化反序列化测试：");
		chunkSerializeTest();
	}
	
	void chunkSerializeTest() {
		List<String> lines = createFormulas();
		System.out.println("表达式总数：" + lines.size());
		System.out.println("开始解析和分析：");
		long start = System.currentTimeMillis();
		LoxRunner runner = new LoxRunner();
		List<Expr> exprs = runner.parse(lines);
		List<ExprInfo> exprInfos = runner.analyze(exprs);
		System.out.println("中间结果生成完成。" + " 耗时(ms):" + (System.currentTimeMillis() - start));
		Chunk chunk = runner.compileIR(exprInfos);
		Environment env = getEnvironment();
		
		testChunk(chunk, env);
		testSyntaxTree(exprInfos, env);
		System.out.println("==========");
	}

	private void testChunk(Chunk chunk, Environment env) {
		System.out.println("开始进行字节码序列化反序列化，字节码大小(KB)：" + (chunk.getByteSize() / 1024));
		long start = System.currentTimeMillis();
		Path path = getPath(Directory, "Chunks.pb");
		writeChunkFile(chunk, path);
		System.out.println("字节码已序列化到文件, 耗时(ms):" + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		chunk = readChunkFile(path);
		System.out.println("完成从文件反序列化字节码。" + " 耗时(ms):" + (System.currentTimeMillis() - start));
		
		System.out.println("开始执行字节码：");
		start = System.currentTimeMillis();
		LoxRunner runner = new LoxRunner();
		runner.runChunk(chunk, env);
		checkResult(env);
		System.out.println("字节码执行完成。" + " 耗时(ms):" + (System.currentTimeMillis() - start));
	}

	private void testSyntaxTree(List<ExprInfo> exprInfos, Environment env) {
		// todo 序列化语法树
		// todo反序列化语法树

		System.out.print("开始执行语法树");
		long start = System.currentTimeMillis();
		env = getEnvironment();
		LoxRunner runner = new LoxRunner();
		runner.runIR(exprInfos, env);
		checkResult(env);
		System.out.println("语法树执行完成。" + " 耗时(ms):" + (System.currentTimeMillis() - start));

	}

	private List<String> createFormulas() {
		String fml = "A! = 1 + 2 * 3 - 6 - 1 + B! + C! * (D! - E! + 10 ** 2 / 5 - (12 + 8)) - F! * G! +  100 / 5 ** 2 ** 1";
		String fml1 = "B! = C! + D! * 2 - 1";
		String fml2 = "C! = D! * 2 + 1";
		String fml3 = "D! = E! + F! * G!";
		String fml4 = "G! = M! + N!";
		List<String> lines = new ArrayList<String>(5 * FORMULA_BATCHES);

		for (int i = 0; i < FORMULA_BATCHES; i++) {
			lines.add(fml.replaceAll("!", String.valueOf(i)));
			lines.add(fml1.replaceAll("!", String.valueOf(i)));
			lines.add(fml2.replaceAll("!", String.valueOf(i)));
			lines.add(fml3.replaceAll("!", String.valueOf(i)));
			lines.add(fml4.replaceAll("!", String.valueOf(i)));
		}
		return lines;
	}
	
	private Environment getEnvironment() {
		Environment env = new DefaultEnvironment();
		for (int i = 0; i < FORMULA_BATCHES; i++) {
			env.put("E" + i, 2);
			env.put("F" + i, 3);
			env.put("M" + i, 4);
			env.put("N" + i, 5);
		}
		return env;
	}
	
	private void checkResult(Environment env) {
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
