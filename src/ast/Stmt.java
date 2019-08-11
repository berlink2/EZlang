package ast;
import lexer.Token;
import java.util.List;

public abstract class Stmt {
	
	interface Visitor<T> {
		T visitExpr(Expression stmt);
		T visitBlock(Block stmt);
		T visitVar(Var stmt);
		T visitIf(If stmt);
		T visitPrint(Print stmt);
		T visitWhile(While stmt);
		T visitRead(Read stmt);
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
		final List<Stmt> statements;
		
		public Block(List<Stmt> statements) {
			this.statements = statements;
		}

		@Override
		<T> T accept(Visitor<T> v) {
			return v.visitBlock(this);
		}
		
		public List<Stmt> getStatements() {
			return statements;
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
	
	public static class Print extends Stmt {
		final Expr printedString;
		
		public Print(Expr printedString) {
			this.printedString = printedString;
		}

		@Override
		<T> T accept(Visitor<T> v) {
			
			return v.visitPrint(this);
		}
		
		public Expr getPrintedString() {
			return printedString;
		}

		
	}
	
		public static class If extends Stmt {
			final Expr Cond;
			final Stmt Then;
			final Stmt Else;
			
			public If(Expr Cond, Stmt Then, Stmt Else) {
				this.Cond = Cond;
				this.Then = Then;
				this.Else = Else;
				
			}

			@Override
			<T> T accept(Visitor<T> v) {
				
				return v.visitIf(this);
			}
			
			public Expr getCond() {
				return Cond;
			}

			public Stmt getThen() {
				return Then;
			}

			public Stmt getElse() {
				return Else;
			}
			
		}
		
		public static class While extends Stmt {
			final Expr Cond;
			final Stmt body;
			
			public While(Expr Cond, Stmt body) {
				this.Cond = Cond;
				this.body = body;
				
				
			}

			@Override
			<T> T accept(Visitor<T> v) {
				
				return v.visitWhile(this);
			}
			
			public Expr getCond() {
				return Cond;
			}

			public Stmt getBody() {
				return body;
			}

			
		}
		
		public static class Read extends Stmt {
			

			final Token variable;
			final Expr value = null;
			
			public Read(Token variable) {
				this.variable = variable;
			}
			
			
			@Override
			<T> T accept(Visitor<T> v) {
				// TODO Auto-generated method stub
				return v.visitRead(this);
			}
			
			public Token getVariable() {
				return variable;
			}


			public Expr getValue() {
				return value;
			}

			
		}
}
