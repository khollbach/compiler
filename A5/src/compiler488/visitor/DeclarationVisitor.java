package compiler488.visitor;

import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;

/**
 * Created by gg on 01/03/17.
 */
public interface DeclarationVisitor {

    // TODO think more about how to design the visitor pattern for Declarations
    // need to take care of marking function parameters, symbol table entries, etc
    // should code from typedescriptorfactory be migrated to a new visitor?

    void visit(Declaration decl);

    void visit(MultiDeclarations multiDecl);

    void visit(RoutineDecl routineDecl);
}
