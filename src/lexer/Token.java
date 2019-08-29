package lexer;

/**
 * Class for Token objects
 * @author Berlian K
 *
 */
public class Token {
	/**
	 * Attributes for each token
	 * 
	 */
	private final TokenType type;
	private final String lexeme;
	private final Object literal;
	private final int line;
	
	/**
	 * 
	 * @param type The type of Token
	 * @param lexeme The token's text in source code
	 * @param literal The token's value
	 * @param line The line number where the token appears
	 */
	public Token(TokenType type, String lexeme, Object literal, int line) {
		this.type = type;
		this.lexeme = lexeme;
		this.literal = literal;
		this.line = line;
	}
	
	
	/**
	 * Returns the type of a token
	 * @return A token's type
	 */
	public TokenType getType() {
		return type;
	}


	/**
	 * 	Returns the text of the token in the sourcecode
	 * @return A token's text 
	 */
	public String getLexeme() {
		return lexeme;
	}


	/**
	 *  Returns the value of a token
	 * @return The value of the token 
	 */
	public Object getLiteral() {
		return literal;
	}


	/**
	 * Returns the line where the token appears in sourcecode
	 * @return line number of the token
	 */
	public int getLine() {
		return line;
	}


	/**
	 * Returns string of a token's type and its lexeme
	 */
	public String toString() {
		return type + ": " + lexeme;
	}

}
