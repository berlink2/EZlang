package ast;
import lexer.*;

public abstract class Expr  {
	
	interface Visitor<T> {
		T visitBinOp(binOp expr);
		T visitLiteral(Literal expr);
		T visitUnaryOp(unaryOp expr);
		T visitGroup(Group expr);
	}
	
	abstract <T> T accept(Visitor<T> v);
	
	 public static class binOp extends Expr {
		final Token op;
		final Expr left;
		final Expr right;
		
		public binOp( Token op, Expr left, Expr right) {
			this.left = left;
			this.right = right;
			this.op = op;
		}
		
		public Token getOp() {
			return op;
		}

		public Expr getLeft() {
			return left;
		}

		public Expr getRight() {
			return right;
		}

		
		<T> T accept(Visitor<T> v) {
			return v.visitBinOp(this);
		}

		
	}
	 
	 public static class unaryOp extends Expr {
		 final Token op;
		 final Expr right;
		 
		 
		 public unaryOp(Token op, Expr right){
			 this.right = right;
			 this.op = op;
		 }
		 
		 <T> T accept(Visitor<T> v) {
				return v.visitUnaryOp(this);
			}

		public Token getOp() {
			return op;
		}

		public Expr getRight() {
			return right;
		}
		 
		 
	 }
	
	 public static class Literal extends Expr {
		final Object val;
		
		public Literal(Object val) {
			this.val = val;
		}
		
		
		<T> T accept(Visitor<T> v) {
			return v.visitLiteral(this);
		}


		public Object getVal() {
			return val;
		}
		
		
	}
	 
	 public static class Group extends Expr {
		 final Expr node;
		 
		 public Group(Expr node) {
			 this.node = node;
		 }
		 <T> T accept(Visitor<T> v) {
			 return v.visitGroup(this);
		 }
		public Expr getNode() {
			return node;
		}
		 
		 
	 }
}
