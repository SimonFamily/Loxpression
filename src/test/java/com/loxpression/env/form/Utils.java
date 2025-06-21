package com.loxpression.env.form;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.loxpression.env.form.defines.DataRow;

public class Utils {
	
	public static List<DataRow> readTableData(String tableName, String[] colNames) throws IOException {
		List<DataRow> result = new ArrayList<DataRow>();
		List<String> lines = readAllLines(tableName);
		if (lines == null || lines.size() <= 1) {
			return result;
		}
		
		String[] headers = lines.get(0).split(",");
		for (int i = 1; i < lines.size(); i++) {
			DataRow row = new DataRow(colNames);
			String[] colDatas = lines.get(i).split(",");
			for (int j = 0; j < headers.length; j++) {
				if (row.hasColumn(headers[j])) {
					row.setValue(headers[j], colDatas[j]);
				}
			}
			result.add(row);
		}
		return result;
	}
	
	public static List<DataRow> readTableData(String name) throws IOException {
		List<DataRow> result = new ArrayList<DataRow>();
		List<String> lines = readAllLines(name);
		if (lines == null || lines.size() <= 1) {
			return result;
		}
		String[] headers = lines.get(0).split(",");
		for (int i = 1; i < lines.size(); i++) {
			DataRow row = new DataRow(headers);
			String[] colDatas = lines.get(i).split(",");
			for (int j = 0; j < headers.length; j++) {
				row.setValue(j, colDatas[j]);
			}
			result.add(row);
		}
		return result;
	}
	
	public static List<String> readAllLines(String tableName) throws IOException {
		String file = Utils.class.getResource(String.format("/tables/%s.csv", tableName)).getFile();
		List<String> lines = Files.readAllLines(Paths.get(file));
		return lines;
	}
}
