package compiler488.semantics;

import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;

/**
 * Created by tarang on 2017-03-02.
 */
public class TypeError extends SemanticError{

    /*
        Error message format strings corresponding to various type errors
     */
    private static final String UNEXPECTED_NONSCALAR = "unexpected non-scalar reference: %s";
    private static final String NON_INTEGER_INDEX = "array indexed with non integral expression: %s";

    /**
     * This TypeError's error message
     */
    private String errorMsg;

    public TypeError(IdentExpn identExpn) {
        offendingNode = identExpn;
        errorMsg = String.format(UNEXPECTED_NONSCALAR, identExpn.getIdent());
    }

    public TypeError(SubsExpn subsExpn) {
        offendingNode = subsExpn;
        errorMsg = String.format(NON_INTEGER_INDEX, subsExpn.toString());
    }

    @Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
