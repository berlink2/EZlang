package ast;

/**
 * Abstract statement tree class that is inherited by all statement tree nodes.
 * Contains abstract accept method which allows classes implementing statement visitor interface
 * to access statement tree nodes.
 * @author Berlian K
 *
 */
public abstract class Stmt {
	
	/**
	 * 
	 * @param Statement visitor interface
	 * 
	 */
	public abstract <T> T accept(StmtVisitor<T> v);

}
