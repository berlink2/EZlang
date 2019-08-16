package ast;

import lexer.Token;

public class StmtVar extends Stmt {
	final Token name;
	Expr initial;

	public StmtVar(Token name, Expr initial) {
		this.name = name;
		this.initial = initial;
	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {
		return v.visitVar(this);
	}

	public Token getName() {
		return name;
	}

	public Expr getInitial() {
		return initial;
	}

}
