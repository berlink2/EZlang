package ast;

/**
 * Class for grouping expression nodes
 * @author Berlian K
 *
 */
public class ExprGroup extends Expr {
	private final Expr groupExpr; //Expressions inside a grouping

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
