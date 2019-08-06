package parser;

import lexer.*;
import ast.*;
import java.util.List;
import java.util.ArrayList;

public class Parser {
	private final List<Token> tokenList; // list of tokens to be parsed
	private int curr = 0; // points to current token in list of tokens, tokenList
	private final List<Stmt> statementList;

	Parser(List<Token> tokenList) {
		this.tokenList = tokenList;
		this.statementList = new ArrayList<>();
	}

	public void parse() {
		while (!checkEnd()) {
			statementList.add(parseStmt());
		}
	}

	private Stmt parseStmt() {

		if (check(TokenType.IF)) {
			return parseIfStmt();
		}

		if (check(TokenType.WHILE)) {
			return parseWhileStmt();
		}

		if (check(TokenType.LEFT_BRACKET)) {
			return new Stmt.Block(parseBlockStmt());
		}

		if (check(TokenType.PRINT)) {
			return parsePrintStmt();
		}

		return parseExprStmt();
	}

	private Stmt parsePrintStmt() {
		Expr printedString = parseExpr();
		consume(TokenType.SEMICOLON);
		return new Stmt.Print(printedString);
	}

	private Stmt parseIfStmt() {

	}

	private Stmt parseWhileStmt() {

	}

	private List<Stmt> parseBlockStmt() {

	}
	
	private Stmt parseExprStmt() {
		Expr expression = parseExpr();
		consume(TokenType.SEMICOLON);
		return new Stmt.Expression(expression);
	}
	
	private Expr parseExpr() {
		return parseAssignment();
	}
	
	private Expr parseAssignment() {
		
	}
	
	private Expr logicOr() {
		
	}
	
	private Expr logicAnd() {
		
	}
	
	private Expr equality() {
		
	}
	
	private Expr relational() {
		
	}
	
	private Expr additive() {
		
	}
	
	private Expr multiplicative() {
		
	}

	private Expr unary() {
		
	}
	
	
	/**
	 * 
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
	 * returns current token
	 * 
	 * @return Token
	 */
	private Token getCurrToken() {
		return tokenList.get(curr);
	}

	/**
	 * returns next token
	 * 
	 * @return Token
	 */
	private Token getNextToken() {
		return tokenList.get(curr + 1);
	}

	/**
	 * returns previous token
	 * 
	 * @return Token
	 */
	private Token getPreviousToken() {
		return tokenList.get(curr - 1);
	}

	/**
	 * moves to next token in list
	 */
	private void move() {
		curr++;
	}

	/**
	 * 
	 * @param token
	 * @return moves position in list of tokens by 1 and returns previous token if
	 *         not at end of file and current token matches expected token
	 */
	private Token consume(TokenType token) {

		if (!checkEnd() && getCurrToken().getType() == token)
			move();
		return getPreviousToken();

	}

	/**
	 * method that checks if current token matches a particular token type. If it
	 * does, that token will be parsed, and curr will move to the next token.
	 * 
	 * @param token
	 * @return boolean
	 */
	private boolean check(TokenType token) {
		boolean match = false;
		if (getCurrToken().getType() == token) {
			match = true;
			move();
		}
		if (checkEnd()) {
			return false;
		}
		return match;
	}

}