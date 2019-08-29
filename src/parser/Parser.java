package parser;

import lexer.*;
import ast.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Recursive Descent Parser class that takes a list of tokens from lexer and parses them, converting them
 * into appropriate abstract syntax tree nodes.
 * @author Berlian K
 *
 */
public class Parser {
	
	/**
	 * attributes of the parser
	 */
	private final List<Token> tokenList; // list of tokens to be parsed
	private int curr = 0;
	private final List<Stmt> statementList; //List of statements of a program for interpretation by TreeInterpreter class

	/**
	 * Constructor class that is fed a list of tokens from the lexer class
	 * @param List of tokens to be parsed
	 */
	public Parser(List<Token> tokenList) {
		this.tokenList = tokenList;
		this.statementList = new ArrayList<>();
	}

	/**
	 * @return List of statements for interpretation
	 */
	public List<Stmt> getStatementList() {
		return statementList;
	}

	/**
	 * Parses statements in statementList until end of the token list has been reached.
	 */
	public void parse() {
		while (!checkEnd()) {
			statementList.add(parseStmt());
		}

	}

	/**
	 * Method that checks if a token is a statement token and and sends it to 
	 * the corresponding parse method if it is. If not it moves to the next parsing method
	 * @return A statement tree node
	 */
	private Stmt parseStmt() {
		if(match(TokenType.MAKE)) {
			return parseDeclareVar();
		}

		if (match(TokenType.IF)) {
			return parseIfStmt();
		}
		if (match(TokenType.REPEAT)) {
			return parseRepeatStmt();
		}

		if (match(TokenType.WHILE)) {
			return parseWhileStmt();
		}

		if (match(TokenType.LEFT_CURLY_BRACKET)) {
			return new StmtBlock(parseBlockStmt());
		}

		if (match(TokenType.PRINT)) {
			return parsePrintStmt();
		}

		return parseExprStmt();
	}
	
	/**
	 * Method that creates a variable declaration statement tree node
	 * @return Variable Statement node
	 */
	private Stmt parseDeclareVar() {
		
		Token variable = consume(TokenType.ID);
		
		Expr initial = null;
		if (match(TokenType.EQUAL)) {
			initial = parseExpr();
		}
		
		consume(TokenType.SEMICOLON);
		
		return new StmtVariable(variable, initial);
	}

	/**
	 * Parse method that creates a print statement tree node
	 * @return Print Statement node
	 */
	private Stmt parsePrintStmt() {
		Expr printedString = parseExpr();
		consume(TokenType.SEMICOLON);
		return new StmtPrint(printedString);
	}

	/**
	 * Parse method that creates an if statement tree node
	 * @return If statement node
	 */
	private Stmt parseIfStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr cond = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);
		
		Stmt then = parseStmt();
		
		Stmt Else = null;
		if (match(TokenType.ELSE)) {
			
			Else = parseStmt();
			
		}
		return new StmtIf(cond, then, Else);

	}

	/**
	 * Parse method that creates a while statement tree node
	 * @return While statement node
	 */
	private Stmt parseWhileStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr cond = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);

		Stmt body = parseStmt();

		return new StmtWhile(cond, body);
	}
	
	/**
	 * Parse method that creates a repeat statement tree node
	 * @return Repeat Statement node
	 */
	private Stmt parseRepeatStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr loops = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);
		
		Stmt body = parseStmt();
		return new StmtRepeat(loops,body);
	}

	/**
	 * Parse method that creates a block statement tree node
	 * @return Block statement node
	 */
	private List<Stmt> parseBlockStmt() {
		List<Stmt> newStmtList = new ArrayList<>();
		Stmt newStmt = null;
		while (getCurrToken().getType()!=TokenType.RIGHT_CURLY_BRACKET && !checkEnd()) {

			newStmt = parseStmt();
			newStmtList.add(newStmt);

		}
		consume(TokenType.RIGHT_CURLY_BRACKET);
		return newStmtList;
	}

	/**
	 * Method that creates a statement expression Tree node
	 * @return Statement Expression node
	 */
	private Stmt parseExprStmt() {
		Expr expression = parseExpr();
		consume(TokenType.SEMICOLON);
		return new StmtExpression(expression);
	}

	/**
	 * Parse method for parsing expressions
	 * @return Expression tree node
	 */
	private Expr parseExpr() {
		return parseAssignment();
	}

	/**
	 * Parse method that creates assignment expression tree node
	 * @return Assignment expression node
	 */
	private Expr parseAssignment() {
		Expr expression = parseLogicOr();

		if (match(TokenType.EQUAL)) {

			Expr value = parseAssignment();

			if (expression instanceof ExprVariable) {
				Token variable = ((ExprVariable) expression).getName();
				return new ExprAssignment(variable, value);
			} else if (expression instanceof ExprSubscript) {
				Token name = ((ExprSubscript) expression).getName();
				return new ExprArrayAccess(name, expression, value);
			}
		}

		return expression;
	}

	/**
	 * Parse method that creates binary operation expression tree nodes  involving or
	 * @return Binary operation expression node
	 */
	private Expr parseLogicOr() {
		Expr expression = parseLogicAnd();
		while (match(TokenType.OR)) {
			Token op = getPreviousToken();
			Expr right = parseLogicAnd();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	/**
	 * Parse method that creates binary operation expression tree nodes  involving and
	 * @return Binary operation expression node
	 */
	private Expr parseLogicAnd() {
		Expr expression = parseEquality();
		while (match(TokenType.AND)) {
			Token op = getPreviousToken();
			Expr right = parseEquality();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	/**
	 * Parse method that creates binary operation expression tree nodes  involving checking for equality
	 * @return Binary operation expression node
	 */
	private Expr parseEquality() {
		Expr expression = parseRelational();
		while (match(TokenType.EQUAL_EQUAL, TokenType.EXCLAMATION_EQUAL)) {
			Token op = getPreviousToken();
			Expr right = parseRelational();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	/**
	 * Parse method that creates binary operation expression tree nodes  involving comparing values of operands
	 * @return Binary operation expression node
	 */
	private Expr parseRelational() {
		Expr expression = parseAdditive();
		while (match(TokenType.GREATER_EQUAL, TokenType.GREATER, TokenType.LESS, TokenType.LESS_EQUAL)) {
			Token op = getPreviousToken();
			Expr right = parseAdditive();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	/**
	 * Parse method that creates binary operation expression tree nodes  involving addition, subtraction,
	 * array appending, or array shrinking.
	 * @return Binary operation expression node
	 */
	private Expr parseAdditive() {
		Expr expression = parseMultiplicative();
		while (match(TokenType.PLUS, TokenType.MINUS,TokenType.SHRINK, TokenType.APPEND)) {
			Token op = getPreviousToken();
			Expr right = parseMultiplicative();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	/**
	 * Parse method that creates binary operation expression tree nodes involving multiplication, modulo, or division
	 * @return Binary operation expression node
	 */
	private Expr parseMultiplicative() {
		Expr expression = parseRead();
		while (match(TokenType.STAR, TokenType.SLASH, TokenType.PERCENT)) {
			Token op = getPreviousToken();
			Expr right = parseRead();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	/**
	 * Parse method that creates read expression tree nodes  
	 * @return Read expression node
	 */
	private Expr parseRead() {

		if (match(TokenType.READ)) {
			Token name = tokenList.get(curr - 3);
			Expr value = null;
			return new ExprRead(name, value);
		}
		return parseUnary();
	}

	/**
	 * Parse method that creates unary operation expression tree nodes  
	 * @return Unary expression node
	 */
	private Expr parseUnary() {
		if (match(TokenType.EXCLAMATION, TokenType.MINUS)) {
			Token op = getPreviousToken();
			Expr right = parseUnary();
			return new ExprUnaryOp(op, right);
		}
		return parseSubscript();

	}

	/**
	 * Parse method that creates subscript  expression tree nodes  
	 * @return Subscript expression node
	 */
	private Expr parseSubscript() {
		Expr expr = parsePrimitive();
		Token exprName = getPreviousToken();

		while (true) {

			if (match(TokenType.LEFT_SQUARE_BRACKET)) {
				
				Expr index = parseExpr();

				consume(TokenType.RIGHT_SQUARE_BRACKET);

				// creates a Subscript expression where exprName = array name, index =array
				// index, expr = array
				expr = new ExprSubscript(exprName, index, expr);
				

			} else {
				break;
			}
		}

		return expr;
	}
	
	/**
	 * Parse method that creates array  expression tree nodes  
	 * @return array expression node
	 */
	private List<Expr> parseArray() {
		List<Expr> elements = new ArrayList<>();
		
		
		while (!match(TokenType.RIGHT_SQUARE_BRACKET)) {
			
			Expr expression = parseExpr();
			elements.add(expression);
			
			TokenType currTokenType =  getCurrToken().getType();
			if(currTokenType !=TokenType.RIGHT_SQUARE_BRACKET) {
			consume(TokenType.COMMA);
			}
			
			
		}
		return elements;

	}
	
	

	/**
	 * Parse method for parsing arrays and primitives 
	 * @return  expression node
	 * @throws ParserError is there is a detectable syntax error in the .ez program
	 */
	private Expr parsePrimitive() {
		TokenType currType = getCurrToken().getType();
		
		switch(currType) {
		
		case LEFT_SQUARE_BRACKET:
			
			
			next();
			return new ExprArray(parseArray());
		case NULL:
			next();
			return new ExprLiteral(null);
		case TRUE:
			next();
			return new ExprLiteral(true);
		case FALSE:
			next();
			return new ExprLiteral(false);
		case STRING:
			next();
			return new ExprLiteral(getPreviousToken().getLiteral());
		case CHAR:
			next();
			return new ExprLiteral(getPreviousToken().getLiteral());
		case INTEGER:
			
			next();
			return new ExprLiteral(getPreviousToken().getLiteral());
		case FLOAT:
			next();
			return new ExprLiteral(getPreviousToken().getLiteral());
		case ID: 
			next();
			return new ExprVariable(getPreviousToken());
		case LEFT_PARENTHESIS:
			next();
			Expr expression = parseExpr();
			consume(TokenType.RIGHT_PARENTHESIS);
			return new ExprGroup(expression);
		default:
			throw new ParserError("Incorrect EZlang syntax involving " + getCurrToken() + " on line " + getCurrToken().getLine());
		
		}


	}

	

	/**
	 * Method for checking if end of token list has been reached
	 * @return true if end of file is reached, false if not yet end of file
	 */
	private boolean checkEnd() {
		TokenType check = getCurrToken().getType();
		if (check == TokenType.EOF) {

			return true;
		}
		return false;
	}

	/**
	 * returns current token being parsed
	 * 
	 * @return current Token
	 */
	private Token getCurrToken() {
		return tokenList.get(curr);
	}

	/**
	 * returns previous parsed token
	 * 
	 * @return previous Token
	 */
	private Token getPreviousToken() {
		return tokenList.get(curr - 1);
	}

	/**
	 * Moves to next token in token list
	 */
	private void next() {
		curr++;
	}

	/**
	 * This method checks if a parsed token matches an expected token. If it does it is consumed, 
	 * and the parser moves to the next token to be parsed. If not then the program is missing a token
	 * and a parser error is thrown
	 * @param Expected token type
	 * @return consumed token 
	 * @throws ParserError if current token is not expected token
	 */
	private Token consume(TokenType tokenType) {
		Token currToken = getCurrToken();
		if (!checkEnd() && currToken.getType() == tokenType) {
			next();
			return getPreviousToken();
		}
		throw new ParserError("Missing a " + tokenType.toString() + " on line " + currToken.getLine());

	}

	/**
	 * method that checks if current token matches a particular token type. If it
	 * does, that token will be parsed, and parser will move to the next token.
	 * Unlike Consume() does not throw an error if current token is not expected token. 
	 * @param Expected token type
	 * @return boolean depending on if current token matches expected token
	 */
	private boolean match(TokenType... types) {
		for (TokenType type : types) {
			if (getCurrToken().getType() == type) {
				next();
				return true;
			}
		}

		return false;
	}

	

}