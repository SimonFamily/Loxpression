package com.loxpression.env.form.service;

import java.io.IOException;
import java.util.*;

import com.loxpression.env.form.Utils;
import com.loxpression.env.form.defines.DataRow;
import com.loxpression.env.form.defines.DataTable;
import com.loxpression.env.form.defines.FieldDefine;

public class DataQuery {
	private String key;
	private String value;
	private Map<String, List<String>> tableFields = new HashMap<>();
	private Map<String, Integer> fieldsIndex = new HashMap<>();
	private List<FieldDefine> fields = new ArrayList<>();
	private FormService service;
	
	public DataQuery(String key, String value) { // 测试，仅支持一个维度
		this.key = key;
		this.value = value;
		this.service = new FormService("formId1");
	}
	
	public int addColumn(FieldDefine field) {
		Integer index = fieldsIndex.get(field.getCode());
		if (index != null) return index;
		
		String tableName = field.getTableName();
		String fieldName = field.getName();
		tableFields.computeIfAbsent(tableName, k -> new ArrayList<>()).add(fieldName);
		fields.add(field);
		index = fields.size() - 1;
		fieldsIndex.put(field.getCode(), index);
		return index;
	}
	
	public boolean containsField(FieldDefine field) {
		Integer index = fieldsIndex.get(field.getCode());
		return index != null;
	}
	
	public DataTable executeQuery() throws IOException {
		fillDimension();
		String[] headers = new String[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			FieldDefine field = fields.get(i);
			headers[i] = field.getCode();
		}
		
		DataRow row = new DataRow(headers);
		for (Map.Entry<String, List<String>> entry : tableFields.entrySet()) {
			String tableName = entry.getKey();
			List<String> fieldNames = entry.getValue();
			List<DataRow> rows = Utils.readTableData(tableName, fieldNames.toArray(new String[fieldNames.size()]));
			for (DataRow tmpRow : rows) {
				if (value.equals(tmpRow.getAsString(key))) {
					assign(row, tmpRow, tableName);
					break;
				}
			}
		}
		DataTable result = new DataTable();
		result.appendRow(row);
		return result;
	}
	
	private void fillDimension() {
		for (Map.Entry<String, List<String>> entry : tableFields.entrySet()) {
			String tableName = entry.getKey();
			FieldDefine keyField = service.getTableKeyField(tableName);
			if (keyField == null) {
				throw new RuntimeException(String.format("存储表%s没有主键id字段！", tableName));
			}
			if (!containsField(keyField)) {
				addColumn(keyField);
			}
		}
	}
	
	private void assign(DataRow row, DataRow other, String tableName) {
		String[] names = other.getColumns();
		for (String name : names) {
			Object value = other.getValue(name);
			row.setValue(tableName + "." + name, value);
		}
	}

}
