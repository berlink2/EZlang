package ast;

/**
 * Class for literal expression nodes
 * @author Berlian K
 *
 */
public class ExprLiteral extends Expr {
	private final Object val; //value of the literal node

	public ExprLiteral(Object val) {
		this.val = val;
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {
		return v.visitLiteral(this);
	}

	public Object getVal() {
		return val;
	}
}
