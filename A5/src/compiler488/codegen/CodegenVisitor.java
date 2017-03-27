package compiler488.codegen;

import compiler488.ast.InvalidASTException;
import compiler488.ast.decl.Declaration;
import compiler488.ast.decl.MultiDeclarations;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;
import compiler488.visitor.DeclarationVisitor;
import compiler488.visitor.ExpressionVisitor;
import compiler488.visitor.StatementVisitor;

public class CodegenVisitor implements DeclarationVisitor, ExpressionVisitor, StatementVisitor {

    /*
     *  INSTANCE VARIABLES:
     */

    /**
     * The index (machine memory address) of the next instruction to be generated.
     */
    private short next_instruction_addr;

    /**
     * The location of the print-string procedure in machine memory.
     */
    private short print_string_procedure_addr;

    /*
     *  CONSTRUCTORS:
     */

    public CodegenVisitor() {
        next_instruction_addr = 0;
        print_string_procedure_addr = 0;
    }

    /*
     * PUBLIC METHODS:
     */

    /**
     * Perform code generation.
     */
    public void doCodeGen(Program programAST) {
        // Recursively perform code generation on the AST.
        programAST.accept(this);

        // Initialize machine program counter, stack pointer, "stack upper bound" (MLP) pointer.
        Machine.setPC((short) 0);
        Machine.setMSP(next_instruction_addr);
        Machine.setMLP((short) (Machine.memorySize - 1));
    }

    /*
     * VISITOR IMPLEMENTATION:
     */

    /* *********************
     * PROGRAM AND SCOPE
     * *********************/

    @Override
    public void visit(Program programScope) {
        // Generate print-string procedure; remember its address.
        print_string_procedure_addr = generatePrintStringProcedure();

        visit((Scope) programScope);

        // Add a HALT as the final instruction.
        writeMemory(next_instruction_addr++, Machine.HALT);
    }

    @Override
    public void visit(Scope scope) {
        throw new RuntimeException("NYI");
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
        short opcode;
        switch (arithExpn.getOpSymbol()) {
            case "+":
                opcode = Machine.ADD;
                break;
            case "-":
                opcode = Machine.SUB;
                break;
            case "*":
                opcode = Machine.MUL;
                break;
            case "/":
                opcode = Machine.DIV;
                break;
            default:
                throw new InvalidASTException("Invalid arithmetic operation symbol: " + arithExpn.getOpSymbol());
        }

        visit(arithExpn.getLeft());
        visit(arithExpn.getRight());
        writeMemory(next_instruction_addr++, opcode);
    }

    @Override
    public void visit(BoolConstExpn boolConst) {
        short constVal = boolConst.getValue() ? Machine.MACHINE_TRUE : Machine.MACHINE_FALSE;
        writeMemory(next_instruction_addr++, Machine.PUSH);
        writeMemory(next_instruction_addr++, constVal);
    }

    @Override
    public void visit(BoolExpn boolExpn) {
        String opSymbol = boolExpn.getOpSymbol();
        if (opSymbol.equals("or")) {
            visit(boolExpn.getLeft());
            visit(boolExpn.getRight());
            writeMemory(next_instruction_addr++, Machine.OR);
        } else if (opSymbol.equals("and")) {
            visit(boolExpn.getLeft());
            writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
            writeMemory(next_instruction_addr++, Machine.EQ);

            visit(boolExpn.getRight());
            writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
            writeMemory(next_instruction_addr++, Machine.EQ);

            writeMemory(next_instruction_addr++, Machine.OR);

            writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
            writeMemory(next_instruction_addr++, Machine.EQ);
        } else {
            throw new InvalidASTException("Invalid boolean operation symbol: " + boolExpn.getOpSymbol());
        }
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
        int intValue = intConstExpn.getValue().intValue();
        if (intValue < Machine.MIN_INTEGER || intValue > Machine.MAX_INTEGER) {
            System.err.println("Integer constant value out of range for machine word.");
            System.err.println("Exiting now.");
            System.exit(1);
        }
        writeMemory(next_instruction_addr++, Machine.PUSH);
        writeMemory(next_instruction_addr++, (short) intValue);
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
        throw new InvalidASTException("Raw Stmt type in AST");
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
    public void visit(WhileDoStmt whileStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(WriteStmt writeStmt) {
        throw new RuntimeException("NYI");
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
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(RoutineDecl routineDecl) {
        throw new RuntimeException("NYI");
    }

    /*
     * PRIVATE METHODS:
     */

    /**
     * Wrapper method to call Machine.writeMemory and rethrow any exceptions as (unchecked) RuntimeExceptions.
     */
    private void writeMemory(short addr, short val) {
        try {
            Machine.writeMemory(addr, val);
        } catch (MemoryAddressException e) {
            System.err.println("Generated code won't fit in machine memory. Exiting now.");
            System.exit(1);
        }
    }

    /**
     * Generate code for the print-string procedure. Returns the memory address of the start of the procedure code.
     */
    private short generatePrintStringProcedure() {
        throw new RuntimeException("NYI");
    }

}
