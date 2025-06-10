package com.loxpression.visitors;

import com.loxpression.expr.AssignExpr;
import com.loxpression.expr.BinaryExpr;
import com.loxpression.expr.CallExpr;
import com.loxpression.expr.GetExpr;
import com.loxpression.expr.IdExpr;
import com.loxpression.expr.IfExpr;
import com.loxpression.expr.LiteralExpr;
import com.loxpression.expr.LogicExpr;
import com.loxpression.expr.SetExpr;
import com.loxpression.expr.UnaryExpr;

public interface Visitor<R> {
	R visit(BinaryExpr expr);
	R visit(LogicExpr expr);
	R visit(LiteralExpr expr);
	R visit(UnaryExpr expr);
	R visit(IdExpr expr);
	R visit(AssignExpr expr);
	R visit(CallExpr expr);
	R visit(IfExpr expr);
	R visit(GetExpr expr);
	R visit(SetExpr expr);
}
