package com.loxpression.parser;

import java.io.Serializable;

import com.loxpression.values.Value;

public class Token implements Serializable {
	private static final long serialVersionUID = -8714620339708759254L;
	public final TokenType type;
	public final String lexeme;
	public final Value literal;
	public final int line;
	
	public Token(TokenType type, String lexme, Value literal, int line) {
		this.type = type;
		this.lexeme = lexme;
		this.literal = literal;
		this.line = line;
	}
	
	public String toString() {
		return type + " " + lexeme + " " + literal;
	}
}
