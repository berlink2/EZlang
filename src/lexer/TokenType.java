package lexer;

public enum TokenType {
	// One character tokens.                      
		  LEFT_PARENTHESIS, RIGHT_PARENTHESIS
		  , LEFT_BRACKET, RIGHT_BRACKET,  
		  COMMA, DOT,  SEMICOLON, 
	
	//Arithmetic
		  MINUS, PLUS, SLASH, STAR, MODULO,
		  
	// Comparisons                  
		  EQUAL, EXCLAMATION_EQUAL,                                
		  EQUAL_EQUAL,                              
		  GREATER, GREATER_EQUAL,                          
		  LESS, LESS_EQUAL,  
		  
	//Boolean 
		  EXCLAMATION, AND, OR,
		  
	// Reserved Keywords
		  IF, THEN, ELSE, PRINT, NULL, TRUE, FALSE, WHILE, 
		  
	// Literals.                                     
		   ID, STRING, NUMBER,                      	  
		  
	//End of file 
		  EOF
}
