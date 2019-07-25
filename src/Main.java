
import ast.*;
import lexer.Token;
import lexer.TokenType;

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
		TreeMaker tm = new TreeMaker();
		TreeEvaluator te = new TreeEvaluator();
		Token name = new Token(TokenType.ID, "a", null, 1);
		Token name2 = new Token(TokenType.ID, "b", null, 1);
		Token op = new Token(TokenType.PLUS, "+", null, 1);
		Expr expr = new Expr.Assign(name, new Expr.Literal(5));
		Expr expr2 = new Expr.Assign(name2, new Expr.Literal(6));
		Expr expr3 = new Expr.binOp(op, expr, expr2);
		Stmt stmt = new Stmt.Expression(expr3);
		Object result = te.evaluate(expr3);
		String tree = tm.make(stmt);
		
		System.out.println(result);
		
		
		

		    

	}

}
