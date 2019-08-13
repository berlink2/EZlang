package ast;
import java.util.List;

import lexer.*;


//Class for Expression AST

public abstract class Expr  {
	
	interface Visitor<T> {
		T visitBinOp(binOp expr);
		T visitLiteral(Literal expr);
		T visitUnaryOp(unaryOp expr);
		T visitGroup(Group expr);
		T visitAssign(Assign expr);
		T visitVariable(Variable expr);
		T visitRead(Read expr);
		T visitArray(Array expr);
		T visitSubscript(Subscript expr);
		T visitAssignArray(AssignArray expr);
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
	 
	 public static class Assign extends Expr {
		final Token name;
		final Expr value;
		
		 public Assign(Token name, Expr value) {
			 this.name = name;
			 this.value = value;
		 }

		@Override
		public <T> T accept(Visitor<T> v) {
			
			return v.visitAssign(this);
		}
		 public Token getName() {
				return name;
			}

			public Expr getValue() {
				return value;
			}
	 }
	 
	 public static class Variable extends Expr { 
		final Token name; 
		
		   public Variable(Token name) {                   
		      this.name = name;                      
		    }

		    <T> T accept(Visitor<T> v) {       
		      return v.visitVariable(this);
		    }   
		    
		    public Token getName() {
				return name;
			}
		                           
		  }
	 
	 public static class Read extends Expr {
		final Token name;
		 final Expr value;
		 
		 public Read(Token name, Expr value) {
			 this.name = name;
			 this.value = value;
		 }

		@Override
		<T> T accept(Visitor<T> v) {
			return v.visitRead(this);
		}
		
		 public Token getName() {
				return name;
			}

			public Expr getValue() {
				return value;
			}
		 
	 }
	 
	 public static class Array extends Expr {
		final List<Expr> arrayValues;
		
		 
		 
		 public Array(List<Expr> arrayValues) {
			 this.arrayValues = arrayValues;
			 
		 }
		@Override
		<T> T accept(Visitor<T> v) {
			// TODO Auto-generated method stub
			return v.visitArray(this);
		}
		 public List<Expr> getArrayValues() {
				return arrayValues;
			}
		 
		 
	 }
	 
	 public static class Subscript extends Expr {
		final Token name;
		 final Expr arrayIndex;
		 final Expr array;
		 
		 public Subscript(Token name, Expr arrayIndex, Expr array) {
			 this.name = name;
			 this.arrayIndex = arrayIndex;
			 this.array = array;
		 }
		 
		@Override
		<T> T accept(Visitor<T> v) {
			// TODO Auto-generated method stub
			return v.visitSubscript(this);
		}
		
		 public Token getName() {
				return name;
			}

			public Expr getArrayIndex() {
				return arrayIndex;
			}

			public Expr getArray() {
				return array;
			}
		 
	 }
	 
	 public static class AssignArray extends Expr{
		 final Expr subscript;
		 final Expr value;
		 
		 public AssignArray(Expr subscript, Expr value) {
			 this.subscript = subscript;
			 this.value=value;
		 }
		@Override
		<T> T accept(Visitor<T> v) {
			// TODO Auto-generated method stub
			return v.visitAssignArray(this);
		}
		
		
			public Expr getsubscript() {
				return subscript;
			}
			public Expr getValue() {
				return value;
			}
		 
	 }
	 
	 
}
