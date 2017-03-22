package compiler488.ast.expn;

import compiler488.visitor.ExpressionVisitor;

/**
 * Represents the boolean negation of an expression.
 */
public class NotExpn extends UnaryExpn {

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }

}
