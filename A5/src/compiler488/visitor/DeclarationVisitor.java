package compiler488.visitor;

import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;

/**
 * Created by gg on 01/03/17.
 */
public interface DeclarationVisitor {

    void visit(Declaration decl);

    void visit(MultiDeclarations multiDecl);

    void visit(RoutineDecl routineDecl);
}
