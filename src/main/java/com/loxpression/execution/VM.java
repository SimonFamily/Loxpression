package com.loxpression.execution;

import com.loxpression.env.Environment;
import com.loxpression.parser.TokenType;
import com.loxpression.values.Value;
import com.loxpression.values.ValuesHelper;

public class VM {
	private final static int STACK_MAX = 256;
	private static final ThreadLocal<VM> vmHolder = ThreadLocal.withInitial(VM::new);
	
	private Value[] stack;
	private int stackTop;
	private Chunk chunk;
	private int ip;

	private VM() {
		this.stack = new Value[STACK_MAX];
	}
	
	public static VM instance() {
		return vmHolder.get();
	}

	private void reset() {
		this.stackTop = 0;
		this.ip = 0;
		this.chunk = null;
	}

	private void push(Value value) {
		stack[stackTop++] = value;
	}

	private Value pop() {
		return stack[--stackTop];
	}

	private Value peek() {
		return this.stack[stackTop - 1];
	}

	private Value peek(int distance) {
		return this.stack[stackTop - 1 - distance];
	}

	public Value execute(Chunk chunk, Environment env) {
		reset();
		this.chunk = chunk;
		return run(env);
	}

	private Value run(Environment env) {
		OpCode op;
		for (;;) {
			byte code = readCode();
			op = OpCode.forValue(code);
			switch (op) {
			case OP_CONSTANT: {
				push(readConstant());
				break;
			}
			case OP_GET_GLOBAL: {
				String name = readString();
				Value v = env.getOrDefault(name, new Value());
				push(v);
				break;
			}
			case OP_SET_GLOBAL: {
				String name = readString();
				env.put(name, peek()); // 赋值语句也有返回值，不弹出结果
				break;
			}
			case OP_ADD:
				binaryOp(TokenType.PLUS);
				break;
			case OP_SUBTRACT:
				binaryOp(TokenType.MINUS);
				break;
			case OP_MULTIPLY:
				binaryOp(TokenType.STAR);
				break;
			case OP_DIVIDE:
				binaryOp(TokenType.SLASH);
				break;
			case OP_MODE:
				binaryOp(TokenType.PERCENT);
				break;
			case OP_POWER:
				binaryOp(TokenType.STARSTAR);
				break;
			case OP_GREATER:
				binaryOp(TokenType.GREATER);
				break;
			case OP_GREATER_EQUAL:
				binaryOp(TokenType.GREATER_EQUAL);
				break;
			case OP_LESS:
				binaryOp(TokenType.LESS);
				break;
			case OP_LESS_EQUAL:
				binaryOp(TokenType.LESS_EQUAL);
				break;
			case OP_EQUAL_EQUAL:
				binaryOp(TokenType.EQUAL_EQUAL);
				break;
			case OP_BANG_EQUAL:
				binaryOp(TokenType.BANG_EQUAL);
				break;
			case OP_NOT:
				preUnaryOp(TokenType.BANG);
				break;
			case OP_NEGATE:
				preUnaryOp(TokenType.MINUS);
				break;
			case OP_RETURN: {
				Value result = pop();
				return result;
			}
			default:
				break;
			}
		}
	}

	private void binaryOp(TokenType type) {
		Value b = pop();
		Value a = pop();
		Value result = ValuesHelper.binaryOperate(a, b, type);
		push(result);
	}
	
	private void preUnaryOp(TokenType type) {
		Value operand = pop();
		Value result = ValuesHelper.preUnaryOperate(operand, type);
		push(result);
	}

	private String readString() {
		Value value = readConstant();
		return value.asString();
	}

	private Value readConstant() {
		byte index = readCode();
		return currentChunk().readContant(index);
	}

	private byte readCode() {
		return (byte)currentChunk().readCode(ip++);
	}

	private Chunk currentChunk() {
		return this.chunk;
	}
}
