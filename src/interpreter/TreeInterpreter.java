package interpreter;

import ast.*;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;


import lexer.Token;
import lexer.TokenType;

/**
 * Class that visits with methods for evaluating each type of node of the
 * abstract syntax tree.
 * @author Berlian K
 *
 */
public class TreeInterpreter implements ExprVisitor<Object>, StmtVisitor<Void> {
	/**
	 * Attribute for scope in a EZlang program.
	 */
	private Environment table = new Environment();

	/**
	 * This method executes a list of statements that were made from the parser
	 * @param stmtList List of statements received from the parser for interpreting
	 */
	public void execute(List<Stmt> stmtList) {
		for (Stmt stmt : stmtList) {
			execute(stmt);
		}

	}

	
	/**
	 * Method for evaluating an expression
	 * @param expr Expression to be evaluated
	 * @return Value of the expression
	 */
	public Object evaluate(Expr expr) {
		Object result = call(expr);
		if (result == null) {
			return null;
		}
		return result;

	}

	/**
	 * Accesses an expression tree node for evaluating
	 * 
	 * @param expr Expression to be evaluated
	 * @return Value of an expression
	 */
	private Object call(Expr expr) {
		return expr.accept(this);
	}

	
	/**
	 * Accesses a Statement tree node 
	 * @param stmt A statement tree node to be executed
	 */
	private void execute(Stmt stmt) {
		stmt.accept(this);
	}

	/**
	 * 
	 * evaluates and returns the result of a binary expression
	 * @param A binary operation expression tree node
	 * @return Value of the binary operation
	 */
	@Override
	public Object visitBinOp(ExprBinOp expr) {
		Token opToken = expr.getOp();

		if (opToken.getType() == TokenType.AND) {
			Object leftCond = evaluate(expr.getLeft());

			if (isTruthy(leftCond) != true) {
				return leftCond;
			} else {
				Object rightCond = evaluate(expr.getRight());
				return rightCond;
			}

		}

		Object left = evaluate(expr.getLeft());
		Object right = evaluate(expr.getRight());

		TokenType opTokenType = opToken.getType();
		String leftString = left.toString();
		String rightString = right.toString();

		switch (opTokenType) {
		case PLUS:

			if (left instanceof Integer && right instanceof Integer) {

				return (Integer) left + (Integer) right;
			}

			if (left instanceof Double && right instanceof Double) {

				return (Double) left + (Double) right;
			}

			if (left instanceof String && (right instanceof String || right instanceof Character)) {
				return leftString + rightString;
			}

			if (left instanceof String && (right instanceof Integer || right instanceof Double)) {
				return leftString + String.valueOf(right);
			}

			if (left instanceof Double && right instanceof String) {

				return Double.parseDouble(leftString) + Double.parseDouble(rightString);

			}

			if (left instanceof Integer && right instanceof String) {

				return Integer.parseInt(leftString) + Integer.parseInt(rightString);

			}

			if ((left instanceof Double && right instanceof Integer)
					|| (left instanceof Integer && right instanceof Double)) {

				return Double.parseDouble(leftString) + Double.parseDouble(rightString);

			}

			if (left instanceof Character && right instanceof Character) {
				return String.valueOf(left) + right;
			}

			throw new RuntimeError(opToken, "Invalid operand types for a '+' operation.");

		case MINUS:

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
		case APPEND:
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
			throw new RuntimeError(opToken, "Append operations must involve at least one array.");
		case SHRINK:
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
			throw new RuntimeError(opToken,
					"You can only perform a shrink operation with an array and an integer. The array must be on the left side.");

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
		default:
			break;

		}

		return null;
	}

	/**
	 * Method that type checks a binary operation
	 * @param operator The operator of the operation
	 * @param left The left node of a binary operation
	 * @param right The right node of a binary operation
	 * @return boolean value of type checking a binary operation
	 * @throws RuntimeError is operands are not numbers
	 */
	private boolean checkTypes(Token operator, Object left, Object right) {
		if ((left instanceof Double || left instanceof Integer)
				&& (right instanceof Double || right instanceof Integer)) {
			return true;
		}
		throw new RuntimeError(operator, "Invalid Operand types. Operands must be numbers.");
	}

	/**
	 * Method for performing arithmetic operations
	 * @param type Type of token for a binary operation
	 * @param left node of a binary operation
	 * @param right node of a binary operation
	 * @return result of the binary operation
	 */
	private Object performArith(TokenType type, Object left, Object right) {
		String leftString = left.toString();
		String rightString = right.toString();
		if (left instanceof Integer && right instanceof Integer) {
			switch (type) {
			case PLUS:
				return (Integer) left + (Integer) right;
			case MINUS:
				return (Integer) left - (Integer) right;
			case STAR:
				return (Integer) left * (Integer) right;
			case SLASH:
				return (Integer) left / (Integer) right;
			case PERCENT:
				return (Integer) left % (Integer) right;
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
	 * 
	 * evaluates and returns the value of a literal expression
	 * @param Literal expression tree node
	 * @return Value of the literal expression
	 */
	@Override
	public Object visitLiteral(ExprLiteral expr) {

		return expr.getVal();
	}

	
	/**
	 * 
	 * evaluates and returns the value of a unary  expression
	 * @param Unary operation expression tree node
	 * @return Value of the unary operation expression
	 */
	@Override
	public Object visitUnaryOp(ExprUnaryOp expr) {
		Object right = evaluate(expr.getRight());
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

	/**
	 * Method that type checks for a unary operation
	 * @param  Operator of the unary operation
	 * @param  Value of the operand in a unary operation
	 */
	private void checkType(Token operator, Object operand) {
		if (operand instanceof Integer || operand instanceof Double) {
			return;
		}
		throw new RuntimeError(operator, "Operand must be an integer or a double.");
	}

	
	/**
	 * 
	 * evaluates and returns the value of a grouping expression
	 * @param Grouping expression tree node
	 * @return Value of the Grouping expression
	 */
	@Override
	public Object visitGroup(ExprGroup expr) {

		return evaluate(expr.getGroupExpr());
	}

	
	/**
	 * Helper method to check if two operands are equal and returns boolean
	 * @param left node of a operation
	 * @param right node of a operation
	 * @return boolean that depends if operands are equal
	 */
	private boolean isEqual(Object left, Object right) {
		boolean isEqual = left.equals(right);
		return isEqual;
	}

	
	/**
	 * helper method to evaluate non-boolean values as a boolean
	 * @param object that is checked if truthy or not
	 * @return boolean that depends if input object is truthy or not
	 */
	private boolean isTruthy(Object obj) {
		if (obj == null) {
			return false;
		} else if (obj instanceof Boolean) {
			return (Boolean) obj;
		}
		return true;

	}

	
	/**
	 * this method executes  a statement expression
	 * @param A statement expression tree node
	 * @return null since statements don't return values
	 */
	@Override
	public Void visitStmtExpr(StmtExpression stmt) {
		evaluate(stmt.getExpr());
		return null;
	}

	
	/**
	 * Executes statements in a block statement. Creates a new scope for executing statements
	 * in a block and pops the scope once execution of statements in the scope is finished.
	 * @param A block statement tree node
	 * @return null since statements don't return values
	 */
	@Override
	public Void visitBlock(StmtBlock stmt) {
		List<Stmt> statementList = stmt.getStatements();

		try {
			table.pushScope();
			for (Stmt statement : statementList) {
				execute(statement);
			}

		} finally {
			table.popScope();

		}

		return null;

	}

	/**
	 * Declares new variables in the current scope
	 * @param A variable statement tree node
	 * @return null since statements don't return values
	 */
	@Override
	public Void visitVariable(StmtVariable stmt) {
		Object value = null;
		if (stmt.getInitial() != null) {
			value = evaluate(stmt.getInitial());

		}

		table.declare(stmt.getName(), value);
		return null;
	}

	
	/**
	 * this method assigns value to the variable in the environment and returns the
	 * value
	 * @param An assignment expression tree node
	 * @return The value of a variable
	 */
	@Override
	public Object visitAssign(ExprAssignment expr) {
		Object value = evaluate(expr.getValue());

		table.assign(expr.getName(), value);
		return value;
	}

	/**
	 * This method looks at the current environment table and retrieves the value
	 * that corresponds to the variable's name
	 * @param A variable expression tree node
	 * @return The value of a variable
	 */
	@Override
	public Object visitVariable(ExprVariable expr) {

		return table.get(expr.getName());
	}

	/**
	 * Executes methods in a if statement. Method checks if condition is truthy or not.
	 * If truthy then statement is executed, if falsey else statement is executed if there is one.
	 * @param An If statement tree node
	 * @return null since statements don't return values
	 */
	@Override
	public Void visitIf(StmtIf stmt) {
		
		boolean cond = isTruthy(evaluate(stmt.getCond()));
		if (cond) {
			execute(stmt.getThen());
		} else if (stmt.getElse() != null) {
			execute(stmt.getElse());
		}
		return null;
	}

	/**
	 * Executes print statement and outputs the content of the print statement if
	 * there is one.
	 * @param A Print statement tree node
	 * @return null since statements don't return values
	 */
	@Override
	public Void visitPrint(StmtPrint stmt) {
		Object value = evaluate(stmt.getPrintedString());

		if (value == null) {
			System.out.println("null");
		} else {
			System.out.println(value.toString());
		}
		return null;
	}

	/**
	 * Executes while statement. Implemented using Java's while loop. Continuously
	 * executes statement until condition is no longer truthy.
	 * @param A While statement tree node
	 * @return null since statements don't return values
	 */
	@Override
	public Void visitWhile(StmtWhile stmt) {

		// checks if expression if truthy or falsey
		boolean loopCheck = isTruthy(evaluate(stmt.getCond()));

		while (loopCheck) { // continuously execute statement until loop termination condition is met
			execute(stmt.getBody());
			loopCheck = isTruthy(evaluate(stmt.getCond()));

		}

		return null;
	}

	/**
	 * Method that takes in a user inputted value and assigns it a corresponding
	 * variable.
	 * @param Read expression tree node
	 * @return user inputted value
	 */
	@Override
	public Object visitRead(ExprRead expr) {
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
			} else if (length > 1 && first >= 'A' && first <= 'z') {
				return String.valueOf(input);
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

	/**
	 * Creates an array using Java's List.
	 * @param Array expression tree node
	 * @return An array
	 */
	@Override
	public Object visitArray(ExprArray expr) {
		List<Object> arrayValues = new ArrayList<>();
		for (Expr expression : expr.getArrayValues()) {
			arrayValues.add(evaluate(expression));
		}

		return arrayValues;
	}

	/**
	 * Method accesses the nth element in an array and returns it
	 * @param Subscript expression tree node
	 * @return value of the subscript i.e. an element in an array
	 */
	@Override
	public Object visitSubscript(ExprSubscript expr) {

		List arrayValues = (List) evaluate(expr.getArray());
		int index = (int) evaluate(expr.getArrayIndex());

		return arrayValues.get(index);
	}

	/**
	 * Method accesses the nth element in an array and changes it, and 
	 * then returns the new value.
	 * @param Array Access expression tree node
	 * @return New value in nth element of an array
	 */
	@Override
	public Object visitArrayAccess(ExprArrayAccess expr) {

		Object newValue = evaluate(expr.getValue());

		Token name = expr.getName();
		Object arrayObject = table.get(name);
		List<Object> array = (List) arrayObject;

		ExprSubscript subscript = (ExprSubscript) expr.getsubscript();

		Object indexObject = evaluate(subscript.getArrayIndex());

		int index = (int) indexObject;

		array.set(index, newValue);

		return newValue;
	}

	/**
	 * Executes repeat statement. Implemented using Java's do-while loop. Executes a statement
	 * for a set number of times.
	 * @param A Repeat statement tree node
	 * @return null since statements don't return values
	 * @throws RuntimeException if the repeat amount is not an integer
	 */
	@Override
	public Void visitRepeat(StmtRepeat stmt) {
		Object loopsObject = evaluate(stmt.getRepeatAmount());
		if (loopsObject instanceof Integer) {
			Integer loops = (Integer) loopsObject;

			int i = 0;
			do {
				execute(stmt.getBody());
				i++;
			} while (i <= loops);

		} else {
			throw new RuntimeException("Repeat amount must be an integer.");
		}
		return null;
	}

}
