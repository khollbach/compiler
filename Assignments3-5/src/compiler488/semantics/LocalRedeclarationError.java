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

    private static final String PARAMETER_REDCL = "parameter \"%s\" declared multiple times";
    private static final String SYM_REDECL = "\"%s\" has already been declared in this scope";

    private String errorMsg;

    public LocalRedeclarationError(DeclarationPart declPart) {
        errorMsg = String.format(SYM_REDECL, declPart.getName());
        offendingNode = declPart
    }

    public LocalRedeclarationError(RoutineDecl routineDecl) {
        errorMsg = String.format(SYM_REDECL, routineDecl.getName());
        offendingNode = routineDecl;
    }

    public LocalRedeclarationError(ScalarDecl paramDecl) {
        errorMsg = String.format(PARAMETER_REDCL, paramDecl.getName());
        offendingNode = paramDecl;
    }

}
