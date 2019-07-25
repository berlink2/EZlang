package ast;
import lexer.Token;
import java.util.List;

public abstract class Stmt {
	
	interface Visitor<T> {
		T visitExpr(Expression stmt);
		T visitBlock(Block stmt);
		T visitVar(Var stmt);
	}
	
	abstract <T> T accept(Visitor<T> v);
	
	public static class Expression extends Stmt {
		final Expr expr;
		
		public Expression(Expr expr) {
			this.expr = expr;
		}
		
		@Override
		<T> T accept(Visitor<T> v) {
			
			return v.visitExpr(this);
		}
		
		public Expr getExpr() {
			return expr;
		}
		
		
		
	}
	
	public static class Block extends Stmt {

		@Override
		<T> T accept(Visitor<T> v) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public static class Var extends Stmt {
		final Token name;
		final Expr initial;
		
		public Var(Token name, Expr initial) {
			this.name = name;
			this.initial = initial;
		}
		
		@Override
		<T> T accept(Visitor<T> v) {
			return v.visitVar(this);
		}
		
		public Token getName() {
			return name;
		}

		public Expr getInitial() {
			return initial;
		}
		
	}
}
