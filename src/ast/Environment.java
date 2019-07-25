package ast;
import java.util.HashMap;
import java.util.Map;
import lexer.Token;

public class Environment {
	private final Map<String, Object> table = new HashMap<>();
	
	public void define(String key, Object value) {
		table.put(key, value);
	}
	
	public void assign(Token name, Object value) {
		if(table.containsKey(name.getLexeme())) {
			table.put(name.getLexeme(), value);
			return;
		}
		
	}
	
	public Object get(Token name) {
		
			return table.get(name.getLexeme());
		
		
		       
		  }  
	}

