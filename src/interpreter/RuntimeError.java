package interpreter;

import lexer.Token;

/**
 * Custom Error class for runtime errors in EZlang programs
 * @author Berlian K
 *
 */
public class RuntimeError extends RuntimeException{
	/**
	 *  Error causing Token
	 */
	private final Token token;
	
	/**
	 * @param token Error causing token
	 * @param errorMessage Message that will be relayed to user when error is made
	 */
	public RuntimeError(Token token, String errorMessage) {
		super(errorMessage);
		this.token = token;
	}
}
