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

		if (getCurrToken().getType() == TokenType.ID && varExists(getCurrToken().getLexeme())) {

			if (match(TokenType.ID)) {

				if (getCurrToken().getType() == TokenType.LEFT_SQUARE_BRACKET) {

					parseSubscript();

				}

				return parseVarDeclare();

			}
		}

		return parseStmt();
	}

	private boolean varExists(String current) {
		for (int i = 0; i < curr; i++) {
			if (tokenList.get(i).getLexeme().equals(current)) {
				return false;
			}
		}
		return true;
	}

	private Stmt parseVarDeclare() {
		Token variable = consume(TokenType.ID);
		Expr initial = null;
		if (match(TokenType.EQUAL)) {
			initial = parseExpr();
		}

		consume(TokenType.SEMICOLON);
		return new Stmt.Var(variable, initial);
	}

	private Stmt parseStmt() {

		if (match(TokenType.IF)) {
			return parseIfStmt();
		}

		if (match(TokenType.WHILE)) {
			return parseWhileStmt();
		}

		if (match(TokenType.LEFT_CURLY_BRACKET)) {
			return new Stmt.Block(parseBlockStmt());
		}

		if (match(TokenType.PRINT)) {
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
		consume(TokenType.LEFT_PARENTHESIS);
		Expr cond = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);

		Stmt then = parseStmt();

		Stmt Else = null;
		if (match(TokenType.ELSE)) {
			consume(TokenType.LEFT_CURLY_BRACKET);
			Else = parseStmt();
			consume(TokenType.RIGHT_CURLY_BRACKET);
		}
		return new Stmt.If(cond, then, Else);

	}

	private Stmt parseWhileStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr cond = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);

		Stmt body = parseStmt();

		return new Stmt.While(cond, body);
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
		return new Stmt.Expression(expression);
	}

	private Expr parseExpr() {
		return parseAssignment();
	}

	private Expr parseAssignment() {
		Expr expression = parseLogicOr();

		if (match(TokenType.EQUAL)) {

			Expr value = parseAssignment();

			if (expression instanceof Expr.Variable) {
				Token variable = ((Expr.Variable) expression).getName();
				return new Expr.Assign(variable, value);
			} else if (expression instanceof Expr.Subscript) {
				Token name = ((Expr.Subscript) expression).getName();
				return new Expr.AssignArray(name, expression, value);
			}
		}

		return expression;
	}

	private Expr parseLogicOr() {
		Expr expression = parseLogicAnd();
		while (match(TokenType.OR)) {
			Token op = getPreviousToken();
			Expr right = parseLogicAnd();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseLogicAnd() {
		Expr expression = parseEquality();
		while (match(TokenType.AND)) {
			Token op = getPreviousToken();
			Expr right = parseEquality();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseEquality() {
		Expr expression = parseRelational();
		while (match(TokenType.EQUAL_EQUAL, TokenType.EXCLAMATION_EQUAL)) {
			Token op = getPreviousToken();
			Expr right = parseRelational();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseRelational() {
		Expr expression = parseAdditive();
		while (match(TokenType.GREATER_EQUAL, TokenType.GREATER, TokenType.LESS, TokenType.LESS_EQUAL)) {
			Token op = getPreviousToken();
			Expr right = parseAdditive();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseAdditive() {
		Expr expression = parseMultiplicative();
		while (match(TokenType.PLUS, TokenType.MINUS)) {
			Token op = getPreviousToken();
			Expr right = parseMultiplicative();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseMultiplicative() {
		Expr expression = parseRead();
		while (match(TokenType.STAR, TokenType.SLASH, TokenType.PERCENT)) {
			Token op = getPreviousToken();
			Expr right = parseRead();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseRead() {

		if (match(TokenType.READ)) {
			Token name = tokenList.get(curr - 3);
			Expr value = null;
			return new Expr.Read(name, value);
		}
		return parseUnary();
	}

	private Expr parseUnary() {
		if (match(TokenType.EXCLAMATION, TokenType.MINUS)) {
			Token op = getPreviousToken();
			Expr right = parseUnary();
			return new Expr.unaryOp(op, right);
		}
		return parseSubscript();

	}

	private Expr parseSubscript() {
		Expr expr = parsePrimitive();
		Token exprName = getPreviousToken();

		while (true) {

			if (match(TokenType.LEFT_SQUARE_BRACKET)) {

				Expr index = parsePrimitive();

				consume(TokenType.RIGHT_SQUARE_BRACKET);

				// creates a Subscript expression where exprName = array name, index =array
				// index, expr = array
				expr = new Expr.Subscript(exprName, index, expr);
				// if (getNextToken().getType()==TokenType.EQUAL)

			} else {
				break;
			}
		}

		return expr;
	}

	private Expr parsePrimitive() {
		if (match(TokenType.NULL))
			return new Expr.Literal(null);
		if (match(TokenType.TRUE))
			return new Expr.Literal(true);
		if (match(TokenType.FALSE))
			return new Expr.Literal(false);
		if (match(TokenType.STRING)) {
			return new Expr.Literal(getPreviousToken().getLiteral());
		}
		if (match(TokenType.CHAR)) {
			return new Expr.Literal(getPreviousToken().getLiteral());
		}
		if (match(TokenType.INTEGER)) {
			return new Expr.Literal(getPreviousToken().getLiteral());
		}
		if (match(TokenType.FLOAT)) {
			return new Expr.Literal(getPreviousToken().getLiteral());
		}
		if (match(TokenType.ID)) {
			return new Expr.Variable(getPreviousToken());
		}
		if (match(TokenType.LEFT_PARENTHESIS)) {
			Expr expression = parseExpr();
			consume(TokenType.RIGHT_PARENTHESIS);
			return new Expr.Group(expression);
		}

		if (match(TokenType.LEFT_SQUARE_BRACKET)) {

			return new Expr.Array(parseArray());
		}

		throw error(getCurrToken(), "");

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

		if (!checkEnd() && getCurrToken().getType() == token) {
			move();
		}
		return getPreviousToken();

	}

	/**
	 * method that checks if current token matches a particular token type. If it
	 * does, that token will be parsed, and curr will move to the next token.
	 * 
	 * @param TokenType
	 * @return boolean
	 */
	private boolean match(TokenType... types) {
		for (TokenType type : types) {
			if (getCurrToken().getType() == type) {
				move();
				return true;
			}
		}

		return false;
	}

	private ParseError error(Token token, String message) {
		return new ParseError();
	}

}