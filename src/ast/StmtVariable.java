package ast;

import lexer.Token;

/**
 * Class for variable declaration statement nodes
 * @author Berlian K
 *
 */
public class StmtVariable extends Stmt{
	final Token name; //variable name
	Expr initial; //initialized value of variable

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
