package ast;

/**
 * class for print statement nodes
 * @author Berlian K
 *
 */
public class StmtPrint extends Stmt {
	final Expr printedString; //printed expression

	public StmtPrint(Expr printedString) {
		this.printedString = printedString;
	}

	@Override
	public <T> T accept(StmtVisitor<T> v) {

		return v.visitPrint(this);
	}

	public Expr getPrintedString() {
		return printedString;
	}

}