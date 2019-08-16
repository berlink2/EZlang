package ast;

public class StmtWhile extends Stmt {
	final Expr Cond;
	final Stmt body;

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