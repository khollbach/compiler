package compiler488.ast.stmt;

import compiler488.ast.Indentable;
import compiler488.visitor.StatementVisitor;

/**
 * A placeholder for statements.
 */
public class Stmt extends Indentable {

    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }
}
