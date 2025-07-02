package com.loxpression.values;

import com.loxpression.LoxRuntimeError;
import com.loxpression.parser.TokenType;

public class ValuesHelper {
	
	public static Value binaryOperate(Value left, Value right, TokenType type) {
		switch (type) {
		case PLUS:
			if (!left.isNumber() && !left.isString() || !right.isNumber() && !right.isString()) {
				throw new LoxRuntimeError("Operands must be number or string.");
			}
			if (left.isString() || right.isString()) {
				return new Value(left.toString() + right.toString());
			} else {
				if (left.isDouble() || right.isDouble()) {
					return new Value(left.asDouble() + right.asDouble());
				} else {
					return new Value(left.asInteger() + right.asInteger());
				}
			}
		case MINUS:
			checkNumberOperands(left, right);
			if (left.isDouble() || right.isDouble()) {
				return new Value(left.asDouble() - right.asDouble());
			} else {
				return new Value(left.asInteger() - right.asInteger());
			}
		case STAR:
			checkNumberOperands(left, right);
			if (left.isDouble() || right.isDouble()) {
				return new Value(left.asDouble() * right.asDouble());
			} else {
				return new Value(left.asInteger() * right.asInteger());
			}
		case SLASH:
			checkNumberOperands(left, right);
			if (right.isInteger() && right.asInteger() == 0) {
                throw new LoxRuntimeError("Division by zero.");
            }
			if (left.isDouble() || right.isDouble()) {
				return new Value(left.asDouble() / right.asDouble());
			} else {
				return new Value(left.asInteger() / right.asInteger());
			}
		case PERCENT:
			checkNumberOperands(left, right);
			if (left.isDouble() || right.isDouble()) {
				return new Value(left.asDouble() % right.asDouble());
			} else {
				return new Value(left.asInteger() % right.asInteger());
			}
		case STARSTAR:
			checkNumberOperands(left, right);
			return new Value(Math.pow(left.asDouble(), right.asDouble()));
		case GREATER:
			checkNumberOperands(left, right);
			return new Value(left.asDouble() > right.asDouble());
		case GREATER_EQUAL:
			checkNumberOperands(left, right);
			return new Value(left.asDouble() >= right.asDouble());
		case LESS:
			checkNumberOperands(left, right);
			return new Value(left.asDouble() < right.asDouble());
		case LESS_EQUAL:
			checkNumberOperands(left, right);
			return new Value(left.asDouble() <= right.asDouble());
		case BANG_EQUAL:
			return new Value(!left.equals(right));
		case EQUAL_EQUAL:
			return new Value(left.equals(right));
		default:
			break;
		}
		return new Value();
	}
	
	public static Value preUnaryOperate(Value operand, TokenType type) {
		switch (type) {
			case BANG:
				boolean truthy = operand != null && operand.isTruthy();
				return new Value(!truthy);
			case MINUS:
				checkNumberOperand(operand);
				if (operand.isInteger()) {
					return new Value(-operand.asInteger());
				} else {
					return new Value(-operand.asDouble());
				}
			default:
				return new Value();
		}
	}
	
	private static void checkNumberOperand(Value operand) {
		if (operand != null && operand.isNumber()) {
			return;
		}
		throw new LoxRuntimeError("Operand must be a number.");
	}
	
	private static void checkNumberOperands(Value left, Value right) {
		if (left.isNumber() && right.isNumber()) {
			return;
		}
		throw new LoxRuntimeError(String.format("Operands must be numbers.left:%s. right: %s" , left, right));
	}
}
