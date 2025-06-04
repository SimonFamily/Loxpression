package com.loxpression.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeSet<T> {
	private Map<String, Node<T>> nodesMap;
	private List<Node<T>> nodes;
	private int cnt;
	
	public NodeSet() {
		nodesMap = new HashMap<>();
		nodes = new ArrayList<>();
	}
	
	public Node<T> addNode(String name) {
		Node<T> node = this.nodesMap.get(name);
		if (node == null) {
			node = new Node<>(name, this.cnt++);
			this.nodesMap.put(name, node);
			this.nodes.add(node);
		}
		return node;
	}
	
	public Node<T> getNode(String name) {
		return nodesMap.get(name);
	}
	
	public Node<T> getNode(int index) {
		validateIndex(index);
		return nodes.get(index);
	}
	
	
	private void validateIndex(int i) {
        if (i < 0 || i >= cnt)
            throw new IllegalArgumentException("index " + i + " is not between 0 and " + (cnt - 1));
    }
	
	public int size() {
		return cnt;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < cnt; i++) {
			Node<T> node = getNode(i);
			sb.append(String.format("%d: %s(%d)", i, node.name, node.index));
			sb.append("\n");
		}
		return sb.toString();
	}
}
