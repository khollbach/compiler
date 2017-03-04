package compiler488.semantics;

import compiler488.ast.InvalidASTException;
import compiler488.ast.Printable;
import compiler488.ast.Readable;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.symbol.SymbolAttributes;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.td.*;
import compiler488.visitor.DeclarationVisitor;
import compiler488.visitor.ExpressionVisitor;
import compiler488.visitor.StatementVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by gg on 01/03/17.
 */
public class SemanticVisitor implements DeclarationVisitor, ExpressionVisitor, StatementVisitor{

    /*
     *  INSTANCE VARIABLES:
     *  This SemanticVisitor's members.
     */

    private SymbolTable symbolTable;
    private List<SemanticError> semanticErrors;

    // the top of this stack is used to keep track of the current loop
    // nesting depth in the innermost major scope
    private Stack<Integer> majScopeLoopNestingDepths;

    private OnVisitScopeListener visitScopeListener;

    /*
     *  CONSTRUCTORS:
     */

    public SemanticVisitor() {
        symbolTable = new SymbolTable();
        semanticErrors = new LinkedList<>();
        majScopeLoopNestingDepths = new Stack<>();
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
        arithExpn.getLeft().accept(this);
        arithExpn.getRight().accept(this);

        if (!(arithExpn.getLeft().evalType().equals(ExpnEvalType.INTEGER)
                && arithExpn.getRight().evalType().equals(ExpnEvalType.INTEGER))){
            semanticErrors.add(new TypeError(arithExpn));
        }

        arithExpn.setEvalType(ExpnEvalType.INTEGER);
    }

    @Override
    public void visit(BoolConstExpn boolConst) {
        boolConst.setEvalType(ExpnEvalType.BOOLEAN);
    }

    @Override
    public void visit(BoolExpn boolExpn) {
        boolExpn.getLeft().accept(this);
        boolExpn.getRight().accept(this);

        if(!(boolExpn.getLeft().evalType().equals(ExpnEvalType.BOOLEAN)
                && boolExpn.getRight().evalType().equals(ExpnEvalType.BOOLEAN))){
            semanticErrors.add(new TypeError(boolExpn));
        }
        boolExpn.setEvalType(ExpnEvalType.BOOLEAN);
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
        SymbolAttributes attrs;
        try {
            attrs = symbolTable.retrieveSymbol(identExpn.getIdent());
        } catch (SymbolTable.SymbolNotFoundException e) {
            semanticErrors.add(new UndefinedReferenceError(identExpn));
            identExpn.setEvalType(ExpnEvalType.UNDEFINED);
            return;
        }

        if(!(attrs.typeDescriptor instanceof ScalarTypeDescriptor)){
            // corner case for poor language design:
            // functions without params
            if (attrs.typeDescriptor instanceof FunctionTypeDescriptor
                    && ((FunctionTypeDescriptor) attrs.typeDescriptor).parameterTypes.isEmpty()) {
                if (((FunctionTypeDescriptor) attrs.typeDescriptor).returnType instanceof IntegerTypeDescriptor) {
                    identExpn.setEvalType(ExpnEvalType.INTEGER);
                } else {
                    identExpn.setEvalType(ExpnEvalType.BOOLEAN);
                }
            } else {
                semanticErrors.add(new TypeError(identExpn));
                identExpn.setEvalType(ExpnEvalType.UNDEFINED);
            }
        } else if (attrs.typeDescriptor instanceof IntegerTypeDescriptor) {
            identExpn.setEvalType(ExpnEvalType.INTEGER);
        } else {
            identExpn.setEvalType(ExpnEvalType.BOOLEAN);
        }
    }

    @Override
    public void visit(IntConstExpn intConstExpn) {
        intConstExpn.setEvalType(ExpnEvalType.INTEGER);
    }

    @Override
    public void visit(NotExpn notExpn) {
        notExpn.getOperand().accept(this);
        notExpn.setEvalType(ExpnEvalType.BOOLEAN);
    }

    @Override
    public void visit(SubsExpn subsExpn) {

        SymbolAttributes attrs = null;
        try {
            attrs = symbolTable.retrieveSymbol(subsExpn.getVariable());
        } catch (SymbolTable.SymbolNotFoundException e) {
            // nonexistent reference, type of this expn unknown
            semanticErrors.add(new UndefinedReferenceError(subsExpn));
            subsExpn.setEvalType(ExpnEvalType.UNDEFINED);
        }

        Expn operand = subsExpn.getOperand();
        operand.accept(this);
        if (!(operand.evalType().equals(ExpnEvalType.INTEGER))) {
            // bad index type
            semanticErrors.add(new TypeError(subsExpn, subsExpn.getOperand()));
        }

        if (attrs != null) {
            if (attrs.typeDescriptor instanceof ArrayTypeDescriptor) {
                if (((ArrayTypeDescriptor) attrs.typeDescriptor)
                        .elementType instanceof IntegerTypeDescriptor) {
                    subsExpn.setEvalType(ExpnEvalType.INTEGER);
                } else {
                    subsExpn.setEvalType(ExpnEvalType.BOOLEAN);
                }
            } else {
                semanticErrors.add(new TypeError(subsExpn));
                subsExpn.setEvalType(ExpnEvalType.UNDEFINED);
            }
        }
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
        assignStmt.getLval().accept(this);
        assignStmt.getRval().accept(this);

        // Check special case of assigning to routine parameters
        SymbolAttributes attrs = null;
        try {
            attrs = symbolTable.retrieveSymbol(assignStmt.getLval().toString());

        } catch (SymbolTable.SymbolNotFoundException ignored) {}
        if (attrs != null) {
            if (attrs.isParameter) {
                semanticErrors.add(new TypeError(assignStmt, attrs.isParameter));
            }
        }

        // Check differing types on either side of assignment statement
        if (assignStmt.getLval().evalType() != assignStmt.getRval().evalType()) {
            semanticErrors.add(new TypeError(assignStmt));
        }

    }

    @Override
    public void visit(ExitStmt exitStmt) {

    }

    @Override
    public void visit(IfStmt ifStmt) {
        ifStmt.getCondition().accept(this);
        ifStmt.getWhenTrue().accept(this);
        ifStmt.getWhenFalse().accept(this);

        if (ifStmt.getCondition().evalType() != ExpnEvalType.BOOLEAN) {
            semanticErrors.add(new TypeError(ifStmt));
        }
    }

    @Override
    public void visit(ProcedureCallStmt procCall) {

    }

    @Override
    public void visit(Program programScope) {
        majScopeLoopNestingDepths.push(0);
        visit((Scope) programScope);
        majScopeLoopNestingDepths.pop();
    }

    @Override
    public void visit(ReadStmt readStmt) {
        for (Readable r : readStmt.getInputs()){
            if(r instanceof IdentExpn){
                IdentExpn rIdent = ((IdentExpn) r);
                rIdent.accept(this);
                if (!rIdent.evalType().equals(ExpnEvalType.INTEGER)) {
                    semanticErrors.add(new ReadStmtError(rIdent));
                }
            } else if (r instanceof SubsExpn) {
                SubsExpn sIdent = (SubsExpn) r;
                sIdent.accept(this);
                if (!sIdent.evalType().equals(ExpnEvalType.INTEGER)) {
                    semanticErrors.add(new ReadStmtError(sIdent));
                }
            }

        }

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

        for (Stmt stmt : scope.getStatements()) {
            stmt.accept(this);
        }

        symbolTable.closeScope();
    }
    
    private void setOnVisitScopeListener(OnVisitScopeListener listener) {
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
        for (Printable p : writeStmt.getOutputs()) {
            if (!(p instanceof TextConstExpn
                    || p instanceof SkipConstExpn)) {
                Expn expnP = (Expn) p;
                expnP.accept(this);
                if (!expnP.evalType().equals(ExpnEvalType.INTEGER)) {
                    semanticErrors.add(new WriteStmtError(expnP));
                }
            }
        }
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
                semanticErrors.add(new LocalRedeclarationError(declPart));
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

        // new major scope
        majScopeLoopNestingDepths.push(0);

        // hook the declaration of parameters when processing routine scope
        setOnVisitScopeListener(() -> {
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

        // major scope processed
        majScopeLoopNestingDepths.pop();
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
