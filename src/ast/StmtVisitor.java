package ast;



public interface StmtVisitor<T> {
	T visitExpr(StmtExpression stmt);
	T visitBlock(StmtBlock stmt);
	T visitIf(StmtIf stmt);
	T visitPrint(StmtPrint stmt);
	T visitWhile(StmtWhile stmt);
	T visitRepeat(StmtRepeat stmt);
	T visitVariable(StmtVariable stmt);
}
