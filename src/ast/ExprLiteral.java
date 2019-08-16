package ast;

public class ExprLiteral extends Expr {
	private final Object val;

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
