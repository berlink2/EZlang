
import java.util.List;

import ast.*;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) {
		

		String test = "a ='a';b='b';c = a+b;print c;";
		
//		String a ="five";
//		if(true){
//			a = "tt";
//			System.out.println(a);
//		}
//		System.out.println(a);
//		
		
		
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
