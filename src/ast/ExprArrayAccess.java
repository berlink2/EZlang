package ast;

import lexer.Token;

/**
 * Class for array access expression nodes
 * @author Berlian K
 *
 */
public class ExprArrayAccess extends Expr {
	private final Expr subscript; //subscript for accessing elements in an array
	private final Expr value; //value of a particular element in an array
	private final Token name; //name of the array

	public ExprArrayAccess(Token name, Expr subscript, Expr value) {
		this.subscript = subscript;
		this.value = value;
		this.name = name;
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitArrayAccess(this);
	}

	public Expr getsubscript() {
		return subscript;
	}

	public Expr getValue() {
		return value;
	}

	public Token getName() {
		return name;
	}

}
