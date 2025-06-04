package com.loxpression.util;

public class Node<T> {
	public String name;
	public T info;
	public int index;
	
	public Node(String name, int index) {
		this.name = name;
		this.index = index;
	}
}
