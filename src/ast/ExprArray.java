package ast;

import java.util.List;

/**
 * Class for array expression nodes
 * @author Berlian k
 *
 */
public class ExprArray extends Expr {
	/**
	 * values inside an array
	 */
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
