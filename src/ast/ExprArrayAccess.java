package ast;

import lexer.Token;

public class ExprArrayAccess extends Expr {
	private final Expr subscript;
	private final Expr value;
	private final Token name;

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
