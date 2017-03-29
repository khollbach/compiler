package compiler488.codegen;

import static compiler488.runtime.Machine.*;

import compiler488.ast.InvalidASTException;
import compiler488.ast.Printable;
import compiler488.ast.Readable;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.codegen.table.RoutineTable;
import compiler488.codegen.table.VariableTable;
import compiler488.runtime.Machine;
import compiler488.visitor.DeclarationVisitor;
import compiler488.visitor.ExpressionVisitor;
import compiler488.visitor.StatementVisitor;

/**
 * Class to perform code generation on the AST after it passes semantic checks.
 */
public class CodeGenVisitor implements DeclarationVisitor, ExpressionVisitor, StatementVisitor {

    private CodeGenerator codeGen;
    
    private OnVisitScopeListener visitScopeListener;

    private VariableTable varTable;

    private RoutineTable routineTable;

    // id used for the print string routine in the routine table
    private static final String PRINT_STRING_ID = "__print_str__";

    /*
     *  CONSTRUCTORS:
     */

    public CodeGenVisitor() {
        codeGen = new CodeGenerator();
        varTable = new VariableTable();
        routineTable = new RoutineTable();
    }

    /*
     * VISITOR IMPLEMENTATION:
     */

    /* *********************
     * PROGRAM AND SCOPE
     * *********************/

    private void openScope(boolean isMajor){
        varTable.openScope(isMajor);
        routineTable.openScope();
    }

    private void closeScope(){
        varTable.closeScope();
        routineTable.closeScope();
    }

    @Override
    public void visit(Program program) {
        // add the print string routine to the main program scope
        setOnVisitScopeListener(() -> {
            genPrintStringProcedure();
            // set PC to the first instruction of the program scope
            Machine.setPC(codeGen.getNextInstrAddr());
        });

        // do codegen on the main program scope
        visit((Scope) program);

        // terminate execution with a HALT
        codeGen.genCode(
                HALT
        );

        // set MSP to the first word after the program code
        Machine.setMSP(codeGen.getNextInstrAddr());
        // set MLP to the last word before the constant pool
        Machine.setMLP(codeGen.getNextConstPoolAddr());
    }

    @Override
    public void visit(Scope scope) {
        openScope(scope.isMajor());

        consumeScopeVisitHook();
        short paramAllocationSize = 0;
        short addrAllocationSize = 0;
        if(scope.isMajor()){
            paramAllocationSize = varTable.getAllocationSize();
            addrAllocationSize = (short) (codeGen.getNextInstrAddr() + 3);
            codeGen.genCode(
                    PUSH, (short) 0,
                    PUSH, UNDEFINED, // <-- addrAllocationSize points here, will be patched in
                    DUPN
            );
        }

        for (Declaration d : scope.getDeclarations()) {
            visit(d);
        }
        scope.setAllocationSize(varTable.getAllocationSize());
        for(Stmt s : scope.getStatements()) {
            visit(s);
            if (s instanceof Scope){
                // keep updating allocation size of top level scope
                scope.setAllocationSize((short) Math.max(scope.getAllocationSize(),
                        ((Scope) s).getAllocationSize()));
            }
        }
        if(scope.isMajor()){
            // allocSize is (scope size) - (parameter size), i.e. number of variables on the stack
            short allocSize  = (short) (scope.getAllocationSize() - paramAllocationSize);
            // patching in the allocation size we need for variables in this scope
            codeGen.patchCode(addrAllocationSize, allocSize);
            // code to close the scope
            codeGen.genCode(
                    PUSH, allocSize, // push allocSize = number of variables to be removed
                    POPN
            );
        }

        closeScope();
    }

    /* *********** */
    /* EXPRESSIONS */
    /* *********** */

    @Override
    public void visit(Expn expn) {
        throw new InvalidASTException("Raw Expn type in AST");
    }

    @Override
    public void visit(ArithExpn arithExpn) {
        short OPCODE;
        switch (arithExpn.getOpSymbol()) {
            case "+":
                OPCODE = ADD;
                break;
            case "-":
                OPCODE = SUB;
                break;
            case "*":
                OPCODE = MUL;
                break;
            case "/":
                OPCODE = DIV;
                break;
            default:
                throw new InvalidASTException("Invalid arithmetic operation symbol: " + arithExpn.getOpSymbol());
        }

        arithExpn.getLeft().accept(this);
        arithExpn.getRight().accept(this);
        codeGen.genCode(
                OPCODE
        );
    }

    @Override
    public void visit(BoolConstExpn boolConst) {
        short BOOL_CONST = boolConst.getValue() ? Machine.MACHINE_TRUE : Machine.MACHINE_FALSE;
        codeGen.genCode(
                PUSH, BOOL_CONST
        );
    }

    @Override
    public void visit(BoolExpn boolExpn) {
        String opSymbol = boolExpn.getOpSymbol();
        switch (opSymbol) {
            case "or":
                boolExpn.getLeft().accept(this);
                boolExpn.getRight().accept(this);
                codeGen.genCode(
                        OR
                );
                break;
            case "and":
                // DeMorgan's Law implementation of "and"
                // i.e. A && B = !(!A || !B)

                // eval & negate first expn
                boolExpn.getLeft().accept(this);
                codeGen.genCode(
                        PUSH, MACHINE_FALSE,
                        EQ
                );
                // eval & negate second expression,
                // then or & negate the results
                boolExpn.getRight().accept(this);
                codeGen.genCode(
                        PUSH, MACHINE_FALSE,
                        EQ,
                        OR,
                        PUSH, MACHINE_FALSE,
                        EQ
                );
                break;
            default:
                throw new InvalidASTException("Invalid boolean operation symbol: " + boolExpn.getOpSymbol());
        }
    }

    @Override
    public void visit(CompareExpn compareExpn) {
        compareExpn.getLeft().accept(this);
        compareExpn.getRight().accept(this);

        switch (compareExpn.getOpSymbol()) {
            case "<":
                codeGen.genCode(
                        LT
                );
                break;
            case ">":
                codeGen.genCode(
                        SWAP,
                        LT
                );
                break;
            case "<=":
                codeGen.genCode(
                        SWAP,
                        LT,
                        PUSH, MACHINE_FALSE,
                        EQ
                );
                break;
            case ">=":
                codeGen.genCode(
                        LT,
                        PUSH, MACHINE_FALSE,
                        EQ
                );
                break;
            default:
                throw new InvalidASTException("Invalid comparison operation symbol: " + compareExpn.getOpSymbol());
        }
    }

    @Override
    public void visit(ConditionalExpn conditionalExpn) {
        conditionalExpn.getCondition().accept(this);

        // store the location for the address to branch to when false
        short addrFalseBranch = (short) (codeGen.getNextInstrAddr() + 1);
        codeGen.genCode(
                PUSH, UNDEFINED, // <- addrFalseBranch points here
                BF
        );

        conditionalExpn.getTrueValue().accept(this);

        // store the location for the address to branch to when true
        short addrTrueBranch = (short) (codeGen.getNextInstrAddr() + 1);
        codeGen.genCode(
                PUSH, UNDEFINED, // <- addrTrueBranch points here
                BR
        );

        // define the address at addrBF
        codeGen.patchCode(addrFalseBranch, codeGen.getNextInstrAddr());

        conditionalExpn.getFalseValue().accept(this);

        // define the address at addrBR
        codeGen.patchCode(addrTrueBranch, codeGen.getNextInstrAddr());
    }

    @Override
    public void visit(EqualsExpn equalsExpn) {
        equalsExpn.getLeft().accept(this);
        equalsExpn.getRight().accept(this);

        switch (equalsExpn.getOpSymbol()) {
            case "=":
                codeGen.genCode(
                        EQ
                );
                break;
            case "not =":
                codeGen.genCode(
                        EQ,
                        PUSH, MACHINE_FALSE,
                        EQ
                );
                break;
            default:
                throw new InvalidASTException("Invalid equality operation symbol: " + equalsExpn.getOpSymbol());
        }
    }

    @Override
    public void visit(FunctionCallExpn funcCall) {
        short functionAddress = routineTable.getAddress(funcCall.getIdent());
        short functionLL = -1; // TODO: need to know function's LL

        short patchReturnAddress = (short)(codeGen.getNextInstrAddr() + 3);
        codeGen.genCode(
                PUSH, (short) 0,
                PUSH, UNDEFINED, // To be patched
                ADDR, functionLL, (short) 0
        );

        for (Expn arg : funcCall.getArguments()) {
            arg.accept(this);
        }

        codeGen.genCode(
                PUSH, functionAddress,
                BR
        );

        codeGen.patchCode(patchReturnAddress);

        throw new RuntimeException("NYI"); // TODO: see above.
    }

    @Override
    public void visit(IdentExpn identExpn) {
        VariableTable.Address var = varTable.getAddress(identExpn.getIdent());
        codeGen.genCode(
                ADDR, var.LL, var.ON,
                LOAD
        );
    }

    @Override
    public void visit(IntConstExpn intConstExpn) {
        Integer iVal = intConstExpn.getValue();
        if (iVal < Machine.MIN_INTEGER || iVal > Machine.MAX_INTEGER) {
            System.err.println("Integer constant value out of range for machine word.");
            System.err.println("Exiting now.");
            System.exit(1);
        }

        codeGen.genCode(
                PUSH, iVal.shortValue()
        );
    }

    @Override
    public void visit(NotExpn notExpn) {
        notExpn.getOperand().accept(this);
        codeGen.genCode(
                PUSH,MACHINE_FALSE,
                EQ
        );
    }

    @Override
    public void visit(SubsExpn subsExpn) {
        VariableTable.Address array = varTable.getAddress(subsExpn.getVariable());
        codeGen.genCode(
                ADDR, array.LL, array.ON
        );

        subsExpn.getOperand().accept(this);

        codeGen.genCode(
                PUSH, array.LOWER_BOUND,
                SUB,
                ADD,
                LOAD
        );
    }

    @Override
    public void visit(UnaryMinusExpn unaryMinusExpn) {
        unaryMinusExpn.getOperand().accept(this);
        codeGen.genCode(
                NEG
        );
    }

    /* ********** */
    /* STATEMENTS */
    /* ********** */

    @Override
    public void visit(Stmt stmt) {
        throw new InvalidASTException("Raw Stmt type in AST");
    }

    @Override
    public void visit(AssignStmt assignStmt) {
        // Load address of LHS.
        Expn lval = assignStmt.getLval();

        if (lval instanceof IdentExpn) {
            VariableTable.Address var = varTable.getAddress(((IdentExpn) lval).getIdent());
            codeGen.genCode(
                    ADDR, var.LL, var.ON
            );

        } else if (lval instanceof SubsExpn) {
            VariableTable.Address array = varTable.getAddress(((SubsExpn) lval).getVariable());
            codeGen.genCode(
                    ADDR, array.LL, array.ON
            );

            ((SubsExpn) lval).getOperand().accept(this);

            codeGen.genCode(
                    PUSH, array.LOWER_BOUND,
                    SUB,
                    ADD
            );

        } else {
            throw new InvalidASTException("Invalid LHS type in AssignStmt");
        }

        // Evaluate RHS
        assignStmt.getRval().accept(this);
        codeGen.genCode(
                STORE
        );
    }

    @Override
    public void visit(ExitStmt exitStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(IfStmt ifStmt) {
        ifStmt.getCondition().accept(this);


        // store the location for the address to branch to when false
        short addrFalseBranch = (short) (codeGen.getNextInstrAddr() + 1);
        codeGen.genCode(
                PUSH, UNDEFINED,
                BF
        );

        ifStmt.getWhenTrue().accept(this);

        // store the location for the address to branch to when true
        short addrTrueBranch = (short) (codeGen.getNextInstrAddr() + 1);
        codeGen.genCode(
                PUSH, UNDEFINED,
                BR
        );

        // Patch false branch address.
        codeGen.patchCode(addrFalseBranch, codeGen.getNextInstrAddr());

        ifStmt.getWhenFalse().accept(this);

        // Patch true branch address
        codeGen.patchCode(addrTrueBranch, codeGen.getNextInstrAddr());
    }

    @Override
    public void visit(ProcedureCallStmt procCall) {
        short procedureAddress = routineTable.getAddress(procCall.getName());
        short procedureLL = -1; // TODO: need to know procedure's LL

        short patchReturnAddress = (short)(codeGen.getNextInstrAddr() + 1);
        codeGen.genCode(
                PUSH, UNDEFINED, // To be patched
                ADDR, procedureLL, (short) 0
        );

        for (Expn arg : procCall.getArguments()) {
            arg.accept(this);
        }

        codeGen.genCode(
                PUSH, procedureAddress,
                BR
        );

        codeGen.patchCode(patchReturnAddress);

        throw new RuntimeException("NYI"); // TODO: see above.
    }

    @Override
    public void visit(ReadStmt readStmt) {
        for (Readable input : readStmt.getInputs()) {
            if (input instanceof IdentExpn) {
                VariableTable.Address var = varTable.getAddress(((IdentExpn) input).getIdent());
                codeGen.genCode(
                        ADDR, var.LL, var.ON,
                        READI,
                        STORE
                );

            } else if (input instanceof SubsExpn) {
                SubsExpn subsExpn = (SubsExpn) input;
                VariableTable.Address array = varTable.getAddress(subsExpn.getVariable());
                codeGen.genCode(
                        ADDR, array.LL, array.ON
                );

                subsExpn.getOperand().accept(this);

                codeGen.genCode(
                        PUSH, array.LOWER_BOUND,
                        SUB,
                        ADD,
                        READI,
                        STORE
                );

            } else {
                throw new InvalidASTException("Invalid input type in ReadStmt");
            }
        }
    }

    @Override
    public void visit(RepeatUntilStmt repeatUntilStmt) {
        // store the address to branch to if the loop termination
        // condition is not met
        short addrLoop = codeGen.getNextInstrAddr();

        repeatUntilStmt.getBody().accept(this);
        repeatUntilStmt.getExpn().accept(this);

        codeGen.genCode(
                PUSH, addrLoop,
                BF
        );
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(WhileDoStmt whileStmt) {
        // store the address to branch to if the loop termination
        // condition is not met
        short addrLoop = codeGen.getNextInstrAddr();

        whileStmt.getExpn().accept(this);

        // store the locaqtion of the address to branch to when the loop terminates
        short addrLoopTerm = (short) (codeGen.getNextInstrAddr() + 1);
        codeGen.genCode(
                PUSH, UNDEFINED, // <- addrLoopTerm points here
                BF
        );

        whileStmt.getBody().accept(this);

        codeGen.genCode(
                PUSH, addrLoop,
                BR
        );

        // Patch the address to branch to when the loop terminates
        codeGen.patchCode(addrLoopTerm, codeGen.getNextInstrAddr());
    }

    @Override
    public void visit(WriteStmt writeStmt) {
        for (Printable item : writeStmt.getOutputs()) {
            if (item instanceof SkipConstExpn) {
                codeGen.genCode(
                        PUSH, (short) '\n',
                        PRINTC
                );

            } else if (item instanceof TextConstExpn) {
                short strAddr = codeGen.genText(((TextConstExpn) item).getValue());
                short printStrAddr = routineTable.getAddress(PRINT_STRING_ID);

                // store location to put return address
                short retAddr = (short) (codeGen.getNextInstrAddr() + 1);
                codeGen.genCode(
                        PUSH, UNDEFINED, // <- retAddr points here
                        PUSH, strAddr,
                        PUSH, printStrAddr,
                        BR
                );
                // Patch return address
                codeGen.patchCode(retAddr, codeGen.getNextInstrAddr());

            } else if (item instanceof Expn) {
                ((Expn) item).accept(this);
                codeGen.genCode(
                        PRINTI
                );
            } else {
                throw new InvalidASTException("Unexpected type in WriteStatement outputs");
            }
        }
    }

    /* *********** */
    /* DECLARATIONS */
    /* *********** */

    @Override
    public void visit(Declaration decl) {
        throw new InvalidASTException("Raw Declaration type in AST");
    }

    @Override
    public void visit(MultiDeclarations multiDecl) {
    	for (DeclarationPart declPart: multiDecl.getElements()){
    		varTable.createEntry(declPart);
    	}
    }

    @Override
    public void visit(RoutineDecl routineDecl) {
    	
    	setOnVisitScopeListener(() -> {
    		for (ScalarDecl param: routineDecl.getRoutineBody().getParameters()){
    			varTable.createEntry(param);
    		}
    	});
    	routineDecl.getRoutineBody().getBody().accept(this);
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

    public interface OnVisitScopeListener {

        /**
         * Executed after a new scope has been opened in the symbol table, but
         * before any of the scope body has been processed.
         */
        void onVisitScope();
    }

    /**
     * Generate code for the print-string procedure.
     */
    private void genPrintStringProcedure() {
        // code to branch around body is not necessary, since
        // the print string procedure occurs outside of the
        // main program's code

        // Remember the starting address.
        routineTable.createEntry(PRINT_STRING_ID, codeGen.getNextInstrAddr());

        short addrBR = codeGen.getNextInstrAddr();
        short addrBF = (short) (codeGen.getNextInstrAddr() + 10);
        codeGen.genCode(
                DUP,                    // <- addrBR points here
                LOAD,                   // load next char
                DUP,
                PUSH, (short) 0,
                EQ,
                PUSH, MACHINE_FALSE,
                EQ,                     // check that next char is not null
                PUSH, UNDEFINED,        // <- addrBF points here
                BF,                     // Exit loop if char is null
                PRINTC,                 // Print char
                PUSH, (short) 1,
                ADD,                    // Add 1 to string address
                PUSH, addrBR,
                BR                      // Go to start of loop
        );
        // patch *addrBF to branch to the first instruction after the loop
        codeGen.patchCode(addrBF, codeGen.getNextInstrAddr());

        // epilogue
        codeGen.genCode(
                POP,
                POP,                    // pop char and string address
                BR                      // return
        );
    }

}
