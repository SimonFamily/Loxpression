package com.loxpression.visitors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class VariableSet {
	private Set<String> assigns = new HashSet<String>();
	private Set<String> depends = new HashSet<String>();;
	
	public Set<String> getAssigns() {
		return assigns;
	}
	
	public void setAssigns(Set<String> assigns) {
		this.assigns = assigns;
	}
	
	public Set<String> getDepends() {
		return depends;
	}
	
	public void setDepends(Set<String> depends) {
		this.depends = depends;
	}
	
	public void addAssign(String name) {
		this.assigns.add(name);
	}
	
	public void addDepend(String name) {
		this.depends.add(name);
	}
	
	public void comebine(VariableSet other) {
		if (other == null) return;
		this.assigns.addAll(other.getAssigns());
		this.depends.addAll(other.getDepends());
	}
	
	public static VariableSet fromDepends(String...names) {
		VariableSet result = new VariableSet();
		for (String name : names) {
			result.addDepend(name);
		}
		return result;
	}
	
	public static VariableSet fromAssigns(String...names) {
		VariableSet result = new VariableSet();
		for (String name : names) {
			result.addAssign(name);
		}
		return result;
	}
	
	@Override
	public String toString() {
		String[] assignArr = assigns.toArray(new String[assigns.size()]);
		Arrays.sort(assignArr);
		String result = String.join(",", assignArr);
		if (result.length() != 0) {
			result += " = ";
		}
		
		String[] dependArr = depends.toArray(new String[depends.size()]);
		Arrays.sort(dependArr);
		result += String.join(",", dependArr);
		return result;
	}
	
}
