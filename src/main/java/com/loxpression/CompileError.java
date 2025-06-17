package com.loxpression;

import com.loxpression.parser.Token;

public class CompileError extends LoxException {
	private static final long serialVersionUID = -2067834034028816111L;

	public CompileError(int line, String message) {
		super(line, message);
	}
	
	public CompileError(Token token, String message) {
		super(token, message);
	}
}
