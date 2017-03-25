package compiler488.ast.stmt;

import compiler488.ast.Indentable;
import compiler488.visitor.StatementVisitor;

import java.io.PrintStream;

/**
 * Represents a loop in which the exit condition is evaluated after each pass.
 */
public class RepeatUntilStmt extends LoopingStmt {
    /**
     * Print a description of the <b>repeat-until</b> construct.
     *
     * @param out   Where to print the description.
     * @param depth How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOnLn(out, depth, "repeat");
        body.printOn(out, depth + (body instanceof Scope ? 0 : 1));
        Indentable.printIndentOnLn(out, depth, "until " + expn);

    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }
}
