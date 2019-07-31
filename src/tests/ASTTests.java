package tests;

import ast.*;
import ast.Expr.*;
import lexer.*;
import ast.Stmt.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

//test class for expression ASTs

class ASTTests {

	// The following tests check that expression ASTs are instantiated
	// correctly

	/**
	 * This test checks that a literal expression is made correctly
	 */
	@Test
	void testLiteral() {
		Expr literal = new Expr.Literal(10);
		TreeMaker tm = new TreeMaker();
		String tree = tm.make(literal);
		String test = "10";
		assertEquals(test, tree, "tested strings should be the same");

	}

	/**
	 * this test checks that a single unary expression is made correctly
	 * 
	 */
	@Test
	void testUnaryOp() {
		Token op = new Token(TokenType.MINUS, "-", null, 1);
		Expr right = new Expr.Literal(10);
		Expr unary = new Expr.unaryOp(op, right);
		TreeMaker tm = new TreeMaker();
		String tree = tm.make(unary);
		String test = "(-10)";
		assertEquals(test, tree, "tested strings should be the same");
	}

	/**
	 * this test checks that a plus binary
	 */
	@Test
	void testBinaryOp() {
		Token op = new Token(TokenType.PLUS, "+", null, 1);
		Expr left = new Expr.Literal(10);
		Expr right = new Expr.Literal(10);

		Expr binOp = new Expr.binOp(op, left, right);
		TreeMaker tm = new TreeMaker();
		String tree = tm.make(binOp);
		String test = "(+ 10 10)";
		assertEquals(test, tree, "tested strings should be the same");
	}

	/**
	 * this test checks that a expression ast containing a binary op containing a
	 * unary op is made correctly
	 */
	@Test
	void testUnaryAndBinaryOp() {
		Token op = new Token(TokenType.SLASH, "/", null, 1);
		Expr left = new Expr.Literal(10);
		Expr right = new Expr.unaryOp(new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(15.67));

		Expr binOp = new Expr.binOp(op, left, right);
		TreeMaker tm = new TreeMaker();
		String tree = tm.make(binOp);
		String test = "(/ 10 (-15.67))";
		assertEquals(test, tree, "tested strings should be the same");
	}

	/**
	 * this test checks that a grouping expression is made correctly
	 */
	@Test
	void testGroup() {
		Expr node = new Expr.Literal(10);
		Expr group = new Expr.Group(node);
		TreeMaker tm = new TreeMaker();
		String tree = tm.make(group);
		String test = "(group 10)";
		assertEquals(test, tree, "tested strings should be the same");

	}

	// The following tests test whether the expression ASTs evaluate correctly

	/**
	 * This test checks that literals are evaluated correctly. Test checks for ints,
	 * doubles, strings,
	 */
	@Test
	void testLiteralEval() {
		TreeEvaluator eval = new TreeEvaluator();

		// tests if 55 = 55
		Expr intLit = new Expr.Literal(55);
		Object intEval = eval.evaluate(intLit);
		int testInt = 55;

		// tests if 55.55 = 55.55
		Expr doubleLit = new Expr.Literal(55.55);
		Object doubleEval = eval.evaluate(doubleLit);
		double testDoub = 55.55;

		// tests if "test" = "test"
		Expr stringLit = new Expr.Literal("test");
		Object stringEval = eval.evaluate(stringLit);
		String testString = "test";

		assertEquals(testInt, intEval, "should be the same");
		assertEquals(testDoub, doubleEval, "should be the same");
		assertEquals(testString, stringEval, "should be the same");

	}

	// This unit test tests if unary expressions are evaluated correctly
	@Test
	void testUnaryEval() {
		TreeEvaluator eval = new TreeEvaluator();

		// tests if -34.6 = -34.6
		Expr neg = new Expr.unaryOp(new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(34.6));
		Object negEval = eval.evaluate(neg);
		double testDoub = -34.6;

		// tests if !true = false;
		Expr notTrue = new Expr.unaryOp(new Token(TokenType.EXCLAMATION, "!", null, 1), new Expr.Literal(true));
		Object notTrueEval = eval.evaluate(notTrue);
		boolean notTrueTest = false;

		// tests if !false = true;
		Expr notFalse = new Expr.unaryOp(new Token(TokenType.EXCLAMATION, "!", null, 1), new Expr.Literal(false));
		Object notFalseEval = eval.evaluate(notFalse);
		boolean notFalseTest = true;

		assertEquals(testDoub, negEval, "should be the same");
		assertEquals(notTrueTest, notTrueEval, "should be the same");
		assertEquals(notFalseTest, notFalseEval, "should be the same");
	}

	// This unit test tests if arithmetic operations and string concatenation for
	// binary expressions are evaluated correctly
	@Test
	void testBinaryEval1() {
		TreeEvaluator eval = new TreeEvaluator();

		// tests if 8+4 = 12
		Expr expr1 = new Expr.binOp(new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(8), new Expr.Literal(4));
		Object expr1Eval = eval.evaluate(expr1);
		int test1 = 12;
		assertEquals(test1, expr1Eval, "should be the same");

		// tests if 10.5 - 3.5 = 7.0
		Expr expr2 = new Expr.binOp(new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(10.5),
				new Expr.Literal(3.5));
		Object expr2Eval = eval.evaluate(expr2);
		double test2 = 7.0;
		assertEquals(test2, expr2Eval, "should be the same");

		// tests if 30/7 = 4. EZlang is set to round down like in java if result is in
		// float
		Expr expr3 = new Expr.binOp(new Token(TokenType.SLASH, "/", null, 1), new Expr.Literal(30),
				new Expr.Literal(7));
		Object expr3Eval = eval.evaluate(expr3);
		int test3 = 4;
		assertEquals(test3, expr3Eval, "should be the same");

		// tests if 7 * 3.5 = 24.5. EZlang is set to turn result into double if any of
		// the operands are doubles
		Expr expr4 = new Expr.binOp(new Token(TokenType.STAR, "*", null, 1), new Expr.Literal(7),
				new Expr.Literal(3.5));
		Object expr4Eval = eval.evaluate(expr4);
		double test4 = 24.5;
		assertEquals(test4, expr4Eval, "should be the same");

		// tests if "te" + "st" = "test"
		Expr expr5 = new Expr.binOp(new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal("te"),
				new Expr.Literal("st"));
		Object expr5Eval = eval.evaluate(expr5);
		String test5 = "test";
		assertEquals(test5, expr5Eval, "should be the same");

	}

	// This unit test tests if binary expressions for boolean operations are
	// evaluated correctly
	@Test
	void testBinaryEval2() {
		TreeEvaluator eval = new TreeEvaluator();

		// tests if 100 >100 is false
		Expr expr1 = new Expr.binOp(new Token(TokenType.GREATER, ">", null, 1), new Expr.Literal(100),
				new Expr.Literal(100));
		Object expr1Eval = eval.evaluate(expr1);
		boolean test1 = false;
		assertEquals(test1, expr1Eval, "should be the same");

		// tests if 100 >= 100 is true
		Expr expr2 = new Expr.binOp(new Token(TokenType.GREATER_EQUAL, ">=", null, 1), new Expr.Literal(100),
				new Expr.Literal(100));
		Object expr2Eval = eval.evaluate(expr2);
		boolean test2 = true;
		assertEquals(test2, expr2Eval, "should be the same");

		// tests if -34.6 <56.8 is true.
		Expr expr3 = new Expr.binOp(new Token(TokenType.LESS, "<", null, 1),
				new Expr.unaryOp(new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(34.6)),
				new Expr.Literal(56.8));
		Object expr3Eval = eval.evaluate(expr3);
		boolean test3 = true;
		assertEquals(test3, expr3Eval, "should be the same");

		// tests if 55.55 >= 55.55 is true
		Expr expr4 = new Expr.binOp(new Token(TokenType.LESS_EQUAL, ">=", null, 1), new Expr.Literal(55.55),
				new Expr.Literal(55.55));
		Object expr4Eval = eval.evaluate(expr4);
		boolean test4 = true;
		assertEquals(test4, expr4Eval, "should be the same");

	}

	// This test checks if grouping expressions are evaluated correctly
	@Test
	void testGroupEval() {
		TreeEvaluator eval = new TreeEvaluator();

		// tests if 2*(3+4) = 14
		Expr literal1 = new Expr.Literal(2);
		Expr binOp1 = new Expr.binOp(new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(3), new Expr.Literal(4));
		Expr group1 = new Expr.Group(binOp1);
		Expr overall = new Expr.binOp(new Token(TokenType.STAR, "*", null, 1), literal1, group1);
		Object overallResult = eval.evaluate(overall);

		int test = 14;
		assertEquals(test, overallResult, "Should be the same");
	}

	// this test checks that assignment works properly with numbers
	@Test
	void testAssignInt() {
		TreeEvaluator eval = new TreeEvaluator();

		// tests if a = 5 i.e. 5 is assigned to a.
		Token name = new Token(TokenType.ID, "a", null, 1);
		Literal literal = new Expr.Literal(5);
		Assign assign = new Expr.Assign(name, literal);
		Object value = eval.evaluate(assign);

		// tests if name of variable is a
		assertEquals("a", assign.getName().getLexeme(), "Should be the same");
		// tests if value assigned to a is 5
		assertEquals(5, value, "Should be the same");

		// tests a+1. a =5 from above.
		Token op = new Token(TokenType.PLUS, "+", null, 1);
		Literal literal2 = new Expr.Literal(1);
		binOp bin = new binOp(op, assign, literal2);
		Object value2 = eval.evaluate(bin);

		// tests if a+1 = 6
		assertEquals(6, value2, "Should be the same");

		// assigns 2.5 to b, b = 2.5
		Token name2 = new Token(TokenType.ID, "b", null, 1);
		Literal literal3 = new Expr.Literal(2.5);
		Assign assign2 = new Expr.Assign(name2, literal3);

		// (a+1) = 6
		Group group = new Group(bin);

		// b*(a+1) = 2.5*(6) = 15.0
		Token op2 = new Token(TokenType.STAR, "*", null, 1);
		binOp bin2 = new binOp(op2, assign2, bin);
		Object value3 = eval.evaluate(bin2);

		assertEquals(15.0, value3, "Should be the same");
	}

	// this test checks that assignment works properly with Strings
	@Test
	void testAssignString() {
		TreeEvaluator eval = new TreeEvaluator();

		// tests if a = "test" i.e. "test" is assigned to a.
		Token name = new Token(TokenType.ID, "a", null, 1);
		Literal literal = new Expr.Literal("test");
		Assign assign = new Expr.Assign(name, literal);
		Object value = eval.evaluate(assign);

		assertEquals("test", value, "Should be the same");
		
		//tests if a + "Again" = "testAgain"
		Literal literal2 = new Expr.Literal("Again");
		Token op = new Token(TokenType.PLUS, "+", null, 1);
		binOp bin2 = new binOp(op, assign, literal2);
		Object value2 = eval.evaluate(bin2);
		
		assertEquals("testAgain", value2, "Should be the same");

	}
	
	//tests whether variable expressions are made correctly
	@Test
	void testVarExp() {
		TreeEvaluator eval = new TreeEvaluator();

		// tests if a variable with name var is made
		Token name = new Token(TokenType.ID, "var", null, 1);
		Variable var = new Variable(name);
		
		assertEquals("var", var.getName().getLexeme(), "Should be the same");
	
		// value of variable should be null since it has not been initialized to anything
		Object varValue = eval.evaluate(var);
		assertEquals(null, varValue, "Should be the same");
		

	}
	
	//tests whether variable statements are made correctly
	@Test
	void testVarStmt() {
		TreeEvaluator eval = new TreeEvaluator();
		
		Token name = new Token(TokenType.ID, "var", null, 1);
		Expr initial = new Expr.Literal(7);
		Var var = new Var(name, initial);
		TreeMaker tm = new TreeMaker();
		String print = tm.make(var);
		
		assertEquals("(= var 7)", print, "Should be the same");
		

	}
	
	

}
