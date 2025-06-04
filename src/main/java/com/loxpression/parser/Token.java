package com.loxpression.parser;

import com.loxpression.values.Value;

public class Token {
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
