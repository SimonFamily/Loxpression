package com.loxpression.execution;

import java.util.List;

import com.loxpression.LoxContext;
import com.loxpression.util.Digraph;
import com.loxpression.util.Node;
import com.loxpression.util.NodeSet;

public class ExecuteContext {
	private List<ExprInfo> exprInfos;
	private NodeSet<ExprInfo> nodeSet; // 有向图的变量节点
	private Digraph graph; // 所有公式内变量构成的有向图
	private LoxContext global;
	
	public ExecuteContext(LoxContext global) {
		this.global = global;
	}

	public List<ExprInfo> getExprInfos() {
		return this.exprInfos;
	}
	
	public NodeSet<ExprInfo> getNodeSet() {
		return this.nodeSet;
	}
	
	public Digraph getGraph() {
		return this.graph;
	}
	
	public boolean hasAssign() {
		return this.graph.V() > 0;
	}
	
	public void preExecute(List<ExprInfo> exprInfos) {
		this.nodeSet = new NodeSet<>();
		this.exprInfos = exprInfos;
		this.initNodes();
		this.initGraph();
	}
	
	private void initNodes() {
		global.getTracer().startTimer();
		for (ExprInfo exprInfo : this.exprInfos) {
			if (!exprInfo.isAssign()) { //只对赋值表达式构造有向图
				continue;
			}
			for (String name : exprInfo.getPrecursors()) {
				nodeSet.addNode(name);
			}
			
			boolean flag = true;
			for (String name : exprInfo.getSuccessors()) {
				Node<ExprInfo> node = nodeSet.addNode(name);
				if (flag) {
					node.info = exprInfo;
					flag = false;
				}
			}
		}
		global.getTracer().endTimer("完成图节点初始化。");
	}
	
	private void initGraph() {
		global.getTracer().startTimer();
		this.graph = new Digraph(nodeSet.size());
		if (nodeSet.size() == 0) {
			return;
		}
		for (ExprInfo info : exprInfos) {
			for (String prec : info.getPrecursors()) {
				Node<ExprInfo> preNode = nodeSet.getNode(prec);
				int u = preNode.index;
				for (String succ : info.getSuccessors()) {
					Node<ExprInfo> succNode = nodeSet.getNode(succ);
					int v = succNode.index;
					this.graph.addEdge(u, v);
				}
			}
		}
		global.getTracer().endTimer("完成图的构造。");
	}
	
	public void printGraph(StringBuilder s) {
        s.append(graph.V() + " vertices, " + graph.E() + " edges \n");
        for (int u = 0; u < graph.V(); u++) {
        	Node<ExprInfo> pre = nodeSet.getNode(u);
            s.append(String.format("%d(%s-%d): ", u, pre.name, graph.indegree(u)));
            for (int v : graph.adj(u)) {
            	Node<ExprInfo> succ = nodeSet.getNode(v);
                s.append(String.format("%d(%s) ", v, succ.name));
            }
            s.append("\n");
        }
	}
}
