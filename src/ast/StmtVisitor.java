package ast;



public interface StmtVisitor<T> {
	public T visitStmtExpr(StmtExpression stmt);
	public T visitBlock(StmtBlock stmt);
	public T visitIf(StmtIf stmt);
	public T visitPrint(StmtPrint stmt);
	public T visitWhile(StmtWhile stmt);
	public T visitRepeat(StmtRepeat stmt);
	public T visitVariable(StmtVariable stmt);
}
