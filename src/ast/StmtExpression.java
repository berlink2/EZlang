package ast;

public class StmtExpression extends Stmt {
	private final Expr expr;

	public StmtExpression(Expr expr) {
		this.expr = expr;
	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {

		return v.visitExpr(this);
	}

	public Expr getExpr() {
		return expr;
	}

}