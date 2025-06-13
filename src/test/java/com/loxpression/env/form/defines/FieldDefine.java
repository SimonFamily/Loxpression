package com.loxpression.env.form.defines;

import java.io.Serializable;

public class FieldDefine implements Serializable { // 对应物理字段
	private static final long serialVersionUID = -7730838461654274699L;

	private String id;
	private String name; // 数据库物理字段名
	private String tableName;
	private String tableId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	
	public String getCode() {
		return tableName + "." + name;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s %s", id, name, tableName, tableId);
	}
}
