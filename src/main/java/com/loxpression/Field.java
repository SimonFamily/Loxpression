package com.loxpression;

import java.util.ArrayList;
import java.util.List;

import com.loxpression.util.StringUtils;

public class Field {
	private final String name;
	private final Field owner; // 可以为null
	private String src;
	
	public Field(String name) {
		this.name = name;
		this.owner = null;
	}
	
	public Field(String name, Field owner) {
		this.name = name;
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public Field getOwner() {
		return owner;
	}
	
	public static Field valueOf(String src) {
		String[] names = src.split("\\.");
		Field cur = null;
		for (String name : names) {
			cur = new Field(name, cur);
		}
		return cur;
	}
	
	private void search(Field field, List<String> path) {
		if (field == null) {
			return;
		}
		search(field.owner, path);
		path.add(field.name);
	}
	
	@Override
	public String toString() {
		if (StringUtils.isEmpty(this.src)) {
			List<String> path = new ArrayList<String>();
			search(this, path);
			this.src = String.join(".", path);
		}
		return this.src;
	}
}
