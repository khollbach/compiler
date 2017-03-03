package compiler488.semantics;

import compiler488.ast.AST;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;

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

    @Override
    public String toString() {
        return super.toString() + String.format("undeclared reference: %s", id);
    }
}
