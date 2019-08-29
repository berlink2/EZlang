package ast;


/**
 * Visitor interface for accessing expression tree nodes
 * @author Berlian K
 *
 * @param <T> Generic typing so visit methods can return any type
 */
public interface ExprVisitor<T> {
	
		public T visitBinOp(ExprBinOp expr);
		public T visitLiteral(ExprLiteral expr);
		public T visitUnaryOp(ExprUnaryOp expr);
		public T visitGroup(ExprGroup expr);
		public T visitAssign(ExprAssignment expr);
		public T visitVariable(ExprVariable expr);
		public T visitRead(ExprRead expr);
		public T visitArray(ExprArray expr);
		public T visitSubscript(ExprSubscript expr);
		public T visitArrayAccess(ExprArrayAccess expr);
		
	
}
