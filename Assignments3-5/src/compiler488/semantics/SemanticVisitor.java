package compiler488.semantics;

import com.sun.xml.internal.ws.wsdl.writer.document.ParamType;
import compiler488.ast.ASTList;
import compiler488.ast.InvalidASTException;
import compiler488.ast.Printable;
import compiler488.ast.Readable;
import compiler488.ast.decl.*;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.ast.type.IntegerType;
import compiler488.symbol.SymbolAttributes;
import compiler488.symbol.SymbolTable;
import compiler488.symbol.td.*;
import compiler488.visitor.DeclarationVisitor;
import compiler488.visitor.ExpressionVisitor;
import compiler488.visitor.StatementVisitor;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by gg on 01/03/17.
 */
public class SemanticVisitor implements DeclarationVisitor, ExpressionVisitor, StatementVisitor {

    /*
     *  INSTANCE VARIABLES:
     *  This SemanticVisitor's members.
     */

    private SymbolTable symbolTable;
    private List<SemanticError> semanticErrors;

    // the top of this stack is used to keep track of the current loop
    // nesting depth in the innermost major scope
    private Stack<MajorScopeInfo> majorScopeInfoStack;

    private OnVisitScopeListener visitScopeListener;

    /*
     *  CONSTRUCTORS:
     */

    public SemanticVisitor() {
        symbolTable = new SymbolTable();
        semanticErrors = new LinkedList<>();
        majorScopeInfoStack = new Stack<>();
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
                && arithExpn.getRight().evalType().equals(ExpnEvalType.INTEGER))) {
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

        if (!(boolExpn.getLeft().evalType().equals(ExpnEvalType.BOOLEAN)
                && boolExpn.getRight().evalType().equals(ExpnEvalType.BOOLEAN))) {
            semanticErrors.add(new TypeError(boolExpn));
        }
        boolExpn.setEvalType(ExpnEvalType.BOOLEAN);
    }

    @Override
    public void visit(CompareExpn compareExpn) {
        compareExpn.getLeft().accept(this);
        compareExpn.getRight().accept(this);
        if (compareExpn.getLeft().evalType().equals(ExpnEvalType.INTEGER) &&
                compareExpn.getRight().evalType().equals(ExpnEvalType.INTEGER)) {
            compareExpn.setEvalType(ExpnEvalType.BOOLEAN);
        } else {
            semanticErrors.add(new TypeError(compareExpn));
            compareExpn.setEvalType(ExpnEvalType.BOOLEAN);
        }

    }

    @Override
    public void visit(ConditionalExpn conditionalExpn) {
        conditionalExpn.getCondition().accept(this);
        conditionalExpn.getTrueValue().accept(this);
        conditionalExpn.getFalseValue().accept(this);

        if (!conditionalExpn.getCondition().evalType().equals(ExpnEvalType.BOOLEAN)) {
            semanticErrors.add(new TypeError(conditionalExpn));
        }
        if (conditionalExpn.getTrueValue().evalType().equals(conditionalExpn.getFalseValue().evalType())) {
            conditionalExpn.setEvalType(conditionalExpn.getTrueValue().evalType());
        } else {
            semanticErrors.add(new TypeError(conditionalExpn, conditionalExpn.getFalseValue(),
                    conditionalExpn.getTrueValue()));
            conditionalExpn.setEvalType(ExpnEvalType.UNDEFINED);
        }

    }

    @Override
    public void visit(EqualsExpn equalsExpn) {
        equalsExpn.getLeft().accept(this);
        equalsExpn.getRight().accept(this);

        if (equalsExpn.getLeft().evalType().equals(equalsExpn.getRight().evalType())) {
            equalsExpn.setEvalType(equalsExpn.getLeft().evalType());
        } else {
            semanticErrors.add(new TypeError(equalsExpn));
            equalsExpn.setEvalType(ExpnEvalType.UNDEFINED);
        }

    }

    @Override
    public void visit(FunctionCallExpn funcExpn) {
        List<ScalarTypeDescriptor> paramTypes = null;

        try {
            SymbolAttributes attrs = symbolTable.retrieveSymbol(funcExpn.getIdent());
            if (attrs.typeDescriptor instanceof FunctionTypeDescriptor) {
                FunctionTypeDescriptor fnTD = ((FunctionTypeDescriptor) attrs.typeDescriptor);
                paramTypes = fnTD.parameterTypes;
                List<ExpnEvalType> evalTypes = evalTypes(funcExpn.getArguments());

                if (fnTD.returnType instanceof IntegerTypeDescriptor) {
                    funcExpn.setEvalType(ExpnEvalType.INTEGER);
                } else {
                    funcExpn.setEvalType(ExpnEvalType.BOOLEAN);
                }

                verifyParamTypes(paramTypes, evalTypes);
            }
        } catch (SymbolTable.SymbolNotFoundException e) {
            semanticErrors.add(new UndefinedReferenceError(funcExpn));
            funcExpn.setEvalType(ExpnEvalType.UNDEFINED);
        } catch (ParamTypeMismatchException e) {
            semanticErrors.add(new TypeError(funcExpn));
        } catch (ParamArityMismatchException e) {
            semanticErrors.add(new RoutineCallError(funcExpn, paramTypes.size()));
        }
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

        if (!(attrs.typeDescriptor instanceof ScalarTypeDescriptor)) {
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

        // Visit child expression
        unaryMinusExpn.getOperand().accept(this);

        // Check for valid type of child expression
        if (unaryMinusExpn.getOperand().evalType() != ExpnEvalType.INTEGER) {
            semanticErrors.add(new TypeError(unaryMinusExpn));
        }

        // Set type to integer.
        unaryMinusExpn.setEvalType(ExpnEvalType.INTEGER);
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

        } catch (SymbolTable.SymbolNotFoundException ignored) {
        }
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
        if (majorScopeInfoStack.peek().getCurrentLoopDepth() < exitStmt.getLevel()) {
            semanticErrors.add(new ExitLevelError(exitStmt, majorScopeInfoStack.peek().getCurrentLoopDepth()));
        }

        if (exitStmt.getExpn() != null) {
            exitStmt.getExpn().accept(this);
            if (!exitStmt.getExpn().evalType().equals(ExpnEvalType.BOOLEAN)) {
                semanticErrors.add(new TypeError(exitStmt));
            }
        }
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
        List<ScalarTypeDescriptor> paramTypes = null;

        try {
            SymbolAttributes attrs = symbolTable.retrieveSymbol(procCall.getName());
            if (attrs.typeDescriptor instanceof ProcedureTypeDescriptor) {
                paramTypes = ((ProcedureTypeDescriptor) attrs.typeDescriptor).parameterTypes;
                List<ExpnEvalType> evalTypes = evalTypes(procCall.getArguments());

                verifyParamTypes(paramTypes, evalTypes);
            }
        } catch (SymbolTable.SymbolNotFoundException e) {
            semanticErrors.add(new UndefinedReferenceError(procCall));
        } catch (ParamTypeMismatchException e) {
            semanticErrors.add(new TypeError(procCall));
        } catch (ParamArityMismatchException e) {
            semanticErrors.add(new RoutineCallError(procCall, paramTypes.size()));
        }
    }

    @Override
    public void visit(Program programScope) {
        majorScopeInfoStack.push(new MajorScopeInfo(MajorScopeInfo.ScopeType.PROGRAM));
        visit((Scope) programScope);
        majorScopeInfoStack.pop();
    }

    @Override
    public void visit(ReadStmt readStmt) {
        for (Readable r : readStmt.getInputs()) {
            if (r instanceof IdentExpn) {
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
        repeatUntilStmt.getExpn().accept(this);

        majorScopeInfoStack.peek().incrementCurrentLoopDepth();

        repeatUntilStmt.getBody().accept(this);

        majorScopeInfoStack.peek().decrementCurrentLoopDepth();

        if (repeatUntilStmt.getExpn().evalType() == ExpnEvalType.INTEGER) {
            semanticErrors.add(new TypeError(repeatUntilStmt));
        }
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        if (majorScopeInfoStack.peek().getScopeType() == MajorScopeInfo.ScopeType.PROGRAM) {
            semanticErrors.add(new ReturnError(returnStmt, false));
        }

        // Procedure return
        if (returnStmt.getValue() == null) {
            if (majorScopeInfoStack.peek().getScopeType() == MajorScopeInfo.ScopeType.FUNCTION) {
                semanticErrors.add(new ReturnError(returnStmt));
            }
        } // Function return
        else {
            returnStmt.getValue().accept(this);

            if (majorScopeInfoStack.peek().getScopeType() == MajorScopeInfo.ScopeType.PROCEDURE) {
                semanticErrors.add(new ReturnError(returnStmt));
            }

            if (returnStmt.getValue().evalType() != majorScopeInfoStack.peek().getReturnType()) {
                semanticErrors.add(new TypeError(returnStmt,
                        majorScopeInfoStack.peek().getReturnType(), returnStmt.getValue().evalType()));
            }
        }
    }

    @Override
    public void visit(Scope scope) {
        symbolTable.openScope();

        consumeScopeVisitHook();

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
        whileStmt.getExpn().accept(this);

        if (whileStmt.getExpn().evalType() == ExpnEvalType.INTEGER) {
            semanticErrors.add(new TypeError(whileStmt));
        }

        majorScopeInfoStack.peek().incrementCurrentLoopDepth();

        whileStmt.getBody().accept(this);

        majorScopeInfoStack.peek().decrementCurrentLoopDepth();
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

                // check that array bounds are valid
                if (declPart instanceof ArrayDeclPart) {
                    ArrayDeclPart arrayDeclPart = (ArrayDeclPart) declPart;
                    if (arrayDeclPart.getLowerBoundary() > arrayDeclPart.getUpperBoundary()) {
                        semanticErrors.add(new ArrayDeclError(arrayDeclPart));
                    }
                }
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
        if (routineDecl.getType() == null) {
            majorScopeInfoStack.push(
                    new MajorScopeInfo(MajorScopeInfo.ScopeType.PROCEDURE));
        } else {
            ExpnEvalType type = null;
            if (routineDecl.getType() instanceof IntegerType) {
                type = ExpnEvalType.INTEGER;
            } else {
                type = ExpnEvalType.BOOLEAN;
            }
            majorScopeInfoStack.push(
                    new MajorScopeInfo(MajorScopeInfo.ScopeType.FUNCTION, type));
        }

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
        majorScopeInfoStack.pop();
    }


    private void verifyParamTypes(List<ScalarTypeDescriptor> declaredParamTypes,
                                  List<ExpnEvalType> evalParamTypes)
            throws ParamTypeMismatchException, ParamArityMismatchException {
        if (declaredParamTypes.size() != evalParamTypes.size()) {
            throw new ParamArityMismatchException();
        } else {
            for (int i = 0; i < declaredParamTypes.size(); i++) {
                if (!typesMatch(declaredParamTypes.get(i), evalParamTypes.get(i))) {
                    throw new ParamTypeMismatchException();
                }
            }
        }
    }

    private List<ExpnEvalType> evalTypes(ASTList<Expn> expns) {
        return expns.stream()
                .map(Expn::evalType)
                .collect(Collectors.toList());
    }

    private boolean typesMatch(ScalarTypeDescriptor td, ExpnEvalType evalType) {
        return (td instanceof IntegerTypeDescriptor && evalType.equals(ExpnEvalType.INTEGER)
                || td instanceof BooleanTypeDescriptor && evalType.equals(ExpnEvalType.BOOLEAN));
    }


    /**
     * Listener used to hook visit(Scope) to perform tasks after a new scope
     * has been opened in the symbol table, but before any of the scope body
     * has been processed.
     * <p>
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

    private class ParamArityMismatchException extends Exception {
    }

    private class ParamTypeMismatchException extends Exception {
    }
}
