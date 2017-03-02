package compiler488.semantics;

import compiler488.ast.AST;

/**
 * The umbrella exception for reporting all semantic errors.
 *
 * Created by gg on 01/03/17.
 */
public abstract class SemanticException {

    /**
     * Returns the AST node associated with this exception.
     *
     * @return
     */
    protected abstract AST getOffendingNode();
}
