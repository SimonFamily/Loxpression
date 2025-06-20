package com.loxpression.execution.chunk;

import java.nio.ByteBuffer;
import java.util.*;

import com.loxpression.CompileError;
import com.loxpression.Tracer;
import com.loxpression.values.Value;
import com.loxpression.values.ValueType;

public class ConstantPool {
	private ArrayList<Value> constants;
	private Map<Value, Integer> indexMap = new HashMap<Value, Integer>();
	private Tracer tracer;

	public ConstantPool(int capacity, Tracer tracer) {
		this.tracer = tracer;
		constants = new ArrayList<>(Math.max(capacity, 10));
	}

	public ConstantPool(Tracer tracer) {
		this.tracer = tracer;
		constants = new ArrayList<>();
	}

	public ConstantPool(byte[] bytes, Tracer tracer) {
		this.tracer = tracer;
		tracer.startTimer();
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		constants = new ArrayList<Value>();
		while (buffer.remaining() > 0) {
			Value value = Value.getFrom(buffer);
			constants.add(value);
		}
		tracer.endTimer("根据字节数组构造常量池。");
	}
	
	public byte[] toBytes() {
		tracer.startTimer();
		int n = constants.size();
		int min_bytes_per_value = 8;
		ByteBuffer buffer = ByteBuffer.allocate(n * 8); // 默认分配大小
		for (Value value : constants) {
			if (buffer.remaining() < min_bytes_per_value * 10) { // 动态扩容
				int newCapacity = buffer.capacity() * 2;
				ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
				buffer.flip();
				newBuffer.put(buffer);
				buffer = newBuffer;
			}
			
			value.writeTo(buffer);
		}
		buffer.flip();
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);
		tracer.endTimer("常量池生成字节数组。");
		return bytes;
	}
	
	public int addShareConst(Value value) {
		Integer index = indexMap.get(value);
		if (index != null) {
			return index;
		}
		index = addConst(value);
		indexMap.put(value, index);
		return index;
	}

	public int addConst(Value value) {
		checkType(value);
		constants.add(value);
		return constants.size() - 1; // 返回索引
	}

	public Value readConst(int index) {
		return constants.get(index);
	}

	public void clear() {
		constants.clear();
	}

	private void checkType(Value value) {
		ValueType type = value.getValueType();
		switch (type) {
		case Integer:
		case Long:
		case Float:
		case Double:
		case String:
		case Boolean:
			break;
		default:
			throw new CompileError(0, "常量池中暂不支持此类型：" + value.getValueType());
		}
	}
}