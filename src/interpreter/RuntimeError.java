package interpreter;

import lexer.Token;

public class RuntimeError extends RuntimeException{
	final Token token;
	
	public RuntimeError(Token token, String errorMessage) {
		super(errorMessage);
		this.token = token;
	}
}
