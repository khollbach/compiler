package compiler488.ast.expn;

import compiler488.ast.Printable;
import compiler488.visitor.ExpressionVisitor;

/**
 * Represents the special literal constant associated with writing a new-line
 * character on the output device.
 */
public class SkipConstExpn extends ConstExpn implements Printable {
	/** Returns the string <b>"skip"</b>. */
	@Override
	public String toString() {
		return " newline ";
	}

	public void accept(ExpressionVisitor expnVisitor) {
		expnVisitor.visit(this);
	}
}
