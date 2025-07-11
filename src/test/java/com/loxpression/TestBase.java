package com.loxpression;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.loxpression.execution.chunk.Chunk;

public class TestBase {

	protected Path getPath(String directory, String fileName) {
		String outputDir = System.getProperty("user.dir");
		return Paths.get(outputDir, ".temp", directory, fileName);
	}
	
	protected <T> T deserializeObject(Path filePath) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toString()))) {
			T obj = (T) ois.readObject();
			return obj;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected  void serializeObject(Object obj, Path filePath) {
		createParentIfNotExist(filePath);
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toString()))) {
			oos.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void writeString(String content, Path filePath) {
		createParentIfNotExist(filePath);
		try (FileWriter writer = new FileWriter(filePath.toString())) {
			writer.write(content);
		} catch (IOException e) {
			System.err.println("文件写入出错: " + e.getMessage());
		}
	}
	
	protected void writeAllLines(List<String> lines, Path filePath) {
		createParentIfNotExist(filePath);
		PrintWriter writer;
		try {
			writer = new PrintWriter(filePath.toString(), "UTF-8");
			for (String line : lines) {
				writer.println(line);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void createParentIfNotExist(Path filePath) {
		Path parent = filePath.getParent();
		File file = parent.toFile();
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	protected void writeChunkFile(Chunk chunk, Path filePath) {
		try {
			createParentIfNotExist(filePath);
			byte[] bytes = chunk.toBytes();
			Files.write(filePath, bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected Chunk readChunkFile(Path filePath) {
		try {
			byte[] bytes  = Files.readAllBytes(filePath);
			return Chunk.valueOf(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
