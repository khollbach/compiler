package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.visitor.ExpressionVisitor;

/**
 * References to an array element variable
 * <p>
 * Treat array subscript operation as a special form of unary expression.
 * operand must be an integer expression
 */
public class SubsExpn extends UnaryExpn implements Readable {

    private String variable; // name of the array variable

    /**
     * Returns a string that represents the array subscript.
     */
    @Override
    public String toString() {
        return (variable + "[" + operand + "]");
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }

}
