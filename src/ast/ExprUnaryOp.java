package ast;

import lexer.Token;

/**
 * Class for unary operation expression nodes
 * @author Berlian K
 *
 */
public class ExprUnaryOp extends Expr {
	private final Token op; //operator token
	private final Expr right; //right operand

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
