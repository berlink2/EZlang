package ast;

import ast.Expr.*;
import ast.Stmt.Block;
import ast.Stmt.Expression;
import ast.Stmt.If;
import ast.Stmt.Print;
import ast.Stmt.Var;
import ast.Stmt.While;
import lexer.*;


/*
 * This class is used to create trees manually.
 */
public class TreeMaker implements Expr.Visitor<String>, Stmt.Visitor<String> {

	public TreeMaker() {

	}
	
	
	public String make(Expr expr) {
		String visitedString = expr.accept(this);
		return visitedString;
	}
	
	public String make(Stmt stmt) {
		String visitedString = stmt.accept(this);
		return visitedString;
	}
	
	//Below are visit methods for each expression and statement tree type available 
	//each method visits the Expr or stmt class and retrieves certain info as strings
	
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


	@Override
	public String visitAssign(Assign expr) {
		StringBuilder sb = new StringBuilder();
		String name = expr.getName().toString();
		String value = expr.getValue().accept(this);
		sb.append("(" + "=" + " " + name + " "+ value +")");
		return sb.toString();
	}


	@Override
	public String visitVariable(Variable expr) {
		
		return expr.getName().getLexeme();
	}


	@Override
	public String visitExpr(Expression stmt) {
		StringBuilder sb = new StringBuilder();
		sb.append("(" + "stmt" + " " + stmt.getExpr().accept(this)+")");
		return sb.toString();
	}


	@Override
	public String visitBlock(Block stmt) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String visitVar(Var stmt) {
		StringBuilder sb = new StringBuilder();
		if (stmt.initial == null) {
			sb.append("(" + "var" + " " + stmt.getName()+")");
		      return sb.toString();
		    }
		sb.append("(" + "=" + " " + stmt.getName().toString()+" "+stmt.getInitial().accept(this)+")");
		    return sb.toString();
		
	}


	@Override
	public String visitIf(If stmt) {
		
		
		return null;
	}


	@Override
	public String visitPrint(Print stmt) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String visitWhile(While stmt) {
		// TODO Auto-generated method stub
		return null;
	}

}
