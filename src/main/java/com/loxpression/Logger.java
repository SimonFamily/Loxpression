package com.loxpression;

import java.util.*;

public class Logger {
	private boolean isTrace;
	private Deque<Long> stack = new LinkedList<>();
	
	public Logger() {
		this.isTrace = false;
	}
	
	public boolean isTrace() {
		return isTrace;
	}

	public void setTrace(boolean isTrace) {
		this.isTrace = isTrace;
	}

	public void startTrace(String message, Object...args) {
		if (!isTrace) return;
		stack.push(System.currentTimeMillis());
		if (message != null && message.length() > 0) {
			System.out.println(String.format(message, args));			
		}
	}
	
	public void startTrace() {
		if (!isTrace) return;
		stack.push(System.currentTimeMillis());
	}
	
	public void endTrace(String message, Object...args) {
		if (!isTrace) return;
		long time = System.currentTimeMillis() - stack.pop();
		System.out.println(String.format(message, args) + " 用时：" + time);
	}
}
