package compiler488.ast.expn;

import compiler488.ast.ASTList;
import compiler488.visitor.ExpressionVisitor;

/**
 * Represents a function call with or without arguments.
 */
public class FunctionCallExpn extends Expn {
    private String ident; // The name of the function.

    private ASTList<Expn> arguments; // The arguments passed to the function.

    /**
     * Returns a string describing the function call.
     */
    @Override
    public String toString() {
        if (arguments.size() > 0) {
            return ident + "(" + arguments + ")";
        } else
            return ident + "";
    }

    public ASTList<Expn> getArguments() {
        return arguments;
    }

    public void setArguments(ASTList<Expn> args) {
        this.arguments = args;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public void accept(ExpressionVisitor expnVisitor) {
        expnVisitor.visit(this);
    }

}
