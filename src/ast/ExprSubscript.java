package ast;

import lexer.Token;

/**
 * Class for subscript expression nodes
 * @author Berlian K
 *
 */
public class ExprSubscript extends Expr {
	private final Token name; //name of the array
	private final Expr arrayIndex; //element of the array
	private final Expr array; //the array

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
