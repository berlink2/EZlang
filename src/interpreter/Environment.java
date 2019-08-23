package interpreter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;

import lexer.Token;

public class Environment {

	final LinkedList<HashMap<String, Object>> scopeList;

	// constructor for the global scope environment
	public Environment() {
		scopeList = new LinkedList<>();
		scopeList.push(new HashMap<String, Object>());

	}

	public void pushScope() {
		scopeList.push(new HashMap<String, Object>());
	}

	public void popScope() {
		scopeList.pop();
	}
	
	public void declare(Token name, Object value) {
		String varName = name.getLexeme();
		scopeList.peek().put(varName, value);
	}

	public void assign(Token name, Object value) {
		boolean alreadyExists = false;
		String varName = name.getLexeme();
		for (HashMap<String, Object> table : scopeList) {
			for (String var : table.keySet()) {
				if (var.equals(varName)) {
					table.put(varName, value);
					return;
					
					
					
				}
			}
		}
		
		
		
		

	}

	public Object get(Token name) {

		String varName = name.getLexeme();
		Object value = null;

		for (HashMap<String, Object> table : scopeList) {
			for (String var : table.keySet()) {
				if (var.equals(varName)) {
					value = table.get(var);
					return value;
				}
			}
		}

		return value;

	}

}
