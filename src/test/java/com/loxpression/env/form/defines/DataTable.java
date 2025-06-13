package com.loxpression.env.form.defines;

import java.util.ArrayList;
import java.util.List;

public class DataTable {
	private List<DataRow> dataRows = new ArrayList<DataRow>();
	
	public DataTable() {
		
	}
	
	public void appendRow(DataRow row) {
		dataRows.add(row);
	}
	
	public DataRow getRow(int index) {
		if (index < 0 || index >= size()) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return dataRows.get(index);
	}
	
	public DataRow getDefaultRow() {
		if (dataRows.size() == 0) {
			return null;
		}
		return dataRows.get(0);
	}
	
	public int size() {
		return dataRows.size();
	}

	public boolean commitChanges() { 
		return true; // write change to database;
	}
}
