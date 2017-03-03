package compiler488.semantics;

import compiler488.ast.AST;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;

/**
 * Created by gg on 02/03/17.
 */
public class LocalRedeclarationError extends SemanticError {

    public LocalRedeclarationError(MultiDeclarations multiDecl, DeclarationPart declPart) {
        super();
    }

    public LocalRedeclarationError(RoutineDecl routineDecl) {
        super();
    }

    public LocalRedeclarationError(ScalarDecl paramDecl) {
        super();
    }

    //TODO:
    //  - Constructors, error message, etc...

    @Override
    protected AST getOffendingNode() {
        return null;
    }
}
