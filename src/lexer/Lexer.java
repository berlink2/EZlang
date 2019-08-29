package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The lexer is the class that takes in sourcecode of a .ez
 * file and converts matching text into tokens for the parser
 * @author Berlian K
 *
 */
public class Lexer {
	/**
	 * Attributes of the lexer
	 */
	private final String sourceCode;
	private final List<Token> tokenList = new ArrayList<>();
	private static final Map<String, TokenType> resKeywords = new HashMap<>();
	private int startOfToken = 0;
	private int curr = 0;
	private int line = 1;
	private boolean isEnd;

	/**
	 * This constructor is fed sourcecode from the EZlang client for lexical analysis.
	 * It also inserts keywords in resKeywords HashMap for tokenizing reserved keywords
	 * 
	 * @param sourceCode
	 */
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

	
	

	/**
	 * @return Returns a list of tokens that the parser will use
	 */
	public List<Token> getTokenList() {
		return tokenList;
	}

	
	/**
	 *  This method  performs lexical analysis on the source code. It turns code that matches tokens into tokens
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

	/**
	 * Helper method for turning chars into char tokens
	 */
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

	
	/**
	 * Helper method for turning strings into string tokens
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

	/**
	 * Helper method for turning identifiers into ID tokens
	 */
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
	 * @return the previous char after having moved to the subsequent char
	 */
	private char next() {

		curr++;

		return sourceCode.charAt(curr - 1);

	}

	
	/**
	 * method that checks for second char in 2 character tokens. Specifically, is
	 * looking for = in !=, <=, etc.
	 * 
	 * @param input checks if current char matches input char 
	 * @return boolean if char matches expected char or not
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

	
	/**
	 * method that returns current char
	 * @return the current char
	 */
	private char getCurrChar() {
		if (checkEnd()) {
			return '\0';
		}
		return sourceCode.charAt(curr);
	}

	/**
	 * Returns next char or terminates string if end of file has been reached.
	 * @return the next char
	 */
	private char getNextChar() {
		if (curr + 1 >= sourceCode.length()) {
			return '\0';
		}
		return sourceCode.charAt(curr + 1);
	}

	/**
	 * @param input char that is checked if it is a letter or _
	 * @return boolean depending if input char is a letter or _
	 */
	private boolean checkLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
	}

	/*
	 * checks if char is a digit
	 */
	/**
	 * @param input char that is checked if it is a number or not
	 * @return boolean that depends if input char is a number or not
	 */
	private boolean checkNumber(char c) {
		return c >= '0' && c <= '9';
	}

	
	/**
	 * Helper method for tokenizing numbers
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

	
	/**
	 * Method that makes tokens for tokens with no values
	 * @param The token type of a corresponding token
	 */
	private void tokenize(TokenType tokenType) {
		tokenize(tokenType, null);
	}

	
	/**
	 * Method that makes tokens for tokens with values
	 * @param tokenType The type of the token
	 * @param literal The value of the token
	 */
	private void tokenize(TokenType tokenType, Object literal) {
		String text = sourceCode.substring(startOfToken, curr);
		tokenList.add(new Token(tokenType, text, literal, line));
	}

	
	/**
	 * method that checks if all chars have been iterated through i.e. end of file has been
	 * reached
	 * @return boolean that depends if the end of the file has been reached
	 */
	private boolean checkEnd() {
		isEnd = curr >= sourceCode.length();
		return isEnd;
	}

}
