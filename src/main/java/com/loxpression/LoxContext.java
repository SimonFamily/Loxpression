package com.loxpression;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.loxpression.execution.ExprInfo;
import com.loxpression.expr.Expr;
import com.loxpression.util.Digraph;
import com.loxpression.util.Node;
import com.loxpression.util.NodeSet;
import com.loxpression.visitors.VariableSet;
import com.loxpression.visitors.VarsQuery;

public class LoxContext {
	private Logger logger;
	private List<ExprInfo> exprInfos;
	private NodeSet<ExprInfo> nodeSet;
	private Digraph graph;

	public LoxContext() {
		logger = new Logger();
	}
	
	public Logger getLogger() {
		return logger;
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
	
	public void preExecute(List<Expr> exprs, List<String> srcs) {
		this.nodeSet = new NodeSet<>();
		exprs = exprs == null ? new ArrayList<Expr>() : exprs;
		this.initExprInfos(exprs, srcs);
		this.initNodes();
		this.initGraph();
	}
	
	private void initExprInfos(List<Expr> exprs, List<String> srcs) {
		getLogger().startTrace();
		this.exprInfos = new ArrayList<ExprInfo>(exprs.size());
		
		VarsQuery varQuery = new VarsQuery();
		for (int i = 0; i < exprs.size(); i++) {
			Expr expr = exprs.get(i);
			VariableSet varSet = varQuery.execute(expr);
			Set<String> precursors = varSet.getDepends();
			Set<String> successors = varSet.getAssigns();
			ExprInfo exprInfo = new ExprInfo(expr, precursors, successors);
			if (srcs != null && srcs.size() > 0) {
				exprInfo.src = srcs.get(i);
			}
			this.exprInfos.add(exprInfo);
		}
		getLogger().endTrace("完成表达式变量信息初始化。");
	}
	
	private void initNodes() {
		getLogger().startTrace();
		for (ExprInfo exprInfo : this.exprInfos) {
			for (String name : exprInfo.precursors) {
				nodeSet.addNode(name);
			}
			
			boolean flag = true;
			for (String name : exprInfo.successors) {
				Node<ExprInfo> node = nodeSet.addNode(name);
				if (flag) {
					node.info = exprInfo;
					flag = false;
				}
			}
		}
		getLogger().endTrace("完成图节点初始化。");
	}
	
	private void initGraph() {
		getLogger().startTrace();
		this.graph = new Digraph(nodeSet.size());
		for (ExprInfo info : exprInfos) {
			for (String prec : info.precursors) {
				Node<ExprInfo> preNode = nodeSet.getNode(prec);
				int u = preNode.index;
				for (String succ : info.successors) {
					Node<ExprInfo> succNode = nodeSet.getNode(succ);
					int v = succNode.index;
					this.graph.addEdge(u, v);
				}
			}
		}
		getLogger().endTrace("完成图的构造。");
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
