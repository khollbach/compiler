package compiler488.semantics;

import compiler488.ast.expn.Expn;
import compiler488.ast.stmt.ReturnStmt;

/**
 * Created by hollbac1 on 3/4/17.
 */
public class ReturnError extends SemanticError {
    private static final String NOT_IN_ROUTINE = "return statement appears outside a routine: %s";
    private static final String NO_EXPRESSION = "no expression in return from function: %s";
    private static final String UNEXPECTED_EXPRESSION = "unexpected expression in return from procedure: %s";

    private String errorMsg;

    public ReturnError(ReturnStmt returnStmt) {
        offendingNode = returnStmt;
        errorMsg = String.format(NO_EXPRESSION, returnStmt.toString());
    }

    public ReturnError(ReturnStmt returnStmt, Expn ignored) {
        offendingNode = returnStmt;
        errorMsg = String.format(UNEXPECTED_EXPRESSION, returnStmt);
    }

    /**
     * Use this constructor for return statements that don't appear in a routine.
     */
    public ReturnError(ReturnStmt returnStmt, boolean ingored) {
        offendingNode = returnStmt;
        errorMsg = String.format(NOT_IN_ROUTINE, returnStmt);
    }


    @Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
