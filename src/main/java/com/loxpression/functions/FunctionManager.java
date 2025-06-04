package com.loxpression.functions;

import java.util.HashMap;
import java.util.Map;

import com.loxpression.functions.num.Abs;
import com.loxpression.functions.sys.Clock;

public class FunctionManager {
	private static final FunctionManager instance = new FunctionManager();
	private final Map<String, Function> functions = new HashMap<>();
	private FunctionManager() {
		registFunction(new Clock());
		registFunction(new Abs());
	}
	
	public static FunctionManager getInstance() {
		return instance;
	}
	
	public Function getFunction(String name) {
		return functions.get(name);
	}
	
	public void registFunction(Function func) {
		String name = func.getName();
		functions.put(name, func);
	}
	
	public void removeFunction(String name) {
		functions.remove(name);
	}
}
