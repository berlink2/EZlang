package parser;

import lexer.Token;

public class ParserError extends RuntimeException{
	 private final Token token;

	 public ParserError(Token token, String message) {
	    super(message);
	    this.token = token;
	    
	  }
	  
	 
}
