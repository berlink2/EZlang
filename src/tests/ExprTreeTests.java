package tests;
import ast.*;
import lexer.*;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
//test class for expression ASTs

class ExprTreeTests {

	/**
	 * This test checks that a literal expression is made correctly
	 */
	@Test
	 void testLiteral() {
		Expr literal = new Expr.Literal(10);
		TreeMaker tm = new TreeMaker();
		String test = tm.make(literal);
		String compareString = "10";
		assertEquals(compareString, test, "tested strings should be the same");
		
	}
	
	@Test
	void testUnaryOp() {
		Token op = new Token(TokenType.MINUS, "-", null, 1); 
		Expr right = new Expr.Literal(10);
		Expr unary = new Expr.unaryOp(op, right);
		TreeMaker tm = new TreeMaker();
		String test = tm.make(unary);
		String compareString = "(-10)";
		assertEquals(compareString, test, "tested strings should be the same");
	}
	
	@Test
	void testBinaryOp() {
		Token op = new Token(TokenType.PLUS, "+", null, 1); 
		Expr left = new Expr.Literal(10);
		Expr right = new Expr.Literal(10);
		
		Expr binOp = new Expr.binOp(op, left, right);
		TreeMaker tm = new TreeMaker();
		String test = tm.make(binOp);
		String compareString = "(+ 10 10)";
		assertEquals(compareString, test, "tested strings should be the same");
	}
	
	@Test
	 void testGroup() {
		Expr node = new Expr.Literal(10);
		Expr group = new Expr.Group(node);
		TreeMaker tm = new TreeMaker();
		String test = tm.make(group);
		String compareString = "(group 10)";
		assertEquals(compareString, test, "tested strings should be the same");
		
	}

}
