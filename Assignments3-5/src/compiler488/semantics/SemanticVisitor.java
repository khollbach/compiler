package compiler488.semantics;

import compiler488.ast.InvalidASTException;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.symbol.SymbolAttributes;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.td.TypeDescriptor;
import compiler488.symbol.td.TypeDescriptorFactory;
import compiler488.visitor.DeclarationVisitor;
import compiler488.visitor.ExpressionVisitor;
import compiler488.visitor.StatementVisitor;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by gg on 01/03/17.
 */
public class SemanticVisitor implements DeclarationVisitor, ExpressionVisitor, StatementVisitor{

    private SymbolTable symbolTable;
    private List<SemanticError> semanticErrors;

    private OnVisitScopeListener visitScopeListener;

    public SemanticVisitor() {
        symbolTable = new SymbolTable();
        semanticErrors = new LinkedList<>();
    }

    /* *********** */
    /* EXPRESSIONS */
    /* *********** */

    @Override
    public void visit(Expn expn) {

    }

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
    public void visit(Stmt stmt) {
        throw new InvalidASTException();
    }

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
        symbolTable.openScope();

        consumeScopeVisitHook();

        //TODO rest of scope processing
        for (Declaration decl : scope.getDeclarations()) {
            decl.accept(this);
        }

        symbolTable.closeScope();
    }

    private void setVisitScopeListener(OnVisitScopeListener listener) {
        this.visitScopeListener = listener;
    }

    private void consumeScopeVisitHook() {
        // run hook once then clear the listener
        if (visitScopeListener != null) {
            visitScopeListener.onVisitScope();
            visitScopeListener = null;
        }
    }

    @Override
    public void visit(WhileDoStmt whileStmt) {

    }

    @Override
    public void visit(WriteStmt writeStmt) {

    }

    @Override
    public void visit(Declaration decl) {
        throw new InvalidASTException();
    }

    @Override
    public void visit(MultiDeclarations multiDecl) {
        // enter each declPart into the symbol table
        for (DeclarationPart declPart : multiDecl.getElements()) {
            TypeDescriptor descriptor =
                    TypeDescriptorFactory.create(declPart, multiDecl.getType());
            SymbolAttributes attributes =
                    new SymbolAttributes(false, descriptor);
            try {
                symbolTable.enterSymbol(declPart.getName(), attributes);
            } catch (SymbolTable.RedeclarationException e) {
                semanticErrors.add(new LocalRedeclarationError(multiDecl, declPart));
            }
        }
    }

    @Override
    public void visit(RoutineDecl routineDecl) {
        // enter the routine id into the symbol table
        TypeDescriptor routineDescriptor =
                TypeDescriptorFactory.create(routineDecl);
        SymbolAttributes routineAttributes = new
                SymbolAttributes(false, routineDescriptor);

        try {
            symbolTable.enterSymbol(routineDecl.getName(), routineAttributes);
        } catch (SymbolTable.RedeclarationException e) {
            semanticErrors.add(new LocalRedeclarationError(routineDecl));
        }

        // hook the declaration of parameters when processing routine scope
        setVisitScopeListener(() -> {
            // add the parameter id's in the function scope
            for (ScalarDecl paramDecl :
                    routineDecl.getRoutineBody().getParameters()) {
                TypeDescriptor paramDescriptor =
                        TypeDescriptorFactory.create(paramDecl);
                SymbolAttributes paramAttributes =
                        new SymbolAttributes(true, paramDescriptor);

                try {
                    symbolTable.enterSymbol(
                            paramDecl.getName(),
                            paramAttributes);
                } catch (SymbolTable.RedeclarationException e) {
                    // two parameters were declared with the same id
                    semanticErrors.add(new LocalRedeclarationError(paramDecl));
                }
            }
        });

        // process the routine scope
        routineDecl.getRoutineBody().getBody().accept(this);
    }


    /**
     * Listener used to hook visit(Scope) to perform tasks after a new scope
     * has been opened in the symbol table, but before any of the scope body
     * has been processed.
     *
     * This interface was created to facilitate adding routine parameters to
     * the symbol table without writing an entirely new function to process
     * routine scopes.
     */
    public interface OnVisitScopeListener {

        /**
         * Executed after a new scope has been opened in the symbol table, but
         * before any of the scope body has been processed.
         */
        void onVisitScope();
    }

    // TODO:
    //  - Type Visitor to buld type info for Expns and Decls (implement expn visitor and decl visitor)
    //  - Implement all 3 visitors
    //  - record semantic analysis operations with the trace function in Semantics
}
