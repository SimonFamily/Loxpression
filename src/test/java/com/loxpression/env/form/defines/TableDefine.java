package com.loxpression.env.form.defines;

import java.io.Serializable;

public class TableDefine implements Serializable { // 对应物理表
	private static final long serialVersionUID = 2388740694755613175L;

	private String id;
	private String name; // 数据库物理表名
	private String title;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", id, name, title);
	}
}
