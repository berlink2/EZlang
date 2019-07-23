package ast;

import ast.Expr.*;
import lexer.*;


/*
 * This class is used to create trees manually.
 */
public class TreeMaker implements Expr.Visitor<String> {

	public TreeMaker() {

	}
	
	
	public String make(Expr expr) {
		String visitedString = expr.accept(this);
		return visitedString;
	}
	
	//Below are visit methods for each expression type available so far
	//each method visits the Expr class and retrieves certain info as strings
	
	@Override
	public String visitBinOp(binOp expr) {
		StringBuilder sb = new StringBuilder();
		String op = expr.getOp().toString();
		String leftNode = expr.getLeft().accept(this);
		String rightNode = expr.getRight().accept(this);
		sb.append("(" + op + " " + leftNode + " " + rightNode + ")");
		return sb.toString();
	}

	@Override
	public String visitLiteral(Literal expr) {
		String literal = expr.getVal().toString();
		return literal;
	}

	@Override
	public String visitUnaryOp(unaryOp expr) {
		StringBuilder sb = new StringBuilder();
		String op = expr.getOp().toString();
		String rightNode = expr.getRight().accept(this);
		sb.append("(" + op + rightNode + ")");
		return sb.toString();
	}

	@Override
	public String visitGroup(Group expr) {
		StringBuilder sb = new StringBuilder();
		String node = expr.getNode().accept(this);
		sb.append("(" + "group" + " " + node + ")");
		return sb.toString();
	}

}
