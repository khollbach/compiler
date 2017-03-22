package compiler488.ast.expn;

import compiler488.visitor.ExpressionVisitor;

/**
 * Place holder for all binary expression where both operands must be boolean
 * expressions.
 */
public class BoolExpn extends BinaryExpn {

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }
}
