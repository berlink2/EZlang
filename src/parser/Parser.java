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
	
	public List<Stmt> parse() {
		while (!checkEnd()) {
			statementList.add(parseDeclare());
		}
		return statementList;
	}
	
	private Stmt parseDeclare() {
		if (match(TokenType.ID)) {
			return parseVarDeclare();
		}
		return parseStmt();
	}
	
	private Stmt parseVarDeclare() {
		Token variable = consume(TokenType.ID);
		Expr initial = null;
		if(match(TokenType.EQUAL)) {
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

		if (match(TokenType.LEFT_BRACKET)) {
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
			consume(TokenType.LEFT_BRACKET);
			Stmt then = parseStmt();
			consume(TokenType.RIGHT_BRACKET);
			Stmt Else = null;
			if(match(TokenType.ELSE)) {
				consume(TokenType.LEFT_BRACKET);
			Else = parseStmt();
			consume(TokenType.RIGHT_BRACKET);
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
		while(!match(TokenType.RIGHT_BRACKET) && !checkEnd()) {
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
		
		if(match(TokenType.EQUAL)) {
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
		while(match(TokenType.OR)) {
			Token op = getPreviousToken();
			Expr right = parseLogicAnd();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}
	
	private Expr parseLogicAnd() {
		Expr expression = parseEquality();
		while(match(TokenType.AND)) {
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
		Expr expression = parseUnary();
		while (match(TokenType.STAR, TokenType.SLASH, TokenType.MODULO)) {
			Token op = getPreviousToken();
			Expr right = parseUnary();
			expression = new Expr.binOp(op, expression, right);
		}
		return expression;
	}

	private Expr parseUnary() {
		if (match(TokenType.EXCLAMATION, TokenType.MINUS)) {
			Token op = getPreviousToken();
			Expr right = parseUnary();
			return new Expr.unaryOp(op, right);
		}
		return parsePrimitive();
		
	}
	
	private Expr parsePrimitive() {
		if (match(TokenType.NULL)) return new Expr.Literal(null);
		if (match(TokenType.TRUE)) return new Expr.Literal(true);
		if (match(TokenType.FALSE)) return new Expr.Literal(false);
		if (match(TokenType.STRING)) {
		      return new Expr.Literal(getPreviousToken().getLiteral());
		    }
		if (match(TokenType.INTEGER)) {
		      return new Expr.Literal(getPreviousToken().getLiteral());
		    }
		if (match(TokenType.FLOAT)) {
		      return new Expr.Literal(getPreviousToken().getLiteral());
		    }
		if(match(TokenType.ID)) {
			return new Expr.Variable(getPreviousToken());
		}
		if(match(TokenType.LEFT_PARENTHESIS)) {
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
	 * @param token
	 * @return boolean
	 */
//	private boolean match(TokenType token) {
//		boolean match = false;
//		if (getCurrToken().getType() == token) {
//			match = true;
//			move();
//		}
//		if (checkEnd()) {
//			return false;
//		}
//		return match;
//	}
	
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