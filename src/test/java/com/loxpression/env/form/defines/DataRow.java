package com.loxpression.env.form.defines;

import java.util.*;

public class DataRow {
	private Object[] values;
	private Map<String, Integer> nameIndexs;
	private String[] colNames;
	
	public DataRow(String[] colNames) {
		this.colNames = colNames;
		if (colNames == null || colNames.length == 0) {
			throw new IllegalArgumentException("colNames");
		}
		values = new Object[colNames.length];
		nameIndexs = new HashMap<String, Integer>();
		this.init(colNames);
	}
	
	private void init(String[] colNames) {
		for (int i = 0; i < colNames.length; i++) {
			nameIndexs.put(colNames[i], i);
		}
	}
	
	public boolean hasColumn(String colName) {
		return indexOfColumn(colName) != -1;
	}
	
	public String[] getColumns() {
		return colNames;
	}
	
	public int indexOfColumn(String colName) {
		Integer i = nameIndexs.get(colName);
		if (i == null) return -1;
		return i;
	}
	
	public Object getValue(int index) {
		if (index < 0 || index >= values.length) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}
		return values[index];
	}
	
	public String getAsString(int index) {
		Object value = getValue(index);
		if (value == null) return null;
		return value.toString();
	}
	
	public Integer getAsInt(int index) {
		Object value = getValue(index);
		if (value == null) return null;
		return Integer.parseInt(value.toString());
	}
	
	public Object getValue(String colName) {
		Integer index = nameIndexs.get(colName);
		if (index == null) return null;
		return values[index];
	}
	
	public String getAsString(String colName) {
		Object value = getValue(colName);
		if (value == null) return null;
		return value.toString();
	}
	
	public Integer getAsInt(String colName) {
		Object value = getValue(colName);
		if (value == null) return null;
		return Integer.parseInt(value.toString());
	}
	
	public void setValue(int index, Object value) {
		if (index < 0 || index >= values.length) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}
		values[index] = value;
	}
	
	public void setValue(String colName, Object value) {
		Integer index = nameIndexs.get(colName);
		if (index == null) {
			throw new IllegalArgumentException("colName: " + colName);
		}
		values[index] = value;
	}
	
	public int size() {
		return colNames.length;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size(); i++) {
			if (i > 0) sb.append(",");
			sb.append(colNames[i]);
			sb.append(":");
			sb.append(values[i]);
		}
		sb.append("]");
		return sb.toString();
	}
}
