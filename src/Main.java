
import java.util.ArrayList;
import java.util.List;

import ast.*;
import ast.Stmt.Print;
import ast.Stmt.Var;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) {
		

		String test = "x=5;if( false ){print x;} else {print 6;}";
		


		
		Lexer lexer = new Lexer(test);
		List<Token> tokenList = lexer.tokenStream();
		Parser parser = new Parser(tokenList);
		List<Stmt> stmtList = parser.parse();
		TreeInterpreter interpreter = new TreeInterpreter();
		interpreter.execute(stmtList);
	
//		for (Token token:tokenList) {
//			System.out.println(token);
//		}
		
		
	 
		
		

		    

	}

}
