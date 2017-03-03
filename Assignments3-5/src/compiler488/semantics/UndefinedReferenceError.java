package compiler488.semantics;

import compiler488.ast.AST;
import compiler488.ast.expn.IdentExpn;

/**
 * Created by tarang on 2017-03-02.
 */
public class UndefinedReferenceError extends SemanticError {

    private String id;

    public UndefinedReferenceError(IdentExpn identExpn) {
        offendingNode = identExpn;
        id = identExpn.getIdent();
    }

    @Override
    public String toString() {
        return super.toString() + String.format("undeclared reference: %s", id);
    }
}
