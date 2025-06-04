package com.loxpression;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DataGenerator {
	
	public static void main(String[] args) throws IOException {
		String outputDir = System.getProperty("user.dir");
		String path = Paths.get(outputDir, "test.txt").toString();
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		String expr = "A1 = B1 + C1 * (D1 - E1 + 10 ** 2 / 5 - (12 + 8)) - F1 * G1 +  100 / 5 ** 2 ** 1";
		for (int i = 1; i <= 10000; i++) {
			writer.println(String.format("A%s = B%s + C%s * (D%s - E%s + 10 ** 2 / 5 - (12 + 8)) - F%s * G%s +  100 / 5 ** 2 ** 1", i, i, i, i, i, i, i));
		}
		writer.close();
		System.out.println("success!");
	}
	
	public static void readData() throws IOException {
		Path path = Paths.get(System.getProperty("user.dir"), "test.txt");
		List<String> lines = Files.readAllLines(path);
	}
}
