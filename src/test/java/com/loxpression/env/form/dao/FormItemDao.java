package com.loxpression.env.form.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.loxpression.env.form.Utils;
import com.loxpression.env.form.defines.DataRow;
import com.loxpression.env.form.defines.FormItem;

public class FormItemDao {
	public static List<FormItem> queryAllItems(String formId) throws IOException {
		List<DataRow> rows = Utils.readTableData("FormItem");
		if (rows == null || rows.size() == 0) return new ArrayList<FormItem>();
		List<FormItem> result = new ArrayList<FormItem>(rows.size());
		for (DataRow row : rows) {
			FormItem formItem = new FormItem();
			formItem.setId(row.getAsString("id"));
			formItem.setTitle(row.getAsString("title"));
			formItem.setFormId(row.getAsString("formId"));
			formItem.setFieldId(row.getAsString("fieldId"));
			result.add(formItem);
		}
		return result;
	}
}
