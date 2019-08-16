package ast;

public class StmtIf extends Stmt {
	final Expr Cond;
	final Stmt Then;
	final Stmt Else;

	public StmtIf(Expr Cond, Stmt Then, Stmt Else) {
		this.Cond = Cond;
		this.Then = Then;
		this.Else = Else;

	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {

		return v.visitIf(this);
	}

	public Expr getCond() {
		return Cond;
	}

	public Stmt getThen() {
		return Then;
	}

	public Stmt getElse() {
		return Else;
	}

}
