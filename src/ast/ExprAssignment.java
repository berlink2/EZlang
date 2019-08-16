package ast;

import lexer.Token;

public class ExprAssignment extends Expr {
	private final Token name;
	private final Expr value;

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
