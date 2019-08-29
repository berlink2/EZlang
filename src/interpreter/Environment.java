package interpreter;

import java.util.HashMap;
import java.util.LinkedList;


import lexer.Token;

/**
 * This class is a custom data structure for handling variables and scope
 * @author Berlian K
 *
 */
public class Environment {

	/**
	 * List of scopes for a EZlang program. Each HashMap is a scope
	 */
	final LinkedList<HashMap<String, Object>> scopeList;

	
	/**
	 *  Constructor that creates the global scope for a EZlang program
	 */
	public Environment() {
		scopeList = new LinkedList<>();
		scopeList.push(new HashMap<String, Object>());

	}

	/**
	 * When called adds a scope to a program
	 */
	public void pushScope() {
		scopeList.push(new HashMap<String, Object>());
	}

	/**
	 * When called removes a scope from a program
	 */
	public void popScope() {
		scopeList.pop();
	}
	
	/**
	 * Method for declaring variables and initializing them
	 * @param name The name of the variable
	 * @param value The value of the variable
	 */
	public void declare(Token name, Object value) {
		String varName = name.getLexeme();
		scopeList.peek().put(varName, value);
	}

	/**
	 * Method for assigning value to a variable. Checks most local scope
	 * first and moves to enclosing scope if variable not found in local scope, and
	 * keeps looking. If no variable found throws error.
	 * @param name The name of the variable
	 * @param value The value assigned to the variable
	 * @throws RuntimeError if variable has not been declared yet
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
	 * Method for retrieving value of a variable. Checks most local scope
	 * first and moves to enclosing scope if variable not found in local scope, and
	 * keeps looking. If no variable found throws error.
	 * @param name The name of the variable
	 * @return The value that is assigned to a variable
	 * @throws RuntimeError if variable has not been declared yet
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
