package compiler488.ast.expn;

import compiler488.ast.Readable;
import compiler488.visitor.ExpressionVisitor;

/**
 * References to a scalar variable.
 */
public class IdentExpn extends Expn implements Readable {
    private String ident;    // name of the identifier

    private boolean isFunction;

    public IdentExpn(){
        super();
        isFunction = false;
    }

    /**
     * Returns the name of the variable or function.
     */
    @Override
    public String toString() {
        return ident;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public void setFunction(boolean function) {
        isFunction = function;
    }

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }
}
