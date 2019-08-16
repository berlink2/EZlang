package ast;

import lexer.Token;

public class ExprRead extends Expr {
	private final Token name;
	private final Expr value;

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
