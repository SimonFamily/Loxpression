package com.loxpression;

import java.util.Map;

import com.loxpression.functions.ClazzMethod;

public class Clazz {
	public final String name;
	public final Clazz superClass;
	public final Map<String, ClazzMethod> methods;
	
	public Clazz(String name, Clazz superClass, Map<String, ClazzMethod> methods) {
		this.superClass = superClass;
		this.name = name;
		this.methods = methods;
	}
}
