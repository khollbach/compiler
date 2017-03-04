package compiler488.semantics;

import compiler488.ast.expn.Expn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;

/**
 * Created by tarang on 2017-03-02.
 */
public class ReadStmtError extends SemanticError{

    private static final String NON_INTEGER_READ_PARAM = "read given non-integral identifier: %s";

    private String errorMsg;

    public ReadStmtError(Expn expn) {
        offendingNode = expn;
        errorMsg = String.format(NON_INTEGER_READ_PARAM, expn);
    }

    @Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
