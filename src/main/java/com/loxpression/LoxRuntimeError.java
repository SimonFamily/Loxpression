package com.loxpression;

import com.loxpression.parser.Token;

public class LoxRuntimeError extends RuntimeException {
	private static final long serialVersionUID = -4712708787382556974L;
	final Token token;
	public LoxRuntimeError(Token token, String message) {
		super(message);
		this.token = token;
	}
	
	public LoxRuntimeError(String message) {
		super(message);
		this.token = null;
	}
}
