package ast;

import lexer.Token;

public class StmtVariable extends Stmt{
	final Token name;
	Expr initial;

	public StmtVariable(Token name, Expr initial) {
		this.name = name;
		this.initial = initial;
	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {
		return v.visitVariable(this);
	}

	public Token getName() {
		return name;
	}

	public Expr getInitial() {
		return initial;
	}
}
