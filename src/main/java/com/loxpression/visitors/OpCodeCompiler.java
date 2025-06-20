package com.loxpression.visitors;

import static com.loxpression.execution.OpCode.*;

import com.loxpression.LoxRuntimeError;
import com.loxpression.Tracer;
import com.loxpression.execution.OpCode;
import com.loxpression.execution.chunk.Chunk;
import com.loxpression.execution.chunk.ChunkWriter;
import com.loxpression.expr.*;
import com.loxpression.functions.Function;
import com.loxpression.functions.FunctionManager;
import com.loxpression.parser.Token;
import com.loxpression.values.Value;

// 将语法树编译为字节码
public class OpCodeCompiler implements Visitor<Void> {
	private ChunkWriter chunkWriter;
	private Tracer tracer;

	public OpCodeCompiler(int chunkCapacity, Tracer tracer) {
		this.chunkWriter = new ChunkWriter(chunkCapacity, tracer);
		this.tracer = tracer;
	}
	
	public OpCodeCompiler(Tracer tracer) {
		this.chunkWriter = new ChunkWriter(tracer);
		this.tracer = tracer;
	}
	
	public void beginCompile() {
		chunkWriter.clear();
	}
	
	public void compile(Expr expr, int order) {
		emitOp(OP_BEGIN, order);
		execute(expr);
		emitOp(OP_END); // 表达式都有返回值
	}
	
	public Chunk endCompile() {
		emitOp(OP_EXIT);
		return this.chunkWriter.flush();
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
		int constant = makeConstant(new Value(expr.id));
		emitOp(OP_GET_GLOBAL, constant); // 带一个常量参数的字节码
		return null;
	}

	@Override
	public Void visit(AssignExpr expr) {
		execute(expr.right);
		IdExpr idExpr = (IdExpr) expr.left;
		int constant = makeConstant(new Value(idExpr.id));
		emitOp(OP_SET_GLOBAL, constant); // 带一个常量参数的字节码
		return null;
	}

	@Override
	public Void visit(CallExpr expr) {
		Expr callee = expr.callee;
		IdExpr idExpr = (IdExpr) callee;
		String name = idExpr == null ? "" : idExpr.id;
		Function func = FunctionManager.getInstance().getFunction(name);
		if (func == null) {
			throw new LoxRuntimeError(expr.rParen, "Function undefine:" + name);
		}
		
		if (expr.arguments.size() != func.arity()) {
			throw new LoxRuntimeError(expr.rParen,
					"Expected " + func.arity() + " arguments but got " + expr.arguments.size() + ".");
		}
		for (Expr arg : expr.arguments) {
			execute(arg);
		}
		int constant = makeConstant(new Value(name));
		emitOp(OP_CALL, constant); // 带一个常量参数的字节码
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
	
	private void emitOp(OpCode opCode) {
		emitByte(opCode.getValue());
	}
	
	private void emitOp(OpCode opCode, byte arg) {
		emitOp(opCode);
		emitByte(arg);
	}

	private void emitOp(OpCode opCode, int arg) {
		emitOp(opCode);
		emitInt(arg);
	}

	private void emitConstant(Value value) {
		int index = makeConstant(value);
		emitOp(OP_CONSTANT, index);
	}

	private int makeConstant(Value value) {
		int index = chunkWriter.addContant(value);
		return index;
	}
	
	private void emitInt(int value) {
		this.chunkWriter.writeInt(value);
	}

	private void emitByte(byte code) {
		this.chunkWriter.writeByte(code);
	}
}
