package ast;

public abstract class Stmt {

	public abstract <T> T accept(StmtVisitor<T> v);

}
