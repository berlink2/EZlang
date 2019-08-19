package ast;



public interface ExprVisitor<T> {
	
		T visitBinOp(ExprBinOp expr);
		T visitLiteral(ExprLiteral expr);
		T visitUnaryOp(ExprUnaryOp expr);
		T visitGroup(ExprGroup expr);
		T visitAssign(ExprAssignment expr);
		T visitVariable(ExprVariable expr);
		T visitRead(ExprRead expr);
		T visitArray(ExprArray expr);
		T visitSubscript(ExprSubscript expr);
		T visitAssignArray(ExprArrayAccess expr);
		
	
}
