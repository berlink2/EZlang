package ast;

//Class for Expression AST that is inherited by all Expr nodes

public abstract class Expr {

	public abstract <T> T accept(ExprVisitor<T> v);

}
