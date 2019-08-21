package parser;

import lexer.*;
import ast.*;

import java.util.List;
import java.util.ArrayList;

public class Parser {

	private final List<Token> tokenList; // list of tokens to be parsed
	private int curr = 0; // points to current token in list of tokens, tokenList
	private final List<Stmt> statementList;

	public Parser(List<Token> tokenList) {
		this.tokenList = tokenList;
		this.statementList = new ArrayList<>();
	}

	public List<Stmt> getStatementList() {
		return statementList;
	}

	public void parse() {
		while (!checkEnd()) {
			statementList.add(parseDeclare());
		}

	}

	private Stmt parseDeclare() {

		if (!checkVarAlreadyDeclared(getCurrToken().getLexeme())) {

			if (match(TokenType.ID)) {

				if (getCurrToken().getType() == TokenType.LEFT_SQUARE_BRACKET) {

					parseSubscript();

				}

				return parseVarDeclare();

			}
		}

		return parseStmt();
	}

	private boolean checkVarAlreadyDeclared(String current) {
		for (int i = 0; i < curr; i++) {
			if (tokenList.get(i).getLexeme().equals(current) && getCurrToken().getType() == TokenType.ID) {
				
				
				return true;
			}
		}
		return false;
	}

	private Stmt parseVarDeclare() {
		
		Token variable = getPreviousToken();
		
		Expr initial = null;
		if (match(TokenType.EQUAL)) {
			initial = parseExpr();
		}
		
		consume(TokenType.SEMICOLON);
		
		return new StmtVar(variable, initial);
	}

	private Stmt parseStmt() {
		

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

	private Stmt parsePrintStmt() {
		Expr printedString = parseExpr();
		consume(TokenType.SEMICOLON);
		return new StmtPrint(printedString);
	}

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

	private Stmt parseWhileStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr cond = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);

		Stmt body = parseStmt();

		return new StmtWhile(cond, body);
	}
	
	private Stmt parseRepeatStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr loops = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);
		
		Stmt body = parseStmt();
		return new StmtRepeat(loops,body);
	}

	private List<Stmt> parseBlockStmt() {
		List<Stmt> newStmtList = new ArrayList<>();
		Stmt newStmt = null;
		while (!match(TokenType.RIGHT_CURLY_BRACKET) && !checkEnd()) {

			newStmt = parseDeclare();
			newStmtList.add(newStmt);

		}
		consume(TokenType.RIGHT_CURLY_BRACKET);
		return newStmtList;
	}

	private Stmt parseExprStmt() {
		Expr expression = parseExpr();
		consume(TokenType.SEMICOLON);
		return new StmtExpression(expression);
	}

	private Expr parseExpr() {
		return parseAssignment();
	}

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

	private Expr parseLogicOr() {
		Expr expression = parseLogicAnd();
		while (match(TokenType.OR)) {
			Token op = getPreviousToken();
			Expr right = parseLogicAnd();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseLogicAnd() {
		Expr expression = parseEquality();
		while (match(TokenType.AND)) {
			Token op = getPreviousToken();
			Expr right = parseEquality();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseEquality() {
		Expr expression = parseRelational();
		while (match(TokenType.EQUAL_EQUAL, TokenType.EXCLAMATION_EQUAL)) {
			Token op = getPreviousToken();
			Expr right = parseRelational();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseRelational() {
		Expr expression = parseAdditive();
		while (match(TokenType.GREATER_EQUAL, TokenType.GREATER, TokenType.LESS, TokenType.LESS_EQUAL)) {
			Token op = getPreviousToken();
			Expr right = parseAdditive();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseAdditive() {
		Expr expression = parseMultiplicative();
		while (match(TokenType.PLUS, TokenType.MINUS,TokenType.SHRINK, TokenType.APPEND)) {
			Token op = getPreviousToken();
			Expr right = parseAdditive();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseMultiplicative() {
		Expr expression = parseRead();
		while (match(TokenType.STAR, TokenType.SLASH, TokenType.PERCENT)) {
			Token op = getPreviousToken();
			Expr right = parseRead();
			expression = new ExprBinOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseRead() {

		if (match(TokenType.READ)) {
			Token name = tokenList.get(curr - 3);
			Expr value = null;
			return new ExprRead(name, value);
		}
		return parseUnary();
	}

	private Expr parseUnary() {
		if (match(TokenType.EXCLAMATION, TokenType.MINUS)) {
			Token op = getPreviousToken();
			Expr right = parseUnary();
			return new ExprUnaryOp(op, right);
		}
		return parseSubscript();

	}

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
	
	private List<Expr> parseArray() {
		List<Expr> arrayExpr = new ArrayList<>();
		Expr expression = null;
		while (!checkEnd() && !match(TokenType.RIGHT_SQUARE_BRACKET)) {
			expression = parseExpr();
			arrayExpr.add(expression);

			consume(TokenType.COMMA);

		}

		return arrayExpr;

	}
	
	

	private Expr parsePrimitive() {
		TokenType currType = getCurrToken().getType();
		next();
		switch(currType) {
		
		case LEFT_SQUARE_BRACKET:
			
			return new ExprArray(parseArray());
		case NULL:
			
			return new ExprLiteral(null);
		case TRUE:
			
			return new ExprLiteral(true);
		case FALSE:
			
			return new ExprLiteral(false);
		case STRING:
			
			return new ExprLiteral(getPreviousToken().getLiteral());
		case CHAR:
			
			return new ExprLiteral(getPreviousToken().getLiteral());
		case INTEGER:
			
			return new ExprLiteral(getPreviousToken().getLiteral());
		case FLOAT:
			
			return new ExprLiteral(getPreviousToken().getLiteral());
		case ID: 
			
			return new ExprVariable(getPreviousToken());
		case LEFT_PARENTHESIS:
			
			Expr expression = parseExpr();
			consume(TokenType.RIGHT_PARENTHESIS);
			return new ExprGroup(expression);
		default:
			throw new ParserError("Incorrect EZlang syntax, please check your code.");
		
		}


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
	 * returns current token
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
	 * nexts to next token in list
	 */
	private void next() {
		curr++;
	}

	/**
	 * 
	 * @param token
	 * @return nexts position in list of tokens by 1 and returns previous token if
	 *         not at end of file and current token matches expected token
	 */
	private Token consume(TokenType token) {

		if (!checkEnd() && getCurrToken().getType() == token) {
			next();
			
		}
		return getPreviousToken();

	}

	/**
	 * method that checks if current token matches a particular token type. If it
	 * does, that token will be parsed, and curr will next to the next token.
	 * 
	 * @param TokenType
	 * @return boolean
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