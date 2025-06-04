package com.loxpression;

import com.loxpression.parser.Token;
import com.loxpression.parser.TokenType;

public class LoxException extends RuntimeException {

	private static final long serialVersionUID = -7770118470967252817L;

	public LoxException(int line, String where, String message) {
		super(errMsg(line, where, message));
	}

	public LoxException(int line, String message) {
		super(errMsg(line, "", message));
	}

	public LoxException(Token token, String message) {
		super(errMsg(token, message));
	}
	
	private static String errMsg(Token token, String message) {
		if (token.type == TokenType.EOF) {
			return errMsg(token.line, " at end ", message);
		} else {
			return errMsg(token.line, " at '" + token.lexeme + "'", message);
		}
	}

	private static String errMsg(int line, String where, String message) {
		return "[line " + line + "] Error" + where + ": " + message;
	}
}
