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
		this.nodeSet = context.getNodeSet();
		this.graph = context.getGraph();
	}
	
	public List<Expr> sort() {
		context.getTracer().startTimer();
		TopologicalSort topSorter = new TopologicalSort(graph);
		if (!topSorter.sort()) {
			throw new LoxRuntimeError("公式列表存在循环引用！");
		}
		int[] nodeOrders = topSorter.getOrders();
		List<Expr> result = new ArrayList<Expr>();
		for (int nodeIndex : nodeOrders) {
			Node<ExprInfo> node = nodeSet.getNode(nodeIndex);
			if (node.info != null) {
				result.add(node.info.expr);
			}
		}
		context.getTracer().endTimer("完成拓扑排序。");
		return result;
	}
	
	public List<ExprInfo> sortX() {
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
		context.getTracer().endTimer("完成拓扑排序。");
		return result;
	}
	
	private void printCircle() {

	}
}