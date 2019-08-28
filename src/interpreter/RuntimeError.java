package interpreter;

import lexer.Token;

public class RuntimeError extends RuntimeException{
	/**
	 * 
	 */
	private final Token token;
	
	/**
	 * @param token
	 * @param errorMessage
	 */
	public RuntimeError(Token token, String errorMessage) {
		super(errorMessage);
		this.token = token;
	}
}
