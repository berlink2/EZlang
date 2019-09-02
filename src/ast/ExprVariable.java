package ast;

import lexer.Token;

/**
 * Class for variable expression nodes
 * @author Berlian K
 *
 */
public class ExprVariable extends Expr {
	private final Token name; //variable name

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
