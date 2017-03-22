package compiler488.semantics;

import compiler488.ast.expn.FunctionCallExpn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;
import compiler488.ast.stmt.ProcedureCallStmt;

/**
 * Created by tarang on 2017-03-02.
 */
public class UndefinedReferenceError extends SemanticError {

    private String id;

    public UndefinedReferenceError(IdentExpn identExpn) {
        offendingNode = identExpn;
        id = identExpn.getIdent();
    }

    public UndefinedReferenceError(SubsExpn subsExpn) {
        offendingNode = subsExpn;
        id = subsExpn.getVariable();
    }

    public UndefinedReferenceError(ProcedureCallStmt procCall) {
        offendingNode = procCall;
        id = procCall.getName();
    }

    public UndefinedReferenceError(FunctionCallExpn funcExpn) {
        offendingNode = funcExpn;
        id = funcExpn.getIdent();
    }

    @Override
    public String toString() {
        return super.toString() + String.format("undeclared reference: %s", id);
    }
}
