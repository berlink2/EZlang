package ast;

import java.util.List;

public class StmtBlock extends Stmt {
	private final List<Stmt> statements;

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