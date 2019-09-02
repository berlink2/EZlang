package ast;

import lexer.Token;

/**
 * class for binary expression nodes 
 * @author Berlian K
 *
 */
public class ExprBinOp extends Expr {
	private final Token op; //operator of the expression
	private Expr left; //left operand
	private Expr right; //right operand

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
