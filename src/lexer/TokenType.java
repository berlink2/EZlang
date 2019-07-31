package lexer;

public enum TokenType {
	// One character tokens.                      
		  LEFT_PARENTHESIS, RIGHT_PARENTHESIS
		  , LEFT_BRACKET, RIGHT_BRACKET, EXCLAMATION, EQUAL,
		  COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,
		  
	// Two character tokens.                  
		   EXCLAMATION_EQUAL,                                
		  EQUAL_EQUAL,                              
		  GREATER, GREATER_EQUAL,                          
		  LESS, LESS_EQUAL,  
	
	// Keywords
		  IF, THEN, ELSE, AND, OR,
		  
	// Literals.                                     
		   ID, STRING, NUMBER,                      	  
		  
	//End of file 
		  EOF
}
