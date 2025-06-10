package com.loxpression.parser;

import static com.loxpression.parser.TokenType.*;
import java.util.*;

import com.loxpression.expr.Expr;
import com.loxpression.parser.parselet.InfixParselet;
import com.loxpression.parser.parselet.PrefixParselet;
import com.loxpression.parser.parselet.impl.AssignParselet;
import com.loxpression.parser.parselet.impl.BinaryParselet;
import com.loxpression.parser.parselet.impl.CallParselet;
import com.loxpression.parser.parselet.impl.GetParselet;
import com.loxpression.parser.parselet.impl.GroupParselet;
import com.loxpression.parser.parselet.impl.IdParselet;
import com.loxpression.parser.parselet.impl.IfParselet;
import com.loxpression.parser.parselet.impl.LiteralParselet;
import com.loxpression.parser.parselet.impl.LogicParselet;
import com.loxpression.parser.parselet.impl.PreUnaryParselet;

public class Parser {
	private static final Map<TokenType, PrefixParselet> prefixParselets = new HashMap<>();
	private static final Map<TokenType, InfixParselet> infixParselets = new HashMap<>();
	static {
		prefixParselets.put(NUMBER, new LiteralParselet());
		prefixParselets.put(STRING, new LiteralParselet());
		prefixParselets.put(IDENTIFIER, new IdParselet());
		prefixParselets.put(LEFT_PAREN, new GroupParselet());
		prefixParselets.put(MINUS, new PreUnaryParselet(Precedence.PREC_UNARY));
		prefixParselets.put(BANG, new PreUnaryParselet(Precedence.PREC_UNARY));
		prefixParselets.put(IF, new IfParselet());
		
		infixParselets.put(PLUS, new BinaryParselet(Precedence.PREC_TERM));
		infixParselets.put(MINUS, new BinaryParselet(Precedence.PREC_TERM));
		infixParselets.put(PERCENT, new BinaryParselet(Precedence.PREC_MODE));
		infixParselets.put(STAR, new BinaryParselet(Precedence.PREC_FACTOR));
		infixParselets.put(SLASH, new BinaryParselet(Precedence.PREC_FACTOR));
		infixParselets.put(STARSTAR, new BinaryParselet(Precedence.PREC_POWER, true));
		infixParselets.put(EQUAL, new AssignParselet(Precedence.PREC_ASSIGNMENT));
		infixParselets.put(OR, new LogicParselet(Precedence.PREC_OR));
		infixParselets.put(AND, new LogicParselet(Precedence.PREC_AND));
		infixParselets.put(EQUAL_EQUAL, new BinaryParselet(Precedence.PREC_EQUALITY));
		infixParselets.put(BANG_EQUAL, new BinaryParselet(Precedence.PREC_EQUALITY));
		infixParselets.put(LESS, new BinaryParselet(Precedence.PREC_COMPARISON));
		infixParselets.put(LESS_EQUAL, new BinaryParselet(Precedence.PREC_COMPARISON));
		infixParselets.put(GREATER, new BinaryParselet(Precedence.PREC_COMPARISON));
		infixParselets.put(GREATER_EQUAL, new BinaryParselet(Precedence.PREC_COMPARISON));
		infixParselets.put(LEFT_PAREN, new CallParselet(Precedence.PREC_CALL));
		infixParselets.put(DOT, new GetParselet(Precedence.PREC_CALL));
	}
	
	private List<Token> tokens;
	private int current = 0;
	
	public Parser(String source) {
		Scanner scanner = new Scanner(source);
		this.tokens = scanner.scanTokens();
	}
	
	/**
	 * 解析表达式
	 * @return
	 */
	public Expr expression() {
		return expressionPrec(Precedence.PREC_NONE);
	}
	
	/**
	 * 解析操作符优先级大于min_prec的子表达式
	 * @param min_prec 优先级下限（不包含）
	 * @return
	 */
	public Expr expressionPrec(int min_prec) {
		Token token = advance();
		PrefixParselet prefixParselet = prefixParselets.get(token.type);
		if (prefixParselet == null) {
			throw new LoxParseError(token, "unkown token: " + token);
		}
		Expr lhs = prefixParselet.parse(this, token);
		while (peek().type != EOF) {
			Token next = peek();
			InfixParselet infixParselet = infixParselets.get(next.type);
			int precedence = infixParselet == null ? 0 : infixParselet.getPrecedence();
			if (precedence <= min_prec) {
				break;
			}
			token = advance();
			lhs = infixParselet.parse(this, lhs, token);
		}
		
		return lhs;
	}
	
	public boolean match(TokenType... types) {
		// 任意匹配即满足
		for (TokenType type : types) {
			if (check(type)) {
				advance();
				return true;
			}
		}
		return false;
	}
	
	public Token consume(TokenType expected) {
		return consume(expected, "Expected token " + expected + " and found " + peek().type);
	}

	public Token consume(TokenType type, String message) {
		if (check(type))
			return advance();
		throw new LoxParseError(peek(), message);
	}

	public Token advance() {
		if (!isAtEnd()) {
			current++;
		}
		return previous();
	}

	public boolean check(TokenType type) {
		return peek().type == type;
	}

	public Token peek() {
		return tokens.get(current);
	}

	public Token previous() {
		return tokens.get(current - 1);
	}
	
	private boolean isAtEnd() {
		return peek().type == EOF;
	}
}
