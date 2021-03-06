package compiler488.semantics;

import compiler488.ast.AST;

/**
 * The umbrella exception for reporting all semantic errors.
 * <p>
 * Created by gg on 01/03/17.
 */
public abstract class SemanticError {

    /**
     * Returns the AST node associated with this exception.
     *
     * @return the subtree of the AST associated with this SemanticError.
     */
    protected AST offendingNode;

    /**
     * Returns a message describing this SemanticError.
     *
     * @return the message, as a String
     */
    @Override
    public String toString() {

        return String.format("at line:%d col:%d - ",
                offendingNode.getSourceCoordinateLine(),
                offendingNode.getSourceCoordinateColumn());
    }
}
