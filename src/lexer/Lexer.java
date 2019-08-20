package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

	private final String sourceCode;
	private final List<Token> tokenList = new ArrayList<>();
	private static final Map<String, TokenType> resKeywords = new HashMap<>();
	private int startOfToken = 0;
	private int curr = 0;
	private int line = 1;
	private boolean isEnd;

	public Lexer(String sourceCode) {
		this.sourceCode = sourceCode;
		resKeywords.put("and", TokenType.AND);
		resKeywords.put("else", TokenType.ELSE);
		resKeywords.put("false", TokenType.FALSE);
		resKeywords.put("if", TokenType.IF);
		resKeywords.put("null", TokenType.NULL);
		resKeywords.put("or", TokenType.OR);
		resKeywords.put("print", TokenType.PRINT);
		resKeywords.put("true", TokenType.TRUE);
		resKeywords.put("while", TokenType.WHILE);
		resKeywords.put("read", TokenType.READ);
		resKeywords.put("append", TokenType.APPEND);
		resKeywords.put("shrink", TokenType.SHRINK);
		resKeywords.put("repeat", TokenType.REPEAT);
		resKeywords.put("make", TokenType.MAKE);
	}

	/*
	 * Method that is fed source code until end of file is reached, calls methods
	 * that make tokens, and returns list of tokens to be parsed.
	 * 
	 * @return
	 */
	

	public List<Token> getTokenList() {
		return tokenList;
	}

	/*
	 * method that performs lexical analysis on the source code. It turns code that matches tokens into tokens
	 * and puts them into a token list.
	 * Once all the source code has been analyzed, an end of file token is made and inserted into the token list.
	 */
	public void LexicalAnalysis() {
		while (!checkEnd()) {
			startOfToken = curr;
			
		char c = next();
		switch (c) {
		case '(':
			tokenize(TokenType.LEFT_PARENTHESIS);
			break;
		case ')':
			tokenize(TokenType.RIGHT_PARENTHESIS);
			break;
		case '{':
			tokenize(TokenType.LEFT_CURLY_BRACKET);
			break;
		case '}':
			tokenize(TokenType.RIGHT_CURLY_BRACKET);
			break;
		case '[':
			tokenize(TokenType.LEFT_SQUARE_BRACKET);
			break;
		case ']':
			tokenize(TokenType.RIGHT_SQUARE_BRACKET);
			break;
		case ',':
			tokenize(TokenType.COMMA);
			break;
		case '.':
			tokenize(TokenType.DOT);
			break;
		case '%':
			tokenize(TokenType.PERCENT);
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
		case '#':

			while (!match('\n') && !checkEnd()) {
				next();
			}

			break;
		case '/':
			if (match('*')) {
				while (!checkEnd()) {
					next();
					if (match('\n')) {
						line++;

					}
					if (match('*')) {
						next();
						if (match('/')) {
							break;
						}
					}

				}
			} else {
				tokenize(TokenType.SLASH);
			}
			break;
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
		case '\'':
			tokenizeChar();
			break;
		default:
			if (checkNumber(c)) {
				tokenizeNumber();
			} else if (checkLetter(c)) {
				tokenizeID();
			}
			break;
		}
		}
		tokenList.add(new Token(TokenType.EOF, "", null, line));
	}

	private void tokenizeChar() {
		while (!checkEnd() && getCurrChar() != '\'') {
			if (getCurrChar() == '\'') {
				line++;
			}
			next();
		}
		next();

		char read = sourceCode.charAt(startOfToken + 1);
		tokenize(TokenType.CHAR, read);
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

		String string = sourceCode.substring(startOfToken + 1, curr - 1);
		tokenize(TokenType.STRING, string);

	}

	private void tokenizeID() {
		while (checkLetter(getCurrChar())) {
			next();
		}
		String kw = sourceCode.substring(startOfToken, curr);
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

		return sourceCode.charAt(curr - 1);

	}

	/*
	 * method that checks for second char in 2 character tokens. Specifically, is
	 * looking for = in !=, <=, etc.
	 * 
	 */
	private boolean match(char input) {
		if (checkEnd()) {
			return false;
		} else if (sourceCode.charAt(curr) != input) {
			return false;
		}
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
		return sourceCode.charAt(curr);
	}

	private char getNextChar() {
		if (curr + 1 >= sourceCode.length()) {
			return '\0';
		}
		return sourceCode.charAt(curr + 1);
	}

	private boolean checkLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	/*
	 * checks if char is a digit
	 */
	private boolean checkNumber(char c) {
		return c >= '0' && c <= '9';
	}

	/*
	 * method for tokenizing numbers
	 */
	private void tokenizeNumber() {
		while (checkNumber(getCurrChar())) {
			next();
		}

		// if . is encountered check to see if next char is a number
		if (getCurrChar() == '.' && checkNumber(getNextChar())) {
			next();
			while (checkNumber(getCurrChar())) {
				next();
			}
			tokenize(TokenType.FLOAT, Double.parseDouble(sourceCode.substring(startOfToken, curr)));
		} else {

			tokenize(TokenType.INTEGER, Integer.parseInt(sourceCode.substring(startOfToken, curr)));
		}

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
		String text = sourceCode.substring(startOfToken, curr);
		tokenList.add(new Token(tokenType, text, literal, line));
	}

	/*
	 * method that checks if all chars have been iterated through.
	 */
	private boolean checkEnd() {
		isEnd = curr >= sourceCode.length();
		return isEnd;
	}

}
