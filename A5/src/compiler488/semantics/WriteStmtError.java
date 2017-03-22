package compiler488.semantics;

import compiler488.ast.expn.Expn;

/**
 * Created by gianacop on 3/3/17.
 */
public class WriteStmtError extends SemanticError {

    private static final String NON_INTEGER_OUTPUT = "write statement output expression has non-integer type: %s";

    private String errorMsg;

    public WriteStmtError(Expn expn) {
        errorMsg = String.format(NON_INTEGER_OUTPUT, expn);
        offendingNode = expn;
    }

    @Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
