package com.loxpression.parser;

import static com.loxpression.parser.TokenType.*;

import java.util.*;

import com.loxpression.values.Value;

public class Scanner {
	private static final Map<String, TokenType> keywords;
	static {
		keywords = new HashMap<>();
		keywords.put("class", CLASS);
		keywords.put("else", ELSE);
		keywords.put("false", FALSE);
		keywords.put("for", FOR);
		keywords.put("fun", FUN);
		keywords.put("if", IF);
		keywords.put("null", NULL);
		keywords.put("print", PRINT);
		keywords.put("return", RETURN);
		keywords.put("super", SUPER);
		keywords.put("this", THIS);
		keywords.put("true", TRUE);
		keywords.put("var", VAR);
		keywords.put("while", WHILE);
	}
	private final String source;
	private final List<Token> tokens = new ArrayList<>();

	private int start;
	private int current;
	private int line = 1;

	public Scanner(String source) {
		this.source = source;
	}

	public List<Token> scanTokens() {
		while (!isEnd()) {
			start = current;
			scanToken();
		}
		tokens.add(new Token(EOF, "", null, line));
		return tokens;
	}

	private void scanToken() {
		char c = advance();
		switch (c) {
		// 单符号
		case '(':
			addToken(LEFT_PAREN);
			break;
		case ')':
			addToken(RIGHT_PAREN);
			break;
		case '{':
			addToken(LEFT_BRACE);
			break;
		case '}':
			addToken(RIGHT_BRACE);
			break;
		case ',':
			addToken(COMMA);
			break;
		case '.':
			addToken(DOT);
			break;
		case '-':
			addToken(MINUS);
			break;
		case '+':
			addToken(PLUS);
			break;
		case '*':
			addToken(match('*') ? STARSTAR : STAR);
			break;
		case ';':
			addToken(SEMICOLON);
			break;
		case '%':
			addToken(PERCENT);
			break;
		// 两符号
		case '!':
			addToken(match('=') ? BANG_EQUAL : BANG);
			break;
		case '=':
			addToken(match('=') ? EQUAL_EQUAL : EQUAL);
			break;
		case '>':
			addToken(match('=') ? GREATER_EQUAL : GREATER);
			break;
		case '<':
			addToken(match('=') ? LESS_EQUAL : LESS);
			break;
		// 斜线
		case '/':
			if (match('/')) { // 注释
				while (peek() != '\n' && !isEnd())
					advance();
			} else {
				addToken(SLASH);
			}
			break;
		case '|':
			if (match('|')) addToken(OR);
			else throw new LoxParseError(line, "unkown character:" + c);
			break;
		case '&':
			if (match('&')) addToken(AND);
			else throw new LoxParseError(line, "unkown character:" + c);
			break;
		case ' ':
		case '\t':
		case '\r':
			break;
		case '\n':
			line++;
			break;
		case '"':
			string();
			break;
		default:
			if (isDigit(c)) {
				number();
			} else if (isAlpha(c)) {
				identity();
			} else {
				throw new LoxParseError(line, "unkown character:" + c);
			}
			break;
		}
	}

	private void string() {
		while (peek() != '"' && !isEnd()) {
			if (peek() == '\n')
				line++;
			advance();
		}
		if (isEnd()) {
			throw new LoxParseError(line, "Unterminated string.");
		}

		advance();
		String str = source.substring(start + 1, current - 1);
		addToken(STRING, new Value(str));
	}

	private void number() {
		while (isDigit(peek()))
			advance();
		boolean isDouble = false;
		if (peek() == '.' && isDigit(peekNext())) {
			isDouble = true;
			advance();
			while (isDigit(peek()))
				advance();
		}
		Value v;
		if (isDouble) {
			v = new Value(Double.parseDouble(source.substring(start, current)));
		} else {
			v = new Value(Integer.parseInt(source.substring(start, current)));
		}
		addToken(NUMBER, v);
	}

	private void identity() {
		while (isAlphaNumeric(peek()))
			advance();
		String text = source.substring(start, current);
		TokenType type = keywords.get(text);
		if (type == null)
			type = IDENTIFIER;
		addToken(type);
	}

	private boolean isEnd() {
		return current == source.length();
	}

	private char advance() {
		return source.charAt(current++);
	}

	private boolean match(char c) {
		if (isEnd())
			return false;
		if (source.charAt(current) != c)
			return false;

		current++;
		return true;
	}

	private char peek() {
		if (isEnd())
			return '\0';
		return source.charAt(current);
	}

	private char peekNext() {
		if (current + 1 >= source.length())
			return '\0';
		return source.charAt(current + 1);
	}

	private void addToken(TokenType type) {
		addToken(type, null);
	}

	private void addToken(TokenType type, Value literal) {
		String text = source.substring(start, current);
		tokens.add(new Token(type, text, literal, line));
	}

	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	private boolean isAlphaNumeric(char c) {
		return isDigit(c) || isAlpha(c);
	}
}
