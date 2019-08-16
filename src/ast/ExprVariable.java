package ast;

import lexer.Token;

public class ExprVariable extends Expr {
	private final Token name;

	public ExprVariable(Token name) {
		this.name = name;
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {
		return v.visitVariable(this);
	}

	public Token getName() {
		return name;
	}
}
