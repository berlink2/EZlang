package com;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import ast.*;
import interpreter.TreeInterpreter;
import lexer.*;
import parser.*;

/**
 * Client class for assembling interpreter components,
 * receiving filepath to a .ez file and taking text of the file 
 * and passing it to be interpreted
 * @author Berlian K
 *
 */
public class EZLangClient {
	/**
	 * Attributes for the client class
	 */
	private static final TreeInterpreter interpreter = new TreeInterpreter();
	private static Scanner s = new Scanner(System.in);
	//Users/mac/Documents/eclipse-workspace/MScProject/src/tests/FrankensteinTests/FizzBuzz.ez
	
	/**
	 * Main method for running interpreter program
	 * @param args The sourcecode of a .ez file
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		System.out.println("----------------------------");
		System.out.println("---- Welcome to EZlang! ----");
		System.out.println("----------------------------");
					
		String filePath = null;
		boolean hasFile = false;
		while(!hasFile) {
			try {
				System.out.println("Please enter a path to an EZlang file: ");
				filePath = s.nextLine();
				boolean validFile = checkFile(filePath);
				if (validFile) {
				hasFile = true;
				} else {
				System.out.println("That is not an EZlang file. Please enter a valid filepath.");
				
				continue;
				}
			} catch (StringIndexOutOfBoundsException e) {
				System.out.println("That is not an EZlang file. Please try again.");
			} 
			
		}
		
		runEZFile(filePath);

	}
	
	/**
	 * This method checks if filepath is a valid filepath to a .ez file
	 * @param file The filepath of a .ez file
	 * @return boolean depending if filepath is valid
	 */
	public static boolean checkFile(String file) {
		int fileLength = file.length();
		String extension = file.substring(fileLength -3, fileLength);
		if (extension.equals(".ez")) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method takes a filepath of a .ez file and 
	 * calls checkFile() to check validity of file path
	 * and if valid passes it to be run by the interpreter
	 * @param path
	 */
	public static void runEZFile(String path)  {
		System.out.println("Running .ez file...The result of running the program can be found below:");
		try {
		byte[] byteArray = Files.readAllBytes(Paths.get(path));
		
		String sourceCode = new String(byteArray, Charset.defaultCharset());
		runSourceCode(sourceCode); 
		} catch (NoSuchFileException e) {
			System.out.println("That is not an EZlang file. Please enter a valid filepath.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method feeds the sourcecode of a .ez file into the appropriate
	 * interpreter components.
	 * @param sourceCode The sourcecode of a .ez file
	 */
	public static void runSourceCode(String sourceCode) {
		Lexer lexer = new Lexer(sourceCode);
		lexer.LexicalAnalysis();
		List<Token> tokenList = lexer.getTokenList();
		Parser parser = new Parser(tokenList);
		parser.parse();
		List<Stmt> statementList = parser.getStatementList();
		
	
		interpreter.execute(statementList);

	}

}
