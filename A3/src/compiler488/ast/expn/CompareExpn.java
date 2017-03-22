package compiler488.ast.expn;

import compiler488.visitor.ExpressionVisitor;

/**
 * Place holder for all ordered comparisions expression where both operands must
 * be integer expressions. e.g. < , > etc. comparisons
 */
public class CompareExpn extends BinaryExpn {

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }

}
