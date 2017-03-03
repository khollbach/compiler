package compiler488.ast.expn;

import compiler488.ast.AST;
import compiler488.ast.Printable;
import compiler488.visitor.ExpressionVisitor;

/**
 * A placeholder for all expressions.
 */
public class Expn extends AST implements Printable {

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }
}
