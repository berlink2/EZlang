package ast;

/**
 * Class for if else statement nodes 
 * @author Berlian K
 *
 */
public class StmtIf extends Stmt {
	final Expr Cond; //if statement condition
	final Stmt Then; //statements executed if cond is true
	final Stmt Else; //statements excuted if cond is not true

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
