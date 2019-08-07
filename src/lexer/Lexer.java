package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
	private final String source;
	private final List<Token> tokens = new ArrayList<>();
	private static final Map<String, TokenType> resKeywords = new HashMap<>();
	private int start = 0;
	private int curr = 0;
	private int line = 1;
	private boolean isEnd;
	
		
	

	public Lexer(String source) {
		this.source = source;
		resKeywords.put("and", TokenType.AND);
		resKeywords.put("else", TokenType.ELSE);
		resKeywords.put("false", TokenType.FALSE);
		resKeywords.put("if", TokenType.IF);
		resKeywords.put("null", TokenType.NULL);
		resKeywords.put("or", TokenType.OR);
		resKeywords.put("print", TokenType.PRINT);
		resKeywords.put("true", TokenType.TRUE);
		resKeywords.put("while", TokenType.WHILE);
	}

	/*
	 * Method that is fed source code until end of file is reached, calls methods
	 * that make tokens, and returns list of tokens to be parsed.
	 * 
	 * @return
	 */
	public List<Token> tokenStream() {
		while (!checkEnd()) {
			start = curr;
			scanToken();
		}

		tokens.add(new Token(TokenType.EOF, "", null, line));
		return tokens;
	}

	/*
	 * method that scans source code for tokens
	 */
	private void scanToken() {
		char c = next();
		switch (c) {
		case '(':
			tokenize(TokenType.LEFT_PARENTHESIS);
			break;
		case ')':
			tokenize(TokenType.RIGHT_PARENTHESIS);
			break;
		case '{':
			tokenize(TokenType.LEFT_BRACKET);
			break;
		case '}':
			tokenize(TokenType.RIGHT_BRACKET);
			break;
		case ',':
			tokenize(TokenType.COMMA);
			break;
		case '.':
			tokenize(TokenType.DOT);
			break;
		case '-':
			tokenize(TokenType.MINUS);
			break;
		case '+':
			tokenize(TokenType.PLUS);
			break;
		case ';':
			tokenize(TokenType.SEMICOLON);
			break;
		case '*':
			tokenize(TokenType.STAR);
			break;
		case '!':
			tokenize(match('=') ? TokenType.EXCLAMATION_EQUAL : TokenType.EXCLAMATION);
			break;
		case '=':
			tokenize(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
			break;
		case '<':
			tokenize(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
			break;
		case '>':
			tokenize(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
			break;
		case '/':
			if (match('/')) {
				while (getCurrChar() != '\n' && !checkEnd()) {
					next();
				}

			} else {
				tokenize(TokenType.SLASH);
			}

		case ' ':
		case '\r':
		case '\t':
			// Ignore whitespace.
			break;
		case '\n':
			line++;
			break;
		case '"':
			tokenizeString();
			break;
		default:
			if (isDigit(c)) {
				tokenizeNumber();
			} else if (isAlpha(c)) {
				tokenizeID();
			}
			break;
		}
	}

	private void tokenizeID() {
		while (isAlpha(getCurrChar())) {
			next();
		}
		String kw = source.substring(start, curr);
		if (resKeywords.get(kw) == null) {
			tokenize(TokenType.ID);
		} else {
			tokenize(resKeywords.get(kw));

		}
	}

	/**
	 * method that iterates to the next char in the source code and returns the
	 * previous char
	 * 
	 * @return char
	 */
	private char next() {

		curr++;

		return source.charAt(curr - 1);

	}

	

	/*
	 * method that checks for second char in 2 character tokens. Specifically, is
	 * looking for = in !=, <=, etc.
	 * 
	 */
	private boolean match(char input) {
//		if (checkEnd()) {
//			return false;
//		}
//		else if (source.charAt(curr)!= input) {
//			return false;
//		}
//		curr++;
//		return true;
		if (checkEnd())
			return false;
		if (source.charAt(curr) != input)
			return false;

		curr++;
		return true;
	}

	/*
	 * method that returns current char
	 */
	private char getCurrChar() {
		if (checkEnd()) {
			return '\0';
		}
		return source.charAt(curr);
	}

	private char getNextChar() {
		if (curr + 1 >= source.length()) {
			return '\0';
		}
		return source.charAt(curr + 1);
	}
	
	private boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	/*
	 * checks if char is a digit
	 */
	private boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}

	/*
	 * method for tokenizing numbers
	 */
	private void tokenizeNumber() {
		while (isDigit(getCurrChar())) {
			next();
		}

		// if . is encountered check to see if next char is a number
		if (getCurrChar() == '.' && isDigit(getNextChar())) {
			next();
			while (isDigit(getCurrChar())) {
				next();
			}
			tokenize(TokenType.NUMBER, Double.parseDouble(source.substring(start, curr)));
		}

		tokenize(TokenType.NUMBER, Integer.parseInt(source.substring(start, curr)));
				

	}

	/*
	 * method for tokenizing strings
	 */
	private void tokenizeString() {
		while (getCurrChar() != '"' && !checkEnd()) {
			if (getCurrChar() == '\n') {
				line++;
				
			}
			next();
		}
	
		next();

		String string = source.substring(start + 1, curr - 1);
		tokenize(TokenType.STRING, string);

	}
	
	/*
	 * method that makes tokens
	 */
	private void tokenize(TokenType tokenType) {
		tokenize(tokenType, null);
	}

	/*
	 * method that makes tokens for literals with values
	 */
	private void tokenize(TokenType tokenType, Object literal) {
		String text = source.substring(start, curr);
		tokens.add(new Token(tokenType, text, literal, line));
	}
	/*
	 * method that checks if all chars have been iterated through.
	 */
	private boolean checkEnd() {
		isEnd = curr >= source.length();
		return isEnd;
	}

}
