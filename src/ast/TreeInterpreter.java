package ast;

import ast.Expr.*;
import ast.Stmt.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import lexer.Token;
import lexer.TokenType;

public class TreeInterpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
	private Environment table = new Environment();

	public void execute(List<Stmt> stmtList) {
		for (Stmt stmt : stmtList) {
			execute(stmt);
		}
		
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
		Token opToken = expr.getOp();
		TokenType opTokenType = opToken.getType();
		String leftString = left.toString();
		String rightString = right.toString();

		switch (opTokenType) {
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
			if (left instanceof Character && right instanceof Character) {
				return String.valueOf(left) + right;
			}
			if (left instanceof List && right instanceof List) {
				List newArray = (List) left;
				newArray.addAll((List) right);

				return newArray;
			}
			if (left instanceof List) {
				List array = (List) left;
				array.add(right);

				return array;
			} else if (right instanceof List) {
				List array = (List) right;
				array.add(left);
				return array;
			}
			throw new RuntimeError(opToken, "Invalid operand types for a '+' operation.");
		case MINUS:
			if (left instanceof List && right instanceof Integer) {
				List oldArray = (List) left;
				int oldArraySize = oldArray.size();
				List<Object> newArray = new ArrayList<>();
				int newArraySize = oldArraySize - Integer.parseInt(rightString);
				if (newArraySize < 0) {
					throw new RuntimeError(opToken, "You can't shrink that array to that size.");
				}
				int i = 0;
				while (i < newArraySize) {
					newArray.add(oldArray.get(i));
					i++;
				}

				return newArray;

			}

			if (checkTypes(opToken, left, right)) {
				return performArith(opTokenType, left, right);
			}

			throw new RuntimeError(opToken, "Invalid operand types for a '-' operation.");

		case SLASH:
			if (Double.parseDouble(rightString) == 0) {
				throw new RuntimeError(opToken, "Can't divide by zero.");
			}
			if (checkTypes(opToken, left, right)) {
				return performArith(opTokenType, left, right);
			}

			throw new RuntimeError(opToken, "Invalid operand types. Operands must be an integer or a double.");
		case STAR:
			if (checkTypes(opToken, left, right)) {
				return performArith(opTokenType, left, right);
			}
			throw new RuntimeError(opToken, "Invalid operand types. Operands must be an integer or a double.");
		case PERCENT:
			if (checkTypes(opToken, left, right)) {
				return performArith(opTokenType, left, right);
			}
			throw new RuntimeError(opToken, "Invalid operand types. Operands must be an integer or a double.");
		case GREATER:
			checkTypes(opToken, left, right);
			return Double.parseDouble(leftString) > Double.parseDouble(rightString);

		case GREATER_EQUAL:
			checkTypes(opToken, left, right);
			return Double.parseDouble(leftString) >= Double.parseDouble(rightString);
		case LESS:
			checkTypes(opToken, left, right);
			return Double.parseDouble(leftString) < Double.parseDouble(rightString);
		case LESS_EQUAL:
			checkTypes(opToken, left, right);
			return Double.parseDouble(leftString) <= Double.parseDouble(rightString);
		case EXCLAMATION_EQUAL:

			return !isEqual(left, right);
		case EQUAL_EQUAL:
			return isEqual(left, right);
		case OR:
			if (isTruthy(left) == true || isTruthy(right) == true) {
				if (isTruthy(left) == true) {
					return left;
				} else if (isTruthy(right) == true) {
					return right;
				}

			} else {
				return false;
			}
		case AND:
			if (isTruthy(left) == true && isTruthy(right) == true) {
				return left;
			} else {
				if (isTruthy(left) != true) {
					return left;
				} else if (isTruthy(right) != true) {
					return right;
				}
			}
		default:
			break;

		}

		return null;
	}

	private boolean checkTypes(Token operator, Object left, Object right) {
		if ((left instanceof Double || left instanceof Integer)
				&& (right instanceof Double || right instanceof Integer)) {
			return true;
		}
		throw new RuntimeError(operator, "Invalid Operand types. Operands must be numbers.");
	}

	private Object performArith(TokenType type, Object left, Object right) {
		String leftString = left.toString();
		String rightString = right.toString();
		if (left instanceof Integer && right instanceof Integer) {
			switch (type) {
			case PLUS:
				return (int) left + (int) right;
			case MINUS:
				return (int) left - (int) right;
			case STAR:
				return (int) left * (int) right;
			case SLASH:
				return (int) left / (int) right;
			case PERCENT:
				return (int) left % (int) right;
			default:
				break;
			}

		}
		if (left instanceof Double || right instanceof Double) {
			switch (type) {
			case PLUS:
				return Double.parseDouble(leftString) + Double.parseDouble(rightString);
			case MINUS:
				return Double.parseDouble(leftString) - Double.parseDouble(rightString);
			case STAR:
				return Double.parseDouble(leftString) * Double.parseDouble(rightString);
			case SLASH:
				return Double.parseDouble(leftString) / Double.parseDouble(rightString);
			case PERCENT:
				return Double.parseDouble(leftString) % Double.parseDouble(rightString);
			default:
				break;
			}

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
			checkType(expr.getOp(), right);
			if (right instanceof Integer) {
				return -Integer.parseInt(rightString);
			} else {
				return -Double.parseDouble(rightString);
			}

		default:
			break;

		}

		return null;
	}

	private void checkType(Token operator, Object operand) {
		if (operand instanceof Integer || operand instanceof Double) {
			return;
		}
		throw new RuntimeError(operator, "Operand must be an integer or a double.");
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
		List<Stmt> statementList = stmt.getStatements();

		Environment oldTable = this.table;
		Environment newTable = new Environment(oldTable);

		try {
			this.table = newTable;

			execute(statementList);

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
		table.declare(stmt.getName().getLexeme(), value);

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
	 * This method looks at the current environment table and retrieves the value
	 * that corresponds to the variable's name
	 */
	@Override
	public Object visitVariable(Variable expr) {

		return table.get(expr.getName());
	}

	@Override
	public Void visitIf(If stmt) {
		// checks if cond is truthy or not.
		// if truthy then statement is executed, if falsey else statement is executed
		boolean cond = isTruthy(evaluate(stmt.Cond));
		if (cond) {
			execute(stmt.getThen());
		} else if (stmt.getElse() != null) {
			execute(stmt.getElse());
		}
		return null;
	}

	@Override
	public Void visitPrint(Print stmt) {
		Object value = evaluate(stmt.getPrintedString());

		if (value == null) {
			System.out.println("null");
		} else {
			System.out.println(value.toString());
		}
		return null;
	}

	@Override
	public Void visitWhile(While stmt) {

		// checks if expression if truthy or falsey
		boolean loopCheck = isTruthy(evaluate(stmt.getCond()));

		while (loopCheck) { // continuously execute statement until loop termination condition is met
			execute(stmt.getBody());

			loopCheck = isTruthy(evaluate(stmt.getCond()));
		}

		return null;
	}

	/**
	 * 
	 */
	@Override
	public Object visitRead(Read expr) {
		String input = null;
		Scanner s = new Scanner(System.in);

		try {
			String variable = expr.getName().getLexeme();
			System.out.print("Please input a value for " + variable + ": ");
			input = s.nextLine();
			int length = input.length();
			char first = input.charAt(0);

			if (length == 1 && first >= 'A' && first <= 'z') {
				return first;
			}

			if (first >= '0' && first <= '9') {
				for (int i = 0; i < length; i++) {
					if (input.charAt(i) == '.') {
						return Double.parseDouble(input);
					}
				}
				return Integer.parseInt(input);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			s.close();
		}
		return input;
	}

	@Override
	public Object visitArray(Array expr) {
		List<Object> arrayValues = new ArrayList<>();
		for (Expr expression : expr.getArrayValues()) {
			arrayValues.add(evaluate(expression));
		}

		return arrayValues;
	}

	@Override
	public Object visitSubscript(Subscript expr) {

		List arrayValues = (List) evaluate(expr.getArray());
		int index = (int) evaluate(expr.arrayIndex);

		return arrayValues.get(index);
	}

	@Override
	public Object visitAssignArray(AssignArray expr) {

		Object newValue = evaluate(expr.getValue());

		Token name = expr.getName();
		Object arrayObject = table.get(name);
		List<Object> array = (List) arrayObject;

		Subscript subscript = (Subscript) expr.getsubscript();
		;
		Object indexObject = evaluate(subscript.getArrayIndex());

		int index = (int) indexObject;

		array.set(index, newValue);

		return newValue;
	}

}
