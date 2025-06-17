package com.loxpression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.loxpression.env.DefaultEnvironment;
import com.loxpression.env.Environment;
import com.loxpression.execution.Chunk;
import com.loxpression.expr.Expr;
import com.loxpression.ir.ExprInfo;

public class SerializeTest {
	private static final int FORMULA_BATCHES = 10000;
	private static final int RUN_BATCHES = 100;
	//@Test
	void test() throws IOException {
		//formulaFileTest();
		//jsonSerializeTest();
		objectSerializeTest();
		chunkSerializeTest();
	}

	void formulaFileTest() throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("开始生成公式：");
		List<String> lines = createFormulas();
		String outputDir = System.getProperty("user.dir");
		String fileName = "formulas.txt";
		String path = Paths.get(outputDir, ".temp", fileName).toString();
		PrintWriter writer = new PrintWriter(path, "UTF-8");

		for (int i = 0; i < lines.size(); i++) {
			writer.println(lines.get(i));
		}
		writer.close();
		System.out.println("文件生成：" + fileName + " 耗时(ms):" + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		System.out.println("开始从文件进行编译：");
		lines = Files.readAllLines(Paths.get(path));
		LoxRunner runner = new LoxRunner();
		List<Expr> exprs = runner.parse(lines);
		List<ExprInfo> exprInfos = runner.analyze(exprs);
		System.out.println("完成编译公式文件。" + " 耗时(ms):" + (System.currentTimeMillis() - start));
		System.out.println("==========");
	}

	void objectSerializeTest() {
		long start = System.currentTimeMillis();
		System.out.println("开始进行语法树序列化：");
		List<String> lines = createFormulas();
		LoxRunner runner = new LoxRunner();
		List<Expr> exprs = runner.parse(lines);
		List<ExprInfo> exprInfos = runner.analyze(exprs);
		
		String outputDir = System.getProperty("user.dir");
		String fileName = "ExprInfos.ser";
		String path = Paths.get(outputDir, ".temp", fileName).toString();
		serializeObject(exprInfos, path);
		System.out.println("语法树已序列化到：" + fileName + " 耗时(s):" + (System.currentTimeMillis() - start) / 1000);

		start = System.currentTimeMillis();
		System.out.println("开始语法树反序列化：");
		exprInfos = deserializeObject(path);
		System.out.println("语法树反序列化完成。" + " 耗时(s):" + (System.currentTimeMillis() - start) / 1000);
		
		start = System.currentTimeMillis();
		System.out.println("开始执行语法树：");
		Environment env = getEnvironment();
		for (int i = 0; i < RUN_BATCHES; i++) {
			runner.interprete(exprInfos, env);			
		}
		checkResult(env);
		System.out.println("语法树执行完成。" + " 耗时(ms):" + (System.currentTimeMillis() - start));
		
		//String json = new Gson().toJson(exprInfos);
		//writeString(json, Paths.get(outputDir, ".temp", "ExprInfos.json").toString());
		System.out.println("==========");
	}
	
	void chunkSerializeTest() {
		long start = System.currentTimeMillis();
		System.out.println("开始进行字节码序列化：");
		List<String> lines = createFormulas();
		LoxRunner runner = new LoxRunner();
		List<Expr> exprs = runner.parse(lines);
		List<ExprInfo> exprInfos = runner.analyze(exprs);
		List<Chunk> chunks = runner.compile(exprInfos);
		
		String outputDir = System.getProperty("user.dir");
		String fileName = "Chunks.ser";
		String path = Paths.get(outputDir, ".temp", fileName).toString();
		serializeObject(chunks, path);
		System.out.println("字节码已序列化到：" + fileName + " 耗时(s):" + (System.currentTimeMillis() - start) / 1000);

		start = System.currentTimeMillis();
		System.out.println("开始字节码反序列化：");
		chunks = deserializeObject(path);
		System.out.println("字节码反序列化完成。" + " 耗时(s):" + (System.currentTimeMillis() - start) / 1000);
		
		start = System.currentTimeMillis();
		System.out.println("开始执行字节码：");
		Environment env = getEnvironment();
		for (int i = 0; i < RUN_BATCHES; i++) {
			runner.run(chunks, env);
		}
		checkResult(env);
		System.out.println("字节码执行完成。" + " 耗时(ms):" + (System.currentTimeMillis() - start));
		
		//String json = new Gson().toJson(chunks);
		//writeString(json, Paths.get(outputDir, ".temp", "Chunks.json").toString());
		System.out.println("==========");
	}

	void jsonSerializeTest() throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("开始进行json序列化：");
		List<String> lines = createFormulas();
		LoxRunner runner = new LoxRunner();
		List<Expr> exprs = runner.parse(lines);
		List<ExprInfo> exprInfos = runner.analyze(exprs);
		String outputDir = System.getProperty("user.dir");
		String fileName = "ExprInfos.json";
		String path = Paths.get(outputDir, ".temp", fileName).toString();
		String json = "";
		//json = new Gson().toJson(exprInfos);
		writeString(json, path);
		System.out.println("对象已序列化到：" + fileName + " 耗时(s):" + (System.currentTimeMillis() - start) / 1000);

		start = System.currentTimeMillis();
		System.out.println("开始从json文件反序列化：");
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		json = new String(bytes);
		//Type exprInfosType = new TypeToken<List<ExprInfo>>(){}.getType();
		//exprInfos = new Gson().fromJson(json, exprInfosType);
		System.out.println("json反序列化完成。" + " 耗时(s):" + (System.currentTimeMillis() - start) / 1000);
		System.out.println("==========");
	}

	private List<String> createFormulas() {
		String fml = "A! = 1 + 2 * 3 - 6 - 1 + B! + C! * (D! - E! + 10 ** 2 / 5 - (12 + 8)) - F! * G! +  100 / 5 ** 2 ** 1";
		String fml1 = "B! = C! + D! * 2 - 1";
		String fml2 = "C! = D! * 2 + 1";
		String fml3 = "D! = E! + F! * G!";
		String fml4 = "G! = M! + N!";
		List<String> lines = new ArrayList<String>(5 * FORMULA_BATCHES);

		for (int i = 1; i <= FORMULA_BATCHES; i++) {
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
		for (int i = 1; i <= FORMULA_BATCHES; i++) {
			env.put("E" + i, 2);
			env.put("F" + i, 3);
			env.put("M" + i, 4);
			env.put("N" + i, 5);
		}
		return env;
	}
	
	private void checkResult(Environment env) {
		assertEquals(1686.0, env.get("A1").getValue());
		assertEquals(116, env.get("B1").getValue());
		assertEquals(59, env.get("C1").getValue());
		assertEquals(29, env.get("D1").getValue());
		assertEquals(9, env.get("G1").getValue());
		assertEquals(1686.0, env.get("A5000").getValue());
		assertEquals(116, env.get("B5000").getValue());
		assertEquals(59, env.get("C5000").getValue());
		assertEquals(29, env.get("D5000").getValue());
		assertEquals(9, env.get("G5000").getValue());
		assertEquals(1686.0, env.get("A10000").getValue());
		assertEquals(116, env.get("B10000").getValue());
		assertEquals(59, env.get("C10000").getValue());
		assertEquals(29, env.get("D10000").getValue());
		assertEquals(9, env.get("G10000").getValue());
	}

	private static void writeString(String content, String filePath) {
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(content);
		} catch (IOException e) {
			System.err.println("文件写入出错: " + e.getMessage());
		}
	}

	private static void serializeObject(Object obj, String fileName) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 反序列化方法
	private static <T> T deserializeObject(String fileName) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			T obj = (T) ois.readObject();
			return obj;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
