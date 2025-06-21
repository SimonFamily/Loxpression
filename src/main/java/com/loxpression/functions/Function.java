package com.loxpression.functions;

import java.util.List;

import com.loxpression.values.Value;

public abstract class Function implements Callable {
	private String name;
	private String title;
	private String group;
	public Function(String name, String title, String group) {
		this.name = name;
		this.title = title;
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public String getGroup() {
		return group;
	}

	public abstract Value call(List<Value> arguments);
}
