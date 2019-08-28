
import java.util.List;

import ast.*;
import interpreter.TreeInterpreter;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) {
	String test = "make a=5<10 and 6<10;print a;";
		
//String test = "print \" \" + 5;";
		
		
	//String test = " make a=\"6\";make b='6';a=a+b; print a; ";
	
			
  //String test = " make i=0;while(i<10){print i=i+1;}";
		
		//String test = "make array = [1,2,3,4,5];if(true){array=array append array[4];print array;}";
//		String test = "make array = [1,2,3,4,5];\n" + 
//				"make x=read;\n" + 
//				"make i=0;\n" + 
//				"make found = false;\n" + 
//				"\n" + 
//				"while(i<5) {\n" + 
//				"if (array[i] == x) {\n" + 
//				"found = true;array[i]=\"Was here.\";" + 
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

		
		
		
		
		
		
//		String test = "make a = \"global a\";\n" + 
//				"make b = \"global b\";\n" + 
//				"make c = \"global c\";\n" + 
//				"{\n" + 
//				"  make a = \"outer a\";\n" + 
//				"  make b = \"outer b\";\n" + 
//				"  {\n" + 
//				"   make  a = \"inner a\";\n" + 
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


