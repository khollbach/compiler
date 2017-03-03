package compiler488.ast.expn;

import compiler488.visitor.ExpressionVisitor;

/**
 * Place holder for all binary expression where both operands must be integer
 * expressions.
 */
public class ArithExpn extends BinaryExpn {

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }
}
