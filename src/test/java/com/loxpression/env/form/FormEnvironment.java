package com.loxpression.env.form;

import java.io.IOException;
import java.util.*;

import com.loxpression.env.Environment;
import com.loxpression.env.form.defines.DataRow;
import com.loxpression.env.form.defines.DataTable;
import com.loxpression.env.form.defines.FieldDefine;
import com.loxpression.env.form.defines.FormItem;
import com.loxpression.env.form.service.DataQuery;
import com.loxpression.env.form.service.FormService;
import com.loxpression.ir.ExecuteContext;
import com.loxpression.ir.ExprInfo;
import com.loxpression.values.Value;

public class FormEnvironment extends Environment {
	private FormService formService;
	private Map<String, Integer> varIndex;
	private DataTable dataTable;
	
	public FormEnvironment(String formId) {
		formService = new FormService(formId);
		varIndex = new HashMap<String, Integer>();
	}
	
	@Override
	public boolean beforeExecute(ExecuteContext context) {
		List<ExprInfo> exprInfos = context.getExprInfos();
		Set<String> variables = new HashSet<>(); // 所有变量
		for (ExprInfo info : exprInfos) {
			variables.addAll(info.getPrecursors()); // read variable
			variables.addAll(info.getSuccessors()); // write variable
		}
		// 开始求值前，查询出所有变量的值并放在dataTable中，后续运行表达式时直接从dataTable取值。
		DataQuery dataQuery = new DataQuery("id", "1");
		for (String variable : variables) {
			FormItem item = formService.getFormItemByTitle(variable); // 表达式中的变量用的是表单项的标题
			if (item == null) {
				throw new RuntimeException("未定义的变量：" + variable);
			}
			FieldDefine field = formService.getFieldById(item.getFieldId());
			int index = dataQuery.addColumn(field);
			varIndex.put(variable, index); // 变量对应的列索引
		}
		try {
			dataTable = dataQuery.executeQuery();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dataTable != null && dataTable.size() > 0;
	}

	@Override
	public Value get(String id) {
		return getOrDefault(id, new Value());
	}

	@Override
	public Value getOrDefault(String id, Value defValue) {
		int index = varIndex.get(id);
		DataRow row = dataTable.getDefaultRow();
		Integer v = row.getAsInt(index);
		if (v == null) return defValue;
		return new Value(v);
	}

	@Override
	public void put(String id, Value value) {
		int index = varIndex.get(id);
		DataRow row = dataTable.getDefaultRow();
		row.setValue(index, value.asInteger());
	}

	@Override
	public int size() {
		return varIndex.size();
	}

}
