package compiler488.ast.expn;

import compiler488.visitor.ExpressionVisitor;

/**
 * Represents a literal integer constant.
 */
public class IntConstExpn extends ConstExpn {
    private Integer value;    // The value of this literal.

    /**
     * Returns a string representing the value of the literal.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }
}
