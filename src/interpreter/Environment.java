package interpreter;

import java.util.HashMap;
import java.util.LinkedList;


import lexer.Token;

public class Environment {

	/**
	 * 
	 */
	final LinkedList<HashMap<String, Object>> scopeList;

	// constructor for the global scope environment
	/**
	 * 
	 */
	public Environment() {
		scopeList = new LinkedList<>();
		scopeList.push(new HashMap<String, Object>());

	}

	/**
	 * 
	 */
	public void pushScope() {
		scopeList.push(new HashMap<String, Object>());
	}

	/**
	 * 
	 */
	public void popScope() {
		scopeList.pop();
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public void declare(Token name, Object value) {
		String varName = name.getLexeme();
		scopeList.peek().put(varName, value);
	}

	/**
	 * @param name
	 * @param value
	 */
	public void assign(Token name, Object value) {
		String varName = name.getLexeme();
		for (HashMap<String, Object> table : scopeList) {
			for (String var : table.keySet()) {
				if (var.equals(varName)) {
					table.put(varName, value);
					return;
					
					
					
				}
			}
		}
		
		throw new RuntimeError(name, "Variable " +name.getLexeme()+ " on line " +name.getLine()+  " has not been declared yet. Please declare it first.");
		
		
		
		

	}

	/**
	 * @param name
	 * @return
	 */
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
		
		throw new RuntimeError(name, "Variable " +name.getLexeme()+ " on line " +name.getLine()+  " has not been declared yet. Please declare it first.");

	}

}
