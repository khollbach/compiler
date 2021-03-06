package compiler488.ast.stmt;

import compiler488.ast.Indentable;
import compiler488.ast.expn.Expn;
import compiler488.visitor.StatementVisitor;

import java.io.PrintStream;

/**
 * The command to return from a function or procedure.
 */
public class ReturnStmt extends Stmt {
    // The value to be returned by a function.
    private Expn value = null;

    /**
     * Print <b>return</b> or <b>return with </b> expression on a line, by itself.
     *
     * @param out   Where to print.
     * @param depth How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOn(out, depth);
        if (value == null)
            out.println("return");
        else
            out.println("return with " + value);
    }

    public Expn getValue() {
        return value;
    }

    public void setValue(Expn value) {
        this.value = value;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        if (value == null)
            return "return";
        else
            return "return with " + value;
    }
}
