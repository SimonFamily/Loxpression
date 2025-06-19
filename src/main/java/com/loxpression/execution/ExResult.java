package com.loxpression.execution;

import com.loxpression.values.Value;

public class ExResult {
	private ExState state;
	private Value value;
	private int index;
	private String error;
	
	public ExResult(Value value, ExState state) {
		this.value = value;
		this.state = state;
	}

	public ExState getState() {
		return state;
	}

	public void setState(ExState state) {
		this.state = state;
	}

	public Value getResult() {
		return value;
	}

	public void setResult(Value value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}
