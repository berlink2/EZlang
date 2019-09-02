package ast;

/**
 * Class for statement expression nodes
 * @author Berlian K
 *
 */
public class StmtExpression extends Stmt {
	private final Expr expr; //expression(s) in the statement

	public StmtExpression(Expr expr) {
		this.expr = expr;
	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {

		return v.visitStmtExpr(this);
	}

	public Expr getExpr() {
		return expr;
	}

}