
import java.util.ArrayList;
import java.util.List;

import ast.*;
import ast.Stmt.Print;
import ast.Stmt.Var;
import lexer.*;
import parser.*;

public class Main {

	public static void main(String[] args) {
		
//		TreeEvaluator eval = new TreeEvaluator();
//
//		Token name = new Token(TokenType.PRINT, "print", null, 1);
//		Expr initial = new Expr.Literal(7);
//		Print print = new Print(initial);
//		
//		eval.evaluate(print);
//		X = 2
//
//	    Y = 3
//
//	    Z = x* (y*10)
//
//	   Print z
//		Token token1 = new Token(TokenType.ID, "x", null, 1);
//		Token token2 = new Token(TokenType.ID, "y", null, 1);
//		Token token3 = new Token(TokenType.ID, "z", null, 1);
//		Token mul = new Token(TokenType.STAR, "*", null, 1);
//		
//		// x=2
//		Var x = new Stmt.Var(token1, new Expr.Literal(2));
//		//y=3
//		Var y = new Stmt.Var(token2, new Expr.Literal(3));
//		
//		//x*(y*10)
//		Expr binop1 = new Expr.binOp(mul, x.getInitial(), new Expr.binOp(mul, y.getInitial(), new Expr.Literal(10)));
//		//z=x*(y*10)
//		Var z = new Stmt.Var(token3, binop1);
//		Print print = new Print(z.getInitial());
//		TreeInterpreter eval = new TreeInterpreter();
//		List<Stmt> stmts = new ArrayList<>();
//		stmts.add(print);
		
		//print z
		//eval.evaluate(stmts);
		
		String test = "x=2;y=3;z=x*y;print z;";
		
		Lexer lexer = new Lexer(test);
		List<Token> tokenList = lexer.tokenStream();
		Parser parser = new Parser(tokenList);
		parser.parse();
		List<Stmt> stmtList = parser.getStatementList();
		TreeInterpreter interpreter = new TreeInterpreter();
		interpreter.evaluate(stmtList);
	
//		for (Token token:tokenList) {
//			System.out.println(token);
//		}
		
		
	 
		
		

		    

	}

}
