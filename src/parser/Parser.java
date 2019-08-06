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
	
	private Stmt parseDeclare() {
		if (check(TokenType.ID)) {
			return parseVarDeclare();
		}
		return parseStmt();
	}
	
	private Stmt parseVarDeclare() {
		Token variable = consume(TokenType.ID);
		Expr initial = null;
		if(check(TokenType.EQUAL)) {
			initial = parseExpr();
		}
		
		consume(TokenType.SEMICOLON);
		return new Stmt.Var(variable, initial);
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
			consume(TokenType.LEFT_PARENTHESIS);
			Expr cond = parseExpr();
			consume(TokenType.RIGHT_PARENTHESIS);
			
			Stmt then = parseStmt();
			Stmt Else = null;
			if(check(TokenType.ELSE)) {
			Else = parseStmt();
			}
			return new Stmt.If(cond, then, Else);
			
	}

	private Stmt parseWhileStmt() {
		consume(TokenType.LEFT_PARENTHESIS);
		Expr cond = parseExpr();
		consume(TokenType.RIGHT_PARENTHESIS);
		consume(TokenType.LEFT_BRACKET);
		Stmt body = parseStmt();
		consume(TokenType.RIGHT_BRACKET);
		return new Stmt.While(cond, body);
	}

	private List<Stmt> parseBlockStmt() {
		List<Stmt> newStmt = new ArrayList<>();
		while(!check(TokenType.RIGHT_BRACKET) && !checkEnd()) {
			newStmt.add(parseDeclare());
		}
		consume(TokenType.RIGHT_BRACKET);
		return newStmt;
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
		
		if(check(TokenType.EQUAL)) {
			Token left = getPreviousToken();
			Expr value = parseAssignment();
			
			if (expression instanceof Expr.Variable) {
				Token variable = ((Expr.Variable) expression).getName();
				return new Expr.Assign(variable, value);
			}
		}
		
		return expression;
	}
	
	private Expr parseLogicOr() {
		Expr expression = parseLogicAnd();
		while(check(TokenType.OR)) {
			Token op = getPreviousToken();
			Expr right = parseLogicAnd();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}
	
	private Expr parseLogicAnd() {
		Expr expression = parseEquality();
		while(check(TokenType.AND)) {
			Token op = getPreviousToken();
			Expr right = parseEquality();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}
	
	private Expr parseEquality() {
		Expr expression = parseRelational();
		while (check(TokenType.EQUAL_EQUAL) || check(TokenType.EXCLAMATION_EQUAL)) {
			Token op = getPreviousToken();
			Expr right = parseRelational();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}
	
	private Expr parseRelational() {
		Expr expression = parseAdditive();
		while (check(TokenType.GREATER_EQUAL) || check(TokenType.GREATER) || check(TokenType.LESS) || check(TokenType.LESS_EQUAL)) {
			Token op = getPreviousToken();
			Expr right = parseAdditive();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}
	
	private Expr parseAdditive() {
		Expr expression = parseMultiplicative();
		while (check(TokenType.PLUS) || check(TokenType.MINUS)) {
			Token op = getPreviousToken();
			Expr right = parseMultiplicative();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}
	
	private Expr parseMultiplicative() {
		Expr expression = parseUnary();
		while (check(TokenType.STAR) || check(TokenType.SLASH) || check(TokenType.MODULO)) {
			Token op = getPreviousToken();
			Expr right = parseUnary();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseUnary() {
		if (check(TokenType.EXCLAMATION) || check(TokenType.MINUS)) {
			Token op = getPreviousToken();
			Expr right = parseUnary();
			return new Expr.unaryOp(op, right);
		}
		return parsePrimitive();
		
	}
	
	private Expr parsePrimitive() {
		if (check(TokenType.NULL)) return new Expr.Literal(null);
		if (check(TokenType.TRUE)) return new Expr.Literal(true);
		if (check(TokenType.FALSE)) return new Expr.Literal(false);
		if (check(TokenType.STRING)) {
		      return new Expr.Literal(getPreviousToken().getLiteral());
		    }
		if (check(TokenType.NUMBER)) {
		      return new Expr.Literal(getPreviousToken().getLiteral());
		    }
		if(check(TokenType.ID)) {
			return new Expr.Variable(getPreviousToken());
		}
		if(check(TokenType.LEFT_PARENTHESIS)) {
			Expr expression = parseExpr();
			consume(TokenType.RIGHT_PARENTHESIS);
			return new Expr.Group(expression);
		}
		
		throw error(getCurrToken(), "");
		
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
	
	private ParseError error(Token token, String message) {
		return new ParseError();
	}

}