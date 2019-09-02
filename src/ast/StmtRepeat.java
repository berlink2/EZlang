package ast;

/**
 * Class for repeat statement nodes
 * @author Berlian K
 *
 */
public class StmtRepeat extends Stmt {
	private final Expr repeatAmount; //amount of times loop is repeated
	private final Stmt body; //statements inside the loop
	
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
