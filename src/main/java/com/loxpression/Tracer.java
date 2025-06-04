package com.loxpression;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import com.loxpression.util.StringUtils;

public class Tracer {
	static class Entry {
		long timeMillis;
		int id;
		public Entry(int id, long timeMills) {
			this.id = id;
			this.timeMillis = timeMills;
		}
	}
	
	private boolean enable;
	private Deque<Entry> stack = new LinkedList<>();
	private OutputStream traceStream;
	public Tracer() {
		this(System.out);
	}
	
	public Tracer(OutputStream traceStream) {
		this.enable = false;
		this.traceStream = traceStream;
		this.stack.push(new Entry(0, System.currentTimeMillis()));
	}
	
	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean isTrace) {
		this.enable = isTrace;
	}
	
	public void println(String message, Object...args) {
		message = "[trace] " + (message == null ? "" : message);
		printTrace(message);
	}
	
	public void startTimer() {
		startTimer(null);
	}

	public void startTimer(String message, Object...args) {
		if (!enable) return;
		Entry entry = newTimeEntry();
		stack.push(entry);
		String msg = makeBlank(entry.id - 1) + String.format("[trace%s]start ", entry.id);
		if (StringUtils.isNotEmpty(message)) {
			msg += String.format(message, args);
		}
		printTrace(msg);
	}
	
	public void endTimer(String message, Object...args) {
		if (!enable) return;
		Entry entry = stack.pop();
		long time = System.currentTimeMillis() - entry.timeMillis;
		String msg = makeBlank(entry.id - 1) + String.format("[trace%s]end:%sms ", entry.id, time);
		if (StringUtils.isNotEmpty(message)) {
			msg += String.format(message, args);
		}
		printTrace(msg);
		
		if (stack.isEmpty()) {
			stack.push(new Entry(0, System.currentTimeMillis()));
		}
	}
	
	private Entry newTimeEntry() {
		int lastId = this.stack.peek().id;
		return new Entry(lastId + 1, System.currentTimeMillis());
	}
	
	private void printTrace(String message) {
		try {
			this.traceStream.write((message + "\n").getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String makeBlank(int n) {
		if (n <= 0) return "";
		StringBuilder sb = new StringBuilder();
		while (n-- > 0) sb.append(" ");
		return sb.toString();
	}
}
