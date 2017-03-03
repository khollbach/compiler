package compiler488.ast.stmt;


import compiler488.visitor.StatementVisitor;

/**
 * Placeholder for the scope that is the entire program
 */
public class Program extends Scope {

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

}
