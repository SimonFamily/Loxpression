package com.loxpression.env.form.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.loxpression.env.form.Utils;
import com.loxpression.env.form.defines.DataRow;
import com.loxpression.env.form.defines.FieldDefine;

public class FieldDao {
	public static List<FieldDefine> queryAllFields(String formId) throws IOException {
		List<DataRow> rows = Utils.readTableData("FieldDefine");
		if (rows == null || rows.size() == 0) return new ArrayList<FieldDefine>();
		List<FieldDefine> result = new ArrayList<FieldDefine>(rows.size());
		for (DataRow row : rows) {
			FieldDefine field = new FieldDefine();
			field.setId(row.getAsString("id"));
			field.setName(row.getAsString("name"));
			field.setTableName(row.getAsString("tableName"));
			field.setTableId(row.getAsString("tableId"));
			result.add(field);
		}
		return result;
	}
}
