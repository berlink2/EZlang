package ast;

import java.util.HashMap;
import java.util.Map;
import lexer.Token;

public class Environment {
	final Map<String, Object> table = new HashMap<>();
	final Environment enclosingScope;

	// constructor for the global scope environment
	public Environment() {
		enclosingScope = null;
	}

	// this constructor creates a new environment and is passed the previous
	// environment which encloses the new one
	public Environment(Environment enclosingScope) {
		this.enclosingScope = enclosingScope;
	}

	public void declare(String key, Object value) {
		table.put(key, value);
	}

	public void assign(Token name, Object value) {
		if (table.containsKey(name.getLexeme())) {
			table.put(name.getLexeme(), value);
			return;
		}
		if (enclosingScope != null) {
			enclosingScope.assign(name, value);
			return;
		}

	}
	

	public Object get(Token name) {
		Object value = null;
		String variable = name.getLexeme();
		if (table.containsKey(name.getLexeme())) {
			value = table.get(variable);
			return value;
		}
		

		if (enclosingScope != null) {
			return enclosingScope.get(name);
		}
		return value;

	}

}
