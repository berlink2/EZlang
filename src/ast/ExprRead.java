package ast;

import lexer.Token;

/**
 * Class for read expression nodes
 * @author Berlian k
 *
 */
public class ExprRead extends Expr {
	private final Token name;  //variable name
	private final Expr value; //user inputted value assigned to variable

	public ExprRead(Token name, Expr value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {
		return v.visitRead(this);
	}

	public Token getName() {
		return name;
	}

	public Expr getValue() {
		return value;
	}
}
