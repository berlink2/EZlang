package ast;

public class StmtRepeat extends Stmt {
	private final Expr repeatAmount;
	private final Stmt body;
	
	public StmtRepeat(Expr repeatAmount, Stmt body) {
		this.repeatAmount = repeatAmount;
		this.body = body;
	}
	
	public Expr getRepeatAmount() {
		return repeatAmount;
	}

	public Stmt getBody() {
		return body;
	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {
		
		return v.visitRepeat(this);
	}
	
}
