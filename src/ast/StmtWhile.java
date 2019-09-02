package ast;

/**
 * class for while statement nodes
 * @author Berlian K
 *
 */
public class StmtWhile extends Stmt {
	final Expr Cond; //while loop condition
	final Stmt body; //statements inside a while loop

	public StmtWhile(Expr Cond, Stmt body) {
		this.Cond = Cond;
		this.body = body;

	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {

		return v.visitWhile(this);
	}

	public Expr getCond() {
		return Cond;
	}

	public Stmt getBody() {
		return body;
	}

}