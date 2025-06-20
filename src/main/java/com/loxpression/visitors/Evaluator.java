package com.loxpression.visitors;

import java.util.List;

import com.loxpression.LoxRuntimeError;
import com.loxpression.env.Environment;
import com.loxpression.expr.AssignExpr;
import com.loxpression.expr.BinaryExpr;
import com.loxpression.expr.CallExpr;
import com.loxpression.expr.Expr;
import com.loxpression.expr.GetExpr;
import com.loxpression.expr.IdExpr;
import com.loxpression.expr.IfExpr;
import com.loxpression.expr.LiteralExpr;
import com.loxpression.expr.LogicExpr;
import com.loxpression.expr.SetExpr;
import com.loxpression.expr.UnaryExpr;
import com.loxpression.functions.Function;
import com.loxpression.functions.FunctionManager;
import com.loxpression.parser.Token;
import com.loxpression.parser.TokenType;
import com.loxpression.values.Value;
import com.loxpression.values.ValuesHelper;

public class Evaluator extends VisitorBase<Value> {
	private Environment env;
	public Evaluator(Environment env) {
		this.env = env;
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
		Token operator = expr.operator;
		return ValuesHelper.binaryOperate(left, right, operator.type);
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
		Token operator = expr.operator;
		return ValuesHelper.preUnaryOperate(right, operator.type);
	}

	@Override
	public Value visit(IdExpr expr) {
		return getVariableValue(expr.id);
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
		
		if (expr.arguments.size() != func.arity()) {
			throw new LoxRuntimeError(expr.rParen,
					"Expected " + func.arity() + " arguments but got " + expr.arguments.size() + ".");
		}
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
	
	@Override
	public Value visit(GetExpr expr) {
		Value object = execute(expr.object);
		if (object.isInstance()) {
			return object.asInstance().get(expr.name.lexeme);
		}
		throw new LoxRuntimeError(expr.name, "Only instances have properties.");
	}
	
	@Override
	public Value visit(SetExpr expr) {
		Value object = execute(expr.object);
		if (!(object.isInstance())) {
			throw new LoxRuntimeError(expr.name, "Only instances have fields.");
		}

		Value value = execute(expr.value);
		object.asInstance().set(expr.name.lexeme, value);
		return value;
	}
	
	private Value getVariableValue(String id) {
		return env.getOrDefault(id, new Value());
	}
}
