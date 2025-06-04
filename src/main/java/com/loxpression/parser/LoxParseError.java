package com.loxpression.parser;

import com.loxpression.LoxException;

public class LoxParseError extends LoxException {
	private static final long serialVersionUID = -2818162301485005963L;

	public LoxParseError(int line, String message) {
		super(line, message);
	}
	
	public LoxParseError(Token token, String message) {
		super(token, message);
	}
}
