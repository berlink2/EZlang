package ast;

import lexer.Token;

public class ExprBinOp extends Expr {
	private final Token op;
	private Expr left;
	private Expr right;

	public ExprBinOp(Token op, Expr left, Expr right) {
		this.left = left;
		this.right = right;
		this.op = op;
	}

	public Token getOp() {
		return op;
	}

	public Expr getLeft() {
		return left;
	}

	public Expr getRight() {
		return right;
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {
		return v.visitBinOp(this);
	}
}
