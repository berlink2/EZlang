package lexer;

/**
 * Enum class for the various types of tokens
 * @author Berlian K
 *
 */
public enum TokenType {
	// One character tokens.                      
		  LEFT_PARENTHESIS, RIGHT_PARENTHESIS
		  , LEFT_CURLY_BRACKET, RIGHT_CURLY_BRACKET,  
		  LEFT_SQUARE_BRACKET, RIGHT_SQUARE_BRACKET,
		  COMMA, DOT,  SEMICOLON, 
	
	//Arithmetic
		  MINUS, PLUS, SLASH, STAR, PERCENT,
		  
	// Comparisons                  
		  EQUAL, EXCLAMATION_EQUAL,                                
		  EQUAL_EQUAL,                              
		  GREATER, GREATER_EQUAL,                          
		  LESS, LESS_EQUAL, 
		  
	//Boolean 
		  EXCLAMATION, AND, OR,
		  
	// Reserved Keywords
		  IF, THEN, ELSE, PRINT, NULL, TRUE, FALSE, WHILE, READ, APPEND, SHRINK,
		  REPEAT, MAKE,
		  
	// Literals.                                     
		  ID, STRING, INTEGER, FLOAT, CHAR,                   	  
		  
	//End of file 
		  EOF
}
