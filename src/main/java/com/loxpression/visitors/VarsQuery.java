package com.loxpression.visitors;

import com.loxpression.expr.AssignExpr;
import com.loxpression.expr.BinaryExpr;
import com.loxpression.expr.CallExpr;
import com.loxpression.expr.Expr;
import com.loxpression.expr.IdExpr;
import com.loxpression.expr.IfExpr;
import com.loxpression.expr.LiteralExpr;
import com.loxpression.expr.LogicExpr;
import com.loxpression.expr.UnaryExpr;

public class VarsQuery extends VisitorBase<VariableSet> {
	
	public VarsQuery() {
		super();
	}
	
	@Override
	public VariableSet execute(Expr expr) {
		if (expr != null) return expr.accept(this);
		return null;
	}

	@Override
	public VariableSet visit(BinaryExpr expr) {
		VariableSet result = execute(expr.left);
		VariableSet rhs = execute(expr.right);
		if (result == null) return rhs;
		result.comebine(rhs);
		return result;
	}

	@Override
	public VariableSet visit(LogicExpr expr) {
		VariableSet result = execute(expr.left);
		VariableSet rhs = execute(expr.right);
		result.comebine(rhs);
		return result;
	}

	@Override
	public VariableSet visit(LiteralExpr expr) {
		return null;
	}

	@Override
	public VariableSet visit(UnaryExpr expr) {
		return execute(expr.right);
	}

	@Override
	public VariableSet visit(IdExpr expr) {
		return VariableSet.fromDepends(expr.id);
	}

	@Override
	public VariableSet visit(AssignExpr expr) {
		IdExpr idExpr = (IdExpr)expr.left;
		VariableSet result = VariableSet.fromAssigns(idExpr.id);
		VariableSet rhs = execute(expr.right);
		result.comebine(rhs);
		return result;
	}

	@Override
	public VariableSet visit(CallExpr expr) {
		VariableSet result = new VariableSet();
		for(Expr arg : expr.arguments) {
			VariableSet cur = execute(arg);
			result.comebine(cur);
		}
		return result;
	}
	
	@Override
	public VariableSet visit(IfExpr expr) {
		VariableSet result = new VariableSet();
		result.comebine(execute(expr.condition));
		result.comebine(execute(expr.thenBranch));
		result.comebine(execute(expr.elseBranch));
		return result;
	}

}
