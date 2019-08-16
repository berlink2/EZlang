package ast;



public interface StmtVisitor<T> {
	T visitExpr(StmtExpression stmt);
	T visitBlock(StmtBlock stmt);
	T visitVar(StmtVar stmt);
	T visitIf(StmtIf stmt);
	T visitPrint(StmtPrint stmt);
	T visitWhile(StmtWhile stmt);
}
