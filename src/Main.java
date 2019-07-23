import ast.*;
import lexer.Token;
import lexer.TokenType;

public class Main {

	public static void main(String[] args) {
		Expr left = new Expr.unaryOp(new Token(TokenType.MINUS, "-", null, 1),new Expr.Literal(50.0));
		Expr right = new Expr.Literal(66);
		Token op = new Token(TokenType.PLUS, "+", null, 1);
		Expr tree = new Expr.binOp(op, left, right);
		TreeMaker tm = new TreeMaker();
		TreeEvaluator te = new TreeEvaluator();
		
		String exampleTree = tm.make(tree);
		te.evaluate(tree);
		
		System.out.println(exampleTree);
		

		    

	}

}
