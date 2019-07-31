package ast;

import ast.Expr.*;
import ast.Stmt.*;
import java.util.List;

public class TreeEvaluator implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
	private Environment table = new Environment();

	public void evaluate(Stmt stmt) {
		execute(stmt);

	}

	/**
	 * 
	 * @param expr
	 * @return result of the evaluated expression
	 */
	public Object evaluate(Expr expr) {
		Object result = call(expr);
		if (result == null) {
			return null;
		}
		return result;

	}

	/**
	 * calls and evaluates an expression and returns the result
	 * 
	 * @param expr
	 * @return
	 */
	private Object call(Expr expr) {
		return expr.accept(this);
	}

	/**
	 * executes a statement
	 * 
	 * @param stmt
	 */
	private void execute(Stmt stmt) {
		stmt.accept(this);
	}

	/**
	 * 
	 * evaluates and returns the result of a binary expression
	 */
	@Override
	public Object visitBinOp(binOp expr) {

		Object left = call(expr.getLeft());
		Object right = call(expr.getRight());
		Object op = expr.getOp();
		String leftString = left.toString();
		String rightString = right.toString();

		switch (expr.getOp().getType()) {
		case PLUS:
			if (left instanceof Integer && right instanceof Integer) {

				return Integer.parseInt(leftString) + Integer.parseInt(rightString);
			}
			if (left instanceof Double || right instanceof Double) {

				return Double.parseDouble(leftString) + Double.parseDouble(rightString);
				

				
			}

			if (left instanceof String && right instanceof String) {
				return leftString + rightString;
			}
		case MINUS:

			if (left instanceof Integer && right instanceof Integer) {

				return Integer.parseInt(leftString) - Integer.parseInt(rightString);
			}
			if (left instanceof Double || right instanceof Double) {

				return Double.parseDouble(leftString) - Double.parseDouble(rightString);
				
			}

		case SLASH:

			if (left instanceof Integer && right instanceof Integer) {

				return Integer.parseInt(leftString) / Integer.parseInt(rightString);
			}
			if (left instanceof Double || right instanceof Double) {

				return Double.parseDouble(leftString) / Double.parseDouble(rightString);
				
			}

		case STAR:
			if (left instanceof Integer && right instanceof Integer) {

				return Integer.parseInt(leftString) * Integer.parseInt(rightString);
			}
			if (left instanceof Double || right instanceof Double) {

				return Double.parseDouble(leftString) * Double.parseDouble(rightString);
				
			}
		case MODULO:
			if (left instanceof Integer && right instanceof Integer) {

				return Integer.parseInt(leftString) % Integer.parseInt(rightString);
			}
			if (left instanceof Double || right instanceof Double) {

				return Double.parseDouble(leftString) % Double.parseDouble(rightString);
				
			}
			

		case GREATER:

			return Double.parseDouble(leftString) > Double.parseDouble(rightString);
		case GREATER_EQUAL:

			return Double.parseDouble(leftString) >= Double.parseDouble(rightString);
		case LESS:

			return Double.parseDouble(leftString) < Double.parseDouble(rightString);
		case LESS_EQUAL:

			return Double.parseDouble(leftString) <= Double.parseDouble(rightString);
		case EXCLAMATION_EQUAL:
			return !isEqual(left, right);
		case EQUAL_EQUAL:
			return isEqual(left, right);
		case OR:
			if (isTruthy(left)==true) {
				return left;
			} else if (isTruthy(right)==true) {
				return right;
			}
		case AND:
			if (isTruthy(left)==true) {
				return left;
			} else {
				if (isTruthy(left)==true) {
					return left;
				}
			}
			return right;

		}

		return null;
	}

	/**
	 * returns the value of a literal
	 */
	@Override
	public Object visitLiteral(Literal expr) {

		return expr.getVal();
	}

	// returns the evaluation of a unary expression
	@Override
	public Object visitUnaryOp(unaryOp expr) {
		Object right = call(expr.getRight());
		String rightString = right.toString();

		switch (expr.getOp().getType()) {
		case EXCLAMATION:
			return !isTruthy(right);
		case MINUS:
			return -Double.parseDouble(rightString);

		}

		return null;
	}

	/**
	 * evaluates expression in a grouping
	 */
	@Override
	public Object visitGroup(Group expr) {

		return call(expr.getNode());
	}

	// helper method to check if two operands are equal and returns boolean
	private boolean isEqual(Object left, Object right) {
		boolean isEqual = left.equals(right);
		if (left == null && right == null) {
			return true;
		}
		if (left == null || right == null) {
			return false;
		}

		return isEqual;
	}

	// helper method to evaluate non-boolean values as a boolean
	private boolean isTruthy(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		return true;

	}

	// this method evaluates the expression in a statement expression
	@Override
	public Void visitExpr(Expression stmt) {
		call(stmt.getExpr());
		return null;
	}

	/**
	 * creates new environment table executes a list of statements in a new nested
	 * environment table and when finished executing returns to older enclosing
	 * environment table
	 */
	@Override
	public Void visitBlock(Block stmt) {
		List<Stmt> statements = stmt.getStatements();

		Environment newTable = new Environment(table);
		Environment oldTable = this.table;

		try {
			this.table = newTable;
			for (Stmt statement : statements) {
				execute(statement);
			}

		} finally {
			this.table = oldTable;
		}
		return null;

	}

	// if variable is not initialized it is set to null
	@Override
	public Void visitVar(Var stmt) {
		Object value = null;
		if (stmt.getInitial() != null) {
			value = call(stmt.getInitial());
		}
		table.define(stmt.getName().getLexeme(), value);
		return null;
	}

	/**
	 * this method assigns value to the variable in the environment and returns the
	 * value
	 */
	@Override
	public Object visitAssign(Assign expr) {
		Object value = call(expr.getValue());

		table.assign(expr.getName(), value);
		return value;
	}

	/**
	 * This method returns looks at the current environment table and retrieves the
	 * value that corresponds to the variable's name
	 */
	@Override
	public Object visitVariable(Variable expr) {

		return table.get(expr.getName());
	}

	@Override
	public Void visitIf(If stmt) {
		// checks if cond is truthy or not.
		// if truthy then statement is executed, if falsey else statement is executed
		boolean cond = isTruthy(stmt.Cond);
		if (cond) {
			execute(stmt.Then);
		} else if (cond == false) {
			execute(stmt.Else);
		}
		return null;
	}

	@Override
	public Void visitPrint(Print stmt) {
		Object value = call(stmt.getPrintedString());
		String printedValue = value.toString();
		System.out.println(printedValue);
		return null;
	}

	@Override
	public Void visitWhile(While stmt) {
		boolean loopCheck = isTruthy(stmt.getCond()); //checks if expression if truthy or falsey
		while (loopCheck) { //continuously execute statement until loop termination condition is met
			execute(stmt.getBody()); 
		}
		return null;
	}

}
