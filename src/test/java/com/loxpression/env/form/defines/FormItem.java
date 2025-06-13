package com.loxpression.env.form.defines;

import java.io.Serializable;

public class FormItem implements Serializable {
	private static final long serialVersionUID = 9031543669807930066L;
	
	private String id;
	private String title;
	private String formId;
	private String fieldId; // 绑定到到物理字段

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s %s", id, title, formId, fieldId);
	}

}
