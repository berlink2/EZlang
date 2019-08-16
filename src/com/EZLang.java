package com;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import ast.*;
import lexer.*;
import parser.*;

public class EZLang {
	private static final TreeInterpreter interpreter = new TreeInterpreter();
	private static Scanner s = new Scanner(System.in);
	
	
	public static void main(String[] args) throws IOException{
		///Users/mac/Documents/eclipse-workspace/MScProject/src/tests/FrankensteinTests/ExistsinArray.ez
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
	
	public static boolean checkFile(String file) {
		int fileLength = file.length();
		String extension = file.substring(fileLength -3, fileLength);
		if (extension.equals(".ez")) {
			return true;
		}
		return false;
	}
	
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
	
	public static void runSourceCode(String sourceCode) {
		Lexer lexer = new Lexer(sourceCode);
		lexer.tokenStream();
		List<Token> tokenList = lexer.getTokenList();
		Parser parser = new Parser(tokenList);
		parser.parse();
		List<Stmt> statementList = parser.getStatementList();
		
	
		interpreter.execute(statementList);
//		for (Token token:tokenList) {
//			System.out.println(token);
//		}
	}

}
