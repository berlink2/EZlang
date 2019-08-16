package ast;

import java.util.List;

public class ExprArray extends Expr {
	private final List<Expr> arrayValues;

	public ExprArray(List<Expr> arrayValues) {
		this.arrayValues = arrayValues;

	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {

		return v.visitArray(this);
	}

	public List<Expr> getArrayValues() {
		return arrayValues;
	}

}
