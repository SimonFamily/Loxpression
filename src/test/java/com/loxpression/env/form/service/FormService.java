package com.loxpression.env.form.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.loxpression.env.form.dao.FieldDao;
import com.loxpression.env.form.dao.FormItemDao;
import com.loxpression.env.form.defines.FieldDefine;
import com.loxpression.env.form.defines.FormItem;

public class FormService {
	private Map<String, FormItem> formItemTitleMap = new HashMap<String, FormItem>();
	private Map<String, FieldDefine> fieldIdMap = new HashMap<String, FieldDefine>();
	private Map<String, FieldDefine> tableKeyMap = new HashMap<String, FieldDefine>();
	
	public FormService(String formId) {
		try {
			initDefines(formId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initDefines(String formId) throws IOException {
		List<FieldDefine> fields = FieldDao.queryAllFields(formId);
		for (FieldDefine field : fields) {
			fieldIdMap.put(field.getId(), field);
			if ("id".equals(field.getName())) {
				tableKeyMap.put(field.getTableName(), field);
			}
		}
		List<FormItem> formItems = FormItemDao.queryAllItems(formId);
		for (FormItem item : formItems) {
			formItemTitleMap.put(item.getTitle(), item);
		}
	}
	
	public FormItem getFormItemByTitle(String title) {
		return formItemTitleMap.get(title);
	}

	public FieldDefine getFieldById(String id) {
		return fieldIdMap.get(id);
	}
	
	public FieldDefine getTableKeyField(String tableName) {
		return tableKeyMap.get(tableName);
	}
}
