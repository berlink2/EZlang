
import java.util.List;

import ast.*;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) {
		
		String test ="a='c'+'c';print a;";
		//String test = "i=1;while(i<=10) {i=i+1;print i;}";
	//String test = "a=[1,2,3];a[0]=0;print a[0];";
	//	String test = "a=5;{a=3;{a=4;}print a;}{a=6;print a;}print a;";
		
//		String test = " a = \"global a\";\n" + 
//				" b = \"global b\";\n" + 
//				" c = \"global c\";\n" + 
//				"{\n" + 
//				"   a = \"outer a\";\n" + 
//				"   b = \"outer b\";\n" + 
//				"  {\n" + 
//				"     a = \"inner a\";\n" + 
//				
//				"    print a;\n" + 
//				"    print b;\n" + 
//				"    print c;\n" + 
//				"  }\n" + 
//				"  print a;\n" + 
//				"  print b;\n" + 
//				"  print c;\n" + 
//				"}\n" + 
//				"print a;\n" + 
//				"print b;\n" + 
//				"print c;";
	
		
		
		Lexer lexer = new Lexer(test);
		lexer.tokenStream();
		List<Token> tokenList = lexer.getTokenList();
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
