package com.loxpression.parser;

public enum TokenType {
	  // Single-character tokens.
	  LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
	  COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, PERCENT,

	  // One or two character tokens.
	  BANG, BANG_EQUAL,
	  EQUAL, EQUAL_EQUAL,
	  GREATER, GREATER_EQUAL,
	  LESS, LESS_EQUAL,
	  STAR, STARSTAR,
	  AND, OR,

	  // Literals.
	  IDENTIFIER, STRING, NUMBER,

	  // Keywords.
	  CLASS, ELSE, FALSE, FUN, FOR, IF, NULL,
	  PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

	  ERROR, EOF
}
