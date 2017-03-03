package compiler488.semantics;

import compiler488.ast.Readable;
import compiler488.ast.expn.IdentExpn;

/**
 * Created by tarang on 2017-03-02.
 */
public class TypeError extends SemanticError{

    private static final String expectedScalarError = "unexpected reference to non-scalar type: %s";
    private String error;

    public TypeError(IdentExpn identExpn) {
        offendingNode = identExpn;
        error = String.format(expectedScalarError, identExpn.getIdent());
    }
}
