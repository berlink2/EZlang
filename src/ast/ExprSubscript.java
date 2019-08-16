package ast;

import lexer.Token;

public class ExprSubscript extends Expr {
	private final Token name;
	private final Expr arrayIndex;
	private final Expr array;

	public ExprSubscript(Token name, Expr arrayIndex, Expr array) {
		this.name = name;
		this.arrayIndex = arrayIndex;
		this.array = array;
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {
		// TODO Auto-generated method stub
		return v.visitSubscript(this);
	}

	public Token getName() {
		return name;
	}

	public Expr getArrayIndex() {
		return arrayIndex;
	}

	public Expr getArray() {
		return array;
	}
}
