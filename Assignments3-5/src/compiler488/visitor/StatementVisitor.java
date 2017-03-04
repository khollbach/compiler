package compiler488.visitor;

import compiler488.ast.decl.RoutineBody;
import compiler488.ast.stmt.*;

/**
 * Visitor interface for Statements
 * <p>
 * Created by gg on 01/03/17.
 */
public interface StatementVisitor {
    void visit(Stmt stmt);

    void visit(AssignStmt assignStmt);

    void visit(ExitStmt exitStmt);

    void visit(IfStmt ifStmt);

    void visit(ProcedureCallStmt procCall);

    void visit(Program programScope);

    void visit(ReadStmt readStmt);

    void visit(RepeatUntilStmt repeatUntilStmt);

    void visit(ReturnStmt returnStmt);

    void visit(Scope scope);

    void visit(WhileDoStmt whileStmt);

    void visit(WriteStmt writeStmt);
}
