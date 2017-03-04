package compiler488.semantics;

import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.stmt.ProcedureCallStmt;

/**
 * Created by gianacop on 3/4/17.
 */
public class RoutineCallError extends SemanticError {

    private static final String PARAM_ARITY_MISMATCH = "expected %d arguments found %d";

    private String errorMsg;

    public RoutineCallError(ProcedureCallStmt procCall, int actualNumParams) {
        errorMsg = String.format(PARAM_ARITY_MISMATCH, actualNumParams, procCall.getArguments().size());
        offendingNode = procCall;
    }

    public RoutineCallError(FunctionCallExpn funcExpn, int actualNumParams) {
        errorMsg = String.format(PARAM_ARITY_MISMATCH, actualNumParams, funcExpn.getArguments().size());
    }
}
