package lexer;

public enum TokenType {
	// Single-character tokens.                      
		  LEFT_PARENTHESIS, RIGHT_PARENTHESIS
		  , LEFT_BRACKET, RIGHT_BRACKET,
		  COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,
		  
	// One or two character tokens.                  
		  EXCLAMATION, EXCLAMATION_EQUAL,                                
		  EQUAL, EQUAL_EQUAL,                              
		  GREATER, GREATER_EQUAL,                          
		  LESS, LESS_EQUAL,  
		  
	// Literals.                                     
		   ID, STRING, NUMBER,                      	  
		  
	//End of file token
		  EOF
}
