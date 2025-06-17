package com.loxpression.visitors;

import static com.loxpression.execution.OpCode.*;

import com.loxpression.execution.Chunk;
import com.loxpression.execution.ChunkMaker;
import com.loxpression.execution.OpCode;
import com.loxpression.expr.AssignExpr;
import com.loxpression.expr.*;
import com.loxpression.parser.Token;
import com.loxpression.values.Value;

public class OpCodeCompiler implements Visitor<Void> {
	private ChunkMaker chunkMaker;

	public OpCodeCompiler() {
	}
	
	public Chunk compile(Expr expr) {
		this.chunkMaker = new ChunkMaker();
		execute(expr);
		emitOp(OP_RETURN); // 表达式都有返回值
		return this.chunkMaker.flush();
	}

	private void execute(Expr expr) {
		if (expr != null) {
			expr.accept(this);
		}
	}

	@Override
	public Void visit(BinaryExpr expr) {
		execute(expr.left);
		execute(expr.right);
		Token operator = expr.operator;
		switch (operator.type) {
		case PLUS:
			emitOp(OP_ADD);
			break;
		case MINUS:
			emitOp(OP_SUBTRACT);
			break;
		case STAR:
			emitOp(OP_MULTIPLY);
			break;
		case SLASH:
			emitOp(OP_DIVIDE);
			break;
		case PERCENT:
			emitOp(OP_MODE);
			break;
		case STARSTAR:
			emitOp(OP_POWER);
			break;
		case GREATER:
			emitOp(OP_GREATER);
			break;
		case GREATER_EQUAL:
			emitOp(OP_GREATER_EQUAL);
			break;
		case LESS:
			emitOp(OP_LESS);
			break;
		case LESS_EQUAL:
			emitOp(OP_LESS_EQUAL);
			break;
		case BANG_EQUAL:
			emitOp(OP_BANG_EQUAL);
			break;
		case EQUAL_EQUAL:
			emitOp(OP_EQUAL_EQUAL);
			break;
		default:
			break;
		}
		return null;
	}

	@Override
	public Void visit(LogicExpr expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(LiteralExpr expr) {
		emitConstant(expr.value);
		return null;
	}

	@Override
	public Void visit(UnaryExpr expr) {
		execute(expr.right);
		Token operator = expr.operator;
		switch (operator.type) {
		case BANG:
			emitOp(OP_NOT);
		case MINUS:
			emitOp(OP_NEGATE);
		default:
			break;
		}
		return null;
	}

	@Override
	public Void visit(IdExpr expr) {
		byte constant = makeConstant(new Value(expr.id));
		emitOp(OP_GET_GLOBAL, constant); // 带一个参数的字节码
		return null;
	}

	@Override
	public Void visit(AssignExpr expr) {
		execute(expr.right);
		IdExpr idExpr = (IdExpr) expr.left;
		byte constant = makeConstant(new Value(idExpr.id));
		emitOp(OP_SET_GLOBAL, constant); // 带一个参数的字节码
		return null;
	}

	@Override
	public Void visit(CallExpr expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IfExpr expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(GetExpr expr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(SetExpr expr) {
		// TODO Auto-generated method stub
		return null;
	}

	private void emitConstant(Value value) {
		emitBytes(OP_CONSTANT.getValue(), makeConstant(value));
	}

	private void emitOp(OpCode opCode, byte arg) {
		emitOp(opCode);
		emitByte(arg);
	}

	private void emitOp(OpCode opCode) {
		emitByte(opCode.getValue());
	}

	private void emitBytes(byte code1, byte code2) {
		emitByte(code1);
		emitByte(code2);
	}

	private byte makeConstant(Value value) {
		byte constant = chunkMaker.addContant(value);
		return constant;
	}

	private void emitByte(byte code) {
		this.chunkMaker.writeCode(code);
	}

}
