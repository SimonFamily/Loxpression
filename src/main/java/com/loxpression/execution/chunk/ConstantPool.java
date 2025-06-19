package com.loxpression.execution.chunk;

import java.util.ArrayList;
import java.util.Arrays;

import com.loxpression.values.Value;

public class ConstantPool {
	ArrayList<Value> constants;
	
	public ConstantPool(int capacity) {
		constants = new ArrayList<>(Math.max(capacity, 10));
	}
	
	public ConstantPool() {
		constants = new ArrayList<>();
	}
	
	public ConstantPool(Value[] values) {
		constants = new ArrayList<Value>(Arrays.asList(values));
	}
	
	public int addConst(Value value) {
		
		constants.add(value);
		return constants.size() - 1; // 返回索引
	}
	
	public Value readConst(int index) {
		return constants.get(index);
	}
	
	public Value[] toArray() {
		return constants.toArray(new Value[constants.size()]);
	}
	
	public void clear() {
		constants.clear();
	}
}