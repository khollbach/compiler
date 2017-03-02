package compiler488.semantics;

import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDeclPart;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.ast.type.Type;
import compiler488.visitor.DeclarationVisitor;
import compiler488.visitor.ExpressionVisitor;
import compiler488.visitor.StatementVisitor;

/**
 * Created by gg on 01/03/17.
 */
public class SemanticVisitor implements DeclarationVisitor, ExpressionVisitor, StatementVisitor{
    /* *********** */
    /* EXPRESSIONS */
    /* *********** */

    @Override
    public void visit(ArithExpn arithExpn) {

    }

    @Override
    public void visit(BoolConstExpn boolConst) {

    }

    @Override
    public void visit(BoolExpn boolExpn) {

    }

    @Override
    public void visit(CompareExpn compareExpn) {

    }

    @Override
    public void visit(ConditionalExpn conditionalExpn) {

    }

    @Override
    public void visit(EqualsExpn equalsExpn) {

    }

    @Override
    public void visit(FunctionCallExpn funcExpn) {

    }

    @Override
    public void visit(IdentExpn identExpn) {

    }

    @Override
    public void visit(IntConstExpn intConstExpn) {

    }

    @Override
    public void visit(NotExpn notExpn) {

    }

    @Override
    public void visit(SkipConstExpn newlineExpn) {

    }

    @Override
    public void visit(SubsExpn subsExpn) {

    }

    @Override
    public void visit(TextConstExpn textExpn) {

    }

    @Override
    public void visit(UnaryMinusExpn unaryMinusExpn) {

    }
    /* ********** */
    /* STATEMENTS */
    /* ********** */

    @Override
    public void visit(AssignStmt assignStmt) {

    }

    @Override
    public void visit(ExitStmt exitStmt) {

    }

    @Override
    public void visit(IfStmt ifStmt) {

    }

    @Override
    public void visit(ProcedureCallStmt procCall) {

    }

    @Override
    public void visit(Program programScope) {

    }

    @Override
    public void visit(ReadStmt readStmt) {

    }

    @Override
    public void visit(RepeatUntilStmt repeatUntilStmt) {

    }

    @Override
    public void visit(ReturnStmt returnStmt) {

    }

    @Override
    public void visit(Scope scope) {

    }

    @Override
    public void visit(WhileDoStmt whileStmt) {

    }

    @Override
    public void visit(WriteStmt writeStmt) {

    }

    @Override
    public void visit(MultiDeclarations multiDecl) {

    }

    @Override
    public void visit(RoutineDecl routineDecl) {

    }

    @Override
    public void visit(ArrayDeclPart arrayPart, Type t) {

    }

    @Override
    public void visit(ScalarDeclPart scalarPart, Type t) {

    }

    // TODO:
    //  - Type Visitor to buld type info for Expns and Decls (implement expn visitor and decl visitor)
    //  - Implement all 3 visitors
}
