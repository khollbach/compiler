package compiler488.ast.expn;

import compiler488.visitor.ExpressionVisitor;

/**
 * Boolean literal constants.
 */
public class BoolConstExpn extends ConstExpn {
    private boolean value;	/* value of the constant */

    /**
     * Returns the value of the boolean constant
     */
    @Override
    public String toString() {
        return (value ? "true" : "false");
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }
}
