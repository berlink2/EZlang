package parser;

/**
 * Custom error class for parsing errors
 * @author Berlian K
 *
 */
public class ParserError extends RuntimeException{
	 
	/**
	 * 
	 * @param message error Message that will be relayed to user when error is made
	 */
	 public ParserError(String message) {
	    super(message);
	    
	    
	  }
	  
	 
}
