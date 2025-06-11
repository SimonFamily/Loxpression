package com.loxpression.execution;

import java.util.*;

import com.loxpression.LoxContext;
import com.loxpression.LoxRuntimeError;
import com.loxpression.expr.Expr;
import com.loxpression.util.Digraph;
import com.loxpression.util.Node;
import com.loxpression.util.NodeSet;
import com.loxpression.util.TopologicalSort;

public class ExprSorter {
	private NodeSet<ExprInfo> nodeSet;
	private Digraph graph;
	private LoxContext context;
	
	public ExprSorter(LoxContext context) {
		this.context = context;
		this.nodeSet = context.getExecContext().getNodeSet();
		this.graph = context.getExecContext().getGraph();
	}
	
	public List<ExprInfo> sort() {
		if (graph == null || graph.V() == 0) return null;
		
		context.getTracer().startTimer();
		TopologicalSort topSorter = new TopologicalSort(graph);
		
		if (!topSorter.sort()) {
			throw new LoxRuntimeError("公式列表存在循环引用！");
		}
		int[] nodeOrders = topSorter.getOrders();
		List<ExprInfo> result = new ArrayList<ExprInfo>();
		for (int nodeIndex : nodeOrders) {
			Node<ExprInfo> node = nodeSet.getNode(nodeIndex);
			if (node.info != null) {
				result.add(node.info);
			}
		}
		
		List<ExprInfo> origInfos = context.getExecContext().getExprInfos();
		for (ExprInfo expr : origInfos) {
			if (!expr.isAssign()) {
				result.add(expr);
			}
		}
		context.getTracer().endTimer("完成拓扑排序。");
		return result;
	}
	
	private void printCircle() {

	}
}