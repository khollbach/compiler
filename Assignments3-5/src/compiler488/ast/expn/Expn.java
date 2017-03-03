package compiler488.ast.expn;

import compiler488.ast.AST;
import compiler488.ast.Printable;
import compiler488.semantics.ExpnEvalType;
import compiler488.visitor.ExpressionVisitor;

/**
 * A placeholder for all expressions.
 */
public class Expn extends AST implements Printable {

    private ExpnEvalType evalType;

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }

    public ExpnEvalType evalType() {
        return evalType;
    }

    public void setEvalType(ExpnEvalType evalType) {
        this.evalType = evalType;
    }
}
