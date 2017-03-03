package compiler488.ast.expn;

import compiler488.visitor.ExpressionVisitor;

/**
 * Represents negation of an integer expression
 */
public class UnaryMinusExpn extends UnaryExpn {

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }

}
