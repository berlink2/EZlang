package ast;


/**
 * Abstract expression tree class that is inherited by all expression tree nodes.
 * Contains abstract accept method which allows classes implementing expression visitor interface
 * to access expression tree nodes.
 * @author Berlian K
 *
 */
public abstract class Expr {
	
	/**
	 * 
	 * @param Expression visitor interface
	 * 
	 */
	public abstract <T> T accept(ExprVisitor<T> v);

}
