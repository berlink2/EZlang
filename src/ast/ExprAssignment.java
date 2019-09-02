package ast;

import lexer.Token;

/**
 * Class for assignment expression nodes
 * @author Berlian K
 *
 */
public class ExprAssignment extends Expr {
	private final Token name; //name of the variable
	private final Expr value; //value assigned to variable

	public ExprAssignment(Token name, Expr value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {

		return v.visitAssign(this);
	}

	public Token getName() {
		return name;
	}

	public Expr getValue() {
		return value;
	}
}
