package compiler488.ast.stmt;

import java.io.PrintStream;

import compiler488.ast.Indentable;
import compiler488.visitor.StatementVisitor;

/**
 * Represents a loop in which the exit condition is evaluated before each pass.
 */
public class WhileDoStmt extends LoopingStmt {
    /**
     * Print a description of the <b>while-do</b> construct.
     *
     * @param out   Where to print the description.
     * @param depth How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        //Indentable.printIndentOnLn(out, depth, "while " + expn + " do");
        //body.printOn(out, depth + 1);
        //Indentable.printIndentOnLn(out, depth, "End while-do");

        Indentable.printIndentOnLn(out, depth, "while " + expn + " do");
        body.printOn(out, depth + (body instanceof Scope ? 0 : 1));
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }
}
