package compiler488.codegen;

import compiler488.ast.InvalidASTException;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.visitor.DeclarationVisitor;
import compiler488.visitor.ExpressionVisitor;
import compiler488.visitor.StatementVisitor;

public class CodegenVisitor implements DeclarationVisitor, ExpressionVisitor, StatementVisitor {

    /*
     *  CONSTRUCTORS:
     */

    public CodegenVisitor() {
    }


    /*
     * VISITOR IMPLEMENTATION:
     */

    /* *********** */
    /* EXPRESSIONS */
    /* *********** */

    @Override
    public void visit(Expn expn) {
        throw new InvalidASTException();
    }

    @Override
    public void visit(ArithExpn arithExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(BoolConstExpn boolConst) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(BoolExpn boolExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(CompareExpn compareExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(ConditionalExpn conditionalExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(EqualsExpn equalsExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(FunctionCallExpn funcExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(IdentExpn identExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(IntConstExpn intConstExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(NotExpn notExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(SubsExpn subsExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(UnaryMinusExpn unaryMinusExpn) {
        throw new RuntimeException("NYI");
    }

    /* ********** */
    /* STATEMENTS */
    /* ********** */

    @Override
    public void visit(Stmt stmt) {
        throw new InvalidASTException();
    }

    @Override
    public void visit(AssignStmt assignStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(ExitStmt exitStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(IfStmt ifStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(ProcedureCallStmt procCall) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(Program programScope) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(ReadStmt readStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(RepeatUntilStmt repeatUntilStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(Scope scope) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(WhileDoStmt whileStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(WriteStmt writeStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(Declaration decl) {
        throw new InvalidASTException();
    }

    @Override
    public void visit(MultiDeclarations multiDecl) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(RoutineDecl routineDecl) {
        throw new RuntimeException("NYI");
    }

}
