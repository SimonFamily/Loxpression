package com.loxpression.execution;

import java.util.HashSet;
import java.util.Set;

import com.loxpression.expr.AssignExpr;
import com.loxpression.expr.Expr;
import com.loxpression.expr.SetExpr;
import com.loxpression.visitors.VariableSet;
import com.loxpression.visitors.VarsQuery;

public class ExprInfo {
	private Set<String> precursors = new HashSet<String>(); // 依赖的变量 read
	private Set<String> successors = new HashSet<String>(); // 被赋值的变量 write
	private Expr expr;
	private String src;
	private int index;
	private boolean isAssign;

	public ExprInfo(Expr expr, String src, int index) {
		this.expr = expr;
		this.src = src;
		this.index = index;
		this.isAssign = expr instanceof AssignExpr || expr instanceof SetExpr;
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
		return isAssign;
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

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
