
import ast.*;
import ast.Stmt.Print;
import ast.Stmt.Var;
import lexer.*;

public class Main {

	public static void main(String[] args) {
//		Expr left = new Expr.unaryOp(new Token(TokenType.MINUS, "-", null, 1),new Expr.Literal(50.0));
//		Expr right = new Expr.Literal(66);
//		Token op = new Token(TokenType.PLUS, "+", null, 1);
//		Expr tree = new Expr.binOp(op, left, right);
		
//		
//		String exampleTree = tm.make(tree);
//		Object result = te.evaluate(tree);
//		
//		
//		
//		System.out.println(exampleTree);
//		TreeMaker tm = new TreeMaker();
//		TreeEvaluator te = new TreeEvaluator();
//		Token name = new Token(TokenType.ID, "a", null, 1);
//		Token name2 = new Token(TokenType.ID, "b", null, 1);
//		Token op = new Token(TokenType.PLUS, "+", null, 1);
//		Expr expr = new Expr.Assign(name, new Expr.Literal(5));
//		Expr expr2 = new Expr.Assign(name2, new Expr.Literal(6));
//		Expr expr3 = new Expr.binOp(op, expr, expr2);
//		
//		Stmt stmt = new Stmt.Expression(expr3);
//		Object result = te.evaluate(expr3);
//		String tree = tm.make(stmt);
//		
//		System.out.println(result);
		
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
		Token token1 = new Token(TokenType.ID, "x", null, 1);
		Token token2 = new Token(TokenType.ID, "y", null, 1);
		Token token3 = new Token(TokenType.ID, "z", null, 1);
		Token mul = new Token(TokenType.STAR, "*", null, 1);
		
		// x=2
		Var x = new Stmt.Var(token1, new Expr.Literal(2));
		//y=3
		Var y = new Stmt.Var(token2, new Expr.Literal(3));
		
		//x*(y*10)
		Expr binop1 = new Expr.binOp(mul, x.getInitial(), new Expr.binOp(mul, y.getInitial(), new Expr.Literal(10)));
		//z=x*(y*10)
		Var z = new Stmt.Var(token3, binop1);
		Print print = new Print(z.getInitial());
		TreeEvaluator eval = new TreeEvaluator();
		
		//print z
		eval.evaluate(print);
		
	 
		
		

		    

	}

}
