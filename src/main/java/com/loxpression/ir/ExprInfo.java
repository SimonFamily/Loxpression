package com.loxpression.ir;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.loxpression.expr.AssignExpr;
import com.loxpression.expr.Expr;
import com.loxpression.expr.SetExpr;
import com.loxpression.visitors.VariableSet;
import com.loxpression.visitors.VarsQuery;

public class ExprInfo implements Serializable {
	private static final long serialVersionUID = 6879322173657186528L;
	private Set<String> precursors = new HashSet<String>(); // 依赖的变量 read
	private Set<String> successors = new HashSet<String>(); // 被赋值的变量 write
	private Expr expr;
	private int index;

	public ExprInfo(Expr expr, int index) {
		this.expr = expr;
		this.index = index;
		this.initVariables();
	}
	
	private void initVariables() {
		VarsQuery varQuery = new VarsQuery();
		VariableSet varSet = varQuery.execute(expr);
		if (varSet != null) {
			precursors = varSet.getDepends();
			successors = varSet.getAssigns();			
		}
	}
	
	public boolean isAssign() {
		return expr instanceof AssignExpr || expr instanceof SetExpr;
	}

	public Set<String> getPrecursors() {
		return precursors;
	}

	public void setPrecursors(Set<String> precursors) {
		this.precursors = precursors;
	}

	public Set<String> getSuccessors() {
		return successors;
	}

	public void setSuccessors(Set<String> successors) {
		this.successors = successors;
	}

	public Expr getExpr() {
		return expr;
	}

	public void setExpr(Expr expr) {
		this.expr = expr;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
