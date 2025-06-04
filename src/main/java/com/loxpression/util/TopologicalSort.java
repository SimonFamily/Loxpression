package com.loxpression.util;

import java.util.*;

public class TopologicalSort {
	private Digraph g;
	private int[] indegree;
	private int[] order;

	public TopologicalSort(Digraph g) {
		this.g = g;
		this.indegree = new int[g.V()];
		this.order = new int[g.V()];
	}

	public boolean sort() {
		for (int v = 0; v < g.V(); v++) {
			indegree[v] = g.indegree(v);
		}

		Deque<Integer> queue = new LinkedList<Integer>();
		for (int v = 0; v < g.V(); v++)
			if (indegree[v] == 0)
				queue.offerLast(v);

		int count = 0;
		while (!queue.isEmpty()) {
			int u = queue.pollFirst();
			order[count++] = u;
			for (int v : g.adj(u)) {
				indegree[v]--;
				if (indegree[v] == 0)
					queue.offerLast(v);
			}
		}

		if (count != g.V()) {
			order = null;
			return false;
		}
		return true;
	}

	public boolean hasOrder() {
		return order != null;
	}

	public int[] getOrders() {
		return order;
	}
	
	@Override
	public String toString() {
		if (order == null) return "null";
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < g.V(); i++) sb.append(order[i]).append(",");
		sb.append("]");
		return sb.toString();
	}
}
