
import java.util.List;

import ast.*;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) {
		

		String test = "a = 5;\n" + 
				"b = 3;\n" + 
				"c = 2;\n" + 
				"d = a+b*c;\n" + 
				"print d;";
		

		
		
		Lexer lexer = new Lexer(test);
		List<Token> tokenList = lexer.tokenStream();
		Parser parser = new Parser(tokenList);
		List<Stmt> stmtList = parser.parse();
		TreeInterpreter interpreter = new TreeInterpreter();
		interpreter.execute(stmtList);
	
		for (Token token:tokenList) {
			System.out.println(token);
		}
		
		
	 
		
		

		    

	}

}
