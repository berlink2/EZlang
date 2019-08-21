
import java.util.List;

import ast.*;
import interpreter.TreeInterpreter;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) {
//	String test = "i=0;while(i<100) { i=i+1;a = \"\"; \n" + 
//			"			if(i%3==0) { a=a + \"Fizz\";}\n" + 
//			"			if(i%5==0) { a=a + \"Buzz\";}\n" + 
//			"			if(a == \"\") {a =i;}\n" + 
//			"					print a; }";
		
	//String test = "a = 5;print a;";
		
		
	//String test = " a=\"6\";b='6';a=a+b; print a; ";
		String test = "/*x=4;\n */x=7;print x";
	//String test ="make a=[1,2,3,4,5];print a[1];";
//String test = " i=0;while(i<10){i=i+1;print i;}";
		//String test = "print a=5;";
		//String test = "array = [1,2,3,4,5];if(true){array=array append array[4];print array;}";
//		String test = "array = [1,2,3,4,5];\n" + 
//				"x=read;\n" + 
//				"i=0;\n" + 
//				"found = false;\n" + 
//				"\n" + 
//				"while(i<5) {\n" + 
//				"if (array[i]==x) {\n" + 
//				"found = true;array[i]=\"Was here\"\n" + 
//				"\n" + 
//				"} \n" + 
//				"i=i+1;\n" + 
//				"\n" + 
//				"}\n" + 
//				"if (found == true) {\n" + 
//				"print \"Found!\";print array;\n" + 
//				"} else {\n" + 
//				"array=array append x;\n" + 
//				"print array;}";

		
		
		
		
		
		
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
		lexer.LexicalAnalysis();
		List<Token> tokenList = lexer.getTokenList();
		Parser parser = new Parser(tokenList);
		parser.parse();
		List<Stmt> stmtList = parser.getStatementList();
		 final TreeInterpreter interpreter = new TreeInterpreter();
		interpreter.execute(stmtList);
//	for (Stmt stmt:stmtList) {
//		System.out.println(stmt);
//	}
//	for (Token token:tokenList) {
//		System.out.println(token);
//	}	
		
		
		
	 
		
		

		    

	}

}
