
import java.util.List;

import ast.*;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) {
		

		String test = "a= read;b=8;c=a+b;print c;";
	
		
		
		Lexer lexer = new Lexer(test);
		List<Token> tokenList = lexer.tokenStream();
		Parser parser = new Parser(tokenList);
		parser.parse();
		List<Stmt> stmtList = parser.getStatementList();
		TreeInterpreter interpreter = new TreeInterpreter();
		interpreter.execute(stmtList);
//	for (Stmt stmt:stmtList) {
//		System.out.println(stmt);
//	}
//	for (Token token:tokenList) {
//		System.out.println(token);
//	}	
		
		
		
	 
		
		

		    

	}

}
