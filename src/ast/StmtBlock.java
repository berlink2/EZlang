package ast;

import java.util.List;

/**
 * Class for block statement nodes
 * @author Berlian K
 *
 */
public class StmtBlock extends Stmt {
	private final List<Stmt> statements; //statements inside a block

	public StmtBlock(List<Stmt> statements) {
		this.statements = statements;
	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {
		return v.visitBlock(this);
	}

	public List<Stmt> getStatements() {
		return statements;
	}

}