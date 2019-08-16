package ast;

import lexer.Token;

public class ExprUnaryOp extends Expr {
	private final Token op;
	private final Expr right;

	public ExprUnaryOp(Token op, Expr right) {
		this.right = right;
		this.op = op;
	}
	
	@Override
	public <T> T accept(ExprVisitor<T> v) {
		return v.visitUnaryOp(this);
	}

	public Token getOp() {
		return op;
	}

	public Expr getRight() {
		return right;
	}

}
