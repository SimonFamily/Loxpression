package com.loxpression.visitors;

import java.util.List;

import com.loxpression.Environment;
import com.loxpression.LoxRuntimeError;
import com.loxpression.expr.AssignExpr;
import com.loxpression.expr.BinaryExpr;
import com.loxpression.expr.CallExpr;
import com.loxpression.expr.Expr;
import com.loxpression.expr.IdExpr;
import com.loxpression.expr.IfExpr;
import com.loxpression.expr.LiteralExpr;
import com.loxpression.expr.LogicExpr;
import com.loxpression.expr.UnaryExpr;
import com.loxpression.functions.Function;
import com.loxpression.functions.FunctionManager;
import com.loxpression.parser.Token;
import com.loxpression.parser.TokenType;
import com.loxpression.values.Value;

public class Evaluator extends VisitorBase<Value> {
	public Evaluator() {
		super();
	}
	
	public Evaluator(Environment env) {
		super(env);
	}
	
	@Override
	public Value execute(Expr expr) {
		if (expr != null) return expr.accept(this);
		return new Value();
	}

	@Override
	public Value visit(BinaryExpr expr) {
		Value left = execute(expr.left);
		Value right = execute(expr.right);

		switch (expr.operator.type) {
		case PLUS:
			if (!left.isNumber() && !left.isString() || !right.isNumber() && !right.isString()) {
				throw new LoxRuntimeError(expr.operator, "Operands must be number or string.");
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
			checkNumberOperands(expr.operator, left, right);
			if (left.isDouble() || right.isDouble()) {
				return new Value(left.asDouble() - right.asDouble());
			} else {
				return new Value(left.asInteger() - right.asInteger());
			}
		case STAR:
			checkNumberOperands(expr.operator, left, right);
			if (left.isDouble() || right.isDouble()) {
				return new Value(left.asDouble() * right.asDouble());
			} else {
				return new Value(left.asInteger() * right.asInteger());
			}
		case SLASH:
			checkNumberOperands(expr.operator, left, right);
			if (left.isDouble() || right.isDouble()) {
				return new Value(left.asDouble() / right.asDouble());
			} else {
				return new Value(left.asInteger() / right.asInteger());
			}
		case PERCENT:
			checkNumberOperands(expr.operator, left, right);
			if (left.isDouble() || right.isDouble()) {
				return new Value(left.asDouble() % right.asDouble());
			} else {
				return new Value(left.asInteger() % right.asInteger());
			}
		case STARSTAR:
			checkNumberOperands(expr.operator, left, right);
			return new Value(Math.pow(left.asDouble(), right.asDouble()));
		case GREATER:
			checkNumberOperands(expr.operator, left, right);
			return new Value(left.asDouble() > right.asDouble());
		case GREATER_EQUAL:
			checkNumberOperands(expr.operator, left, right);
			return new Value(left.asDouble() >= right.asDouble());
		case LESS:
			checkNumberOperands(expr.operator, left, right);
			return new Value(left.asDouble() < right.asDouble());
		case LESS_EQUAL:
			checkNumberOperands(expr.operator, left, right);
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
	
	public Value visit(LogicExpr expr) {
		Value left = execute(expr.left);
		if (expr.operator.type == TokenType.OR) {
			if (left != null && left.isTruthy()) {
				return new Value(true);
			}
		} else {
			if (left == null || !left.isTruthy()) {
				return new Value(false);
			}
		}
		return execute(expr.right);
	}

	@Override
	public Value visit(LiteralExpr expr) {
		return expr.value;
	}

	@Override
	public Value visit(UnaryExpr expr) {
		Value right = execute(expr.right);
		switch (expr.operator.type) {
			case BANG:
				boolean truthy = right != null && right.isTruthy();
				return new Value(!truthy);
			case MINUS:
				checkNumberOperand(expr.operator, right);
				if (right.isInteger()) {
					return new Value(-right.asInteger());
				} else {
					return new Value(-right.asDouble());
				}
			default:
				return new Value();
		}
	}

	@Override
	public Value visit(IdExpr expr) {
		return env.getOrDefault(expr.id, new Value());
	}
	
	@Override
	public Value visit(AssignExpr expr) {
		Value right = execute(expr.right);
		IdExpr idExpr = (IdExpr)expr.left;
		this.env.put(idExpr.id, right);
		return right; 
	}
	
	@Override
	public Value visit(CallExpr expr) {
		Expr callee = expr.callee;
		IdExpr idExpr = (IdExpr) callee;
		String name = idExpr == null ? "" : idExpr.id;
		Function func = FunctionManager.getInstance().getFunction(name);
		if (func == null) {
			throw new LoxRuntimeError(expr.rParen, "Can only call function");
		}
		
//		if (expr.arguments.size() != func.arity()) {
//			throw new RuntimeError(expr.rParen,
//					"Expected " + func.arity() + " arguments but got " + expr.arguments.size() + ".");
//		}
		List<Value> values = execute(expr.arguments);
		return func.call(values);
	}
	
	@Override
	public Value visit(IfExpr expr) {
		Value condition = execute(expr.condition);
		if (condition.isTruthy()) {
			return execute(expr.thenBranch);
		} else if (expr.elseBranch != null) {
			return execute(expr.elseBranch);
		}
		return new Value();
	}
	
	private void checkNumberOperand(Token operator, Value operand) {
		if (operand != null && operand.isNumber()) {
			return;
		}
		throw new LoxRuntimeError(operator, "Operand must be a number.");
	}
	
	private void checkNumberOperands(Token operator, Value left, Value right) {
		if (left.isNumber() && right.isNumber()) {
			return;
		}
		throw new LoxRuntimeError(operator, String.format("Operands must be numbers.left:%s. right: %s" , left, right));
	}
}
