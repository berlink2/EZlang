package ast;

public class ExprGroup extends Expr {
	private final Expr groupExpr;

	public ExprGroup(Expr groupExpr) {
		this.groupExpr = groupExpr;
	}

	@Override
	public <T> T accept(ExprVisitor<T> v) {
		return v.visitGroup(this);
	}

	public Expr getGroupExpr() {
		return groupExpr;
	}

}
