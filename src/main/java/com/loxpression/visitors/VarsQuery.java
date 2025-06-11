package com.loxpression.visitors;

import java.util.ArrayList;
import java.util.List;

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
import com.loxpression.values.Value;

public class VarsQuery extends VisitorBase<VariableSet> {
	
	public VarsQuery() {
		super(null);
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
		if (result == null) return rhs;
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
	
	@Override
	public VariableSet visit(GetExpr expr) {
		List<String> names = new ArrayList<String>();
		visitGet(expr, names);
		String id = String.join(".", names);
		return VariableSet.fromDepends(id);
	}
	
	private void visitGet(Expr expr, List<String> names) {
		if (expr instanceof IdExpr) {
			names.add(((IdExpr)expr).id);
			return;
		}
		GetExpr get = (GetExpr)expr;
		visitGet(get.object, names);
		names.add(get.name.lexeme);
	}
	
	@Override
	public VariableSet visit(SetExpr expr) {
		VariableSet gets = execute(new GetExpr(expr.object, expr.name));
		VariableSet result = new VariableSet();
		result.setAssigns(gets.getDepends());
		VariableSet rhs = execute(expr.value);
		result.comebine(rhs);
		return result;	
	}

}
