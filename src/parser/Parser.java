package parser;

import lexer.*;
import ast.*;

import java.util.List;
import java.util.ArrayList;

public class Parser {

	private final List<Token> tokenList; // list of tokens to be parsed
	private int curr = 0; // points to current token in list of tokens, tokenList
	private final List<Stmt> statementList;

	/**
	 * @param tokenList
	 */
	public Parser(List<Token> tokenList) {
		this.tokenList = tokenList;
		this.statementList = new ArrayList<>();
	}

	/**
	 * @return
	 */
	public List<Stmt> getStatementList() {
		return statementList;
	}

	/**
	 * 
	 */
	public void parse() {
		while (!checkEnd()) {
			statementList.add(parseStmt());
		}

	}

	/**
	 * @return
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
	 * @return
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
	 * @return
	 */
	private Stmt parsePrintStmt() {
		Expr printedString = parseExpr();
		consume(TokenType.SEMICOLON);
		return new StmtPrint(printedString);
	}

	/**
	 * @return
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
	 * @return
	 */
	private Stmt parseWhileStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr cond = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);

		Stmt body = parseStmt();

		return new StmtWhile(cond, body);
	}
	
	/**
	 * @return
	 */
	private Stmt parseRepeatStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr loops = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);
		
		Stmt body = parseStmt();
		return new StmtRepeat(loops,body);
	}

	/**
	 * @return
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
	 * @return
	 */
	private Stmt parseExprStmt() {
		Expr expression = parseExpr();
		consume(TokenType.SEMICOLON);
		return new StmtExpression(expression);
	}

	/**
	 * @return
	 */
	private Expr parseExpr() {
		return parseAssignment();
	}

	/**
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
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
	 * @return
	 */
	private List<Expr> parseArray() {
		List<Expr> elements = new ArrayList<>();
		
		
		while (!match(TokenType.RIGHT_SQUARE_BRACKET)) {
			//System.out.println(getCurrToken());
			Expr expression = parseExpr();
			elements.add(expression);
			//System.out.println(getCurrToken());
			TokenType currTokenType =  getCurrToken().getType();
			if(currTokenType !=TokenType.RIGHT_SQUARE_BRACKET) {
			consume(TokenType.COMMA);
			}
			
			
		}
		return elements;

	}
	
	

	/**
	 * @return
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
	 * 
	 * @return true if end of file is reached, false if not yet end of file
	 */
	/**
	 * @return
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
	/**
	 * @return
	 */
	private Token getCurrToken() {
		return tokenList.get(curr);
	}

	/**
	 * returns current token
	 * 
	 * @return Token
	 */
	/**
	 * @return
	 */
	private Token getNextToken() {
		return tokenList.get(curr + 1);
	}

	/**
	 * returns previous token
	 * 
	 * @return Token
	 */
	/**
	 * @return
	 */
	private Token getPreviousToken() {
		return tokenList.get(curr - 1);
	}

	/**
	 * nexts to next token in list
	 */
	/**
	 * 
	 */
	private void next() {
		curr++;
	}
	
	/**
	 * @param token
	 * @return
	 */
	private  boolean checkTokenType(TokenType token) {
		boolean match = false;
		TokenType currType = getCurrToken().getType();
		if (currType == token) {
			match = true;
		}
		return match;
	}

	/**
	 * 
	 * @param token
	 * @return nexts position in list of tokens by 1 and returns previous token if
	 *         not at end of file and current token matches expected token
	 */
	/**
	 * @param tokenType
	 * @return
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
	 * does, that token will be parsed, and curr will next to the next token.
	 * 
	 * @param TokenType
	 * @return boolean
	 */
	/**
	 * @param types
	 * @return
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