package compiler488.codegen;

import compiler488.ast.InvalidASTException;
import compiler488.ast.Printable;
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
     * The index (machine memory address) of the most recently allocated text constant.
     */
    private short most_recent_textconst_addr;

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
        most_recent_textconst_addr = Machine.memorySize;
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
        // Generate print-string procedure.
        generatePrintStringProcedure();

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
        switch (opSymbol) {
            case "or":
                visit(boolExpn.getLeft());
                visit(boolExpn.getRight());
                writeMemory(next_instruction_addr++, Machine.OR);
                break;
            case "and":
                visit(boolExpn.getLeft());
                writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
                writeMemory(next_instruction_addr++, Machine.EQ);

                visit(boolExpn.getRight());
                writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
                writeMemory(next_instruction_addr++, Machine.EQ);

                writeMemory(next_instruction_addr++, Machine.OR);

                writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
                writeMemory(next_instruction_addr++, Machine.EQ);
                break;
            default:
                throw new InvalidASTException("Invalid boolean operation symbol: " + boolExpn.getOpSymbol());
        }
    }

    @Override
    public void visit(CompareExpn compareExpn) {
        visit(compareExpn.getLeft());
        visit(compareExpn.getRight());

        switch (compareExpn.getOpSymbol()) {
            case "<":
                writeMemory(next_instruction_addr++, Machine.LT);
                break;
            case ">":
                writeMemory(next_instruction_addr++, Machine.SWAP);
                writeMemory(next_instruction_addr++, Machine.LT);
                break;
            case "<=":
                writeMemory(next_instruction_addr++, Machine.SWAP);
                writeMemory(next_instruction_addr++, Machine.LT);
                writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
                writeMemory(next_instruction_addr++, Machine.EQ);
                break;
            case ">=":
                writeMemory(next_instruction_addr++, Machine.LT);
                writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
                writeMemory(next_instruction_addr++, Machine.EQ);
                break;
            default:
                throw new InvalidASTException("Invalid comparison operation symbol: " + compareExpn.getOpSymbol());
        }
    }

    @Override
    public void visit(ConditionalExpn conditionalExpn) {
        visit(conditionalExpn.getCondition());

        writeMemory(next_instruction_addr++, Machine.PUSH);

        short patch_false_addr = next_instruction_addr;
        writeMemory(next_instruction_addr++, Machine.UNDEFINED);

        writeMemory(next_instruction_addr++, Machine.BF);

        visit(conditionalExpn.getTrueValue());

        writeMemory(next_instruction_addr++, Machine.PUSH);

        short patch_end_addr = next_instruction_addr;
        writeMemory(next_instruction_addr++, Machine.UNDEFINED);

        writeMemory(next_instruction_addr++, Machine.BR);

        // Patch
        writeMemory(patch_false_addr, next_instruction_addr);

        visit(conditionalExpn.getFalseValue());

        // Patch
        writeMemory(patch_end_addr, next_instruction_addr);
    }

    @Override
    public void visit(EqualsExpn equalsExpn) {
        visit(equalsExpn.getLeft());
        visit(equalsExpn.getRight());

        switch (equalsExpn.getOpSymbol()) {
            case "=":
                writeMemory(next_instruction_addr++, Machine.EQ);
                break;
            case "not =":
                writeMemory(next_instruction_addr++, Machine.EQ);
                writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
                writeMemory(next_instruction_addr++, Machine.EQ);
                break;
            default:
                throw new InvalidASTException("Invalid equality operation symbol: " + equalsExpn.getOpSymbol());
        }
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
        int intValue = intConstExpn.getValue();
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
        visit(notExpn.getOperand());

        writeMemory(next_instruction_addr++, Machine.MACHINE_FALSE);
        writeMemory(next_instruction_addr++, Machine.EQ);
    }

    @Override
    public void visit(SubsExpn subsExpn) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(UnaryMinusExpn unaryMinusExpn) {
        visit(unaryMinusExpn.getOperand());

        writeMemory(next_instruction_addr++, Machine.NEG);
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
        visit(ifStmt.getCondition());

        writeMemory(next_instruction_addr++, Machine.PUSH);
        short patch_false_addr = next_instruction_addr;
        writeMemory(next_instruction_addr++, Machine.UNDEFINED);
        writeMemory(next_instruction_addr++, Machine.BF);

        visit(ifStmt.getWhenTrue());

        writeMemory(next_instruction_addr++, Machine.PUSH);
        short patch_end_addr = next_instruction_addr;
        writeMemory(next_instruction_addr++, Machine.UNDEFINED);
        writeMemory(next_instruction_addr++, Machine.BR);

        // Patch false address.
        writeMemory(patch_false_addr, next_instruction_addr);

        visit(ifStmt.getWhenFalse());

        // Patch end address.
        writeMemory(patch_end_addr, next_instruction_addr);
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
        short start_addr = next_instruction_addr;

        visit(repeatUntilStmt.getBody());

        visit(repeatUntilStmt.getExpn());

        writeMemory(next_instruction_addr++, Machine.PUSH);
        writeMemory(next_instruction_addr++, start_addr);
        writeMemory(next_instruction_addr++, Machine.BF);
    }

    @Override
    public void visit(ReturnStmt returnStmt) {
        throw new RuntimeException("NYI");
    }

    @Override
    public void visit(WhileDoStmt whileStmt) {
        short start_loop_addr = next_instruction_addr;

        visit(whileStmt.getExpn());

        writeMemory(next_instruction_addr++, Machine.PUSH);
        short patch_end_loop = next_instruction_addr;
        writeMemory(next_instruction_addr++, Machine.UNDEFINED);
        writeMemory(next_instruction_addr++, Machine.BF);

        visit(whileStmt.getBody());

        writeMemory(next_instruction_addr++, Machine.PUSH);
        writeMemory(next_instruction_addr++, start_loop_addr);
        writeMemory(next_instruction_addr++, Machine.BR);

        // Patch end loop address.
        writeMemory(patch_end_loop, next_instruction_addr);
    }

    @Override
    public void visit(WriteStmt writeStmt) {
        for (Printable item : writeStmt.getOutputs()) {
            if (item instanceof SkipConstExpn) {
                writeMemory(next_instruction_addr++, Machine.PUSH);
                writeMemory(next_instruction_addr++, (short) '\n');

                writeMemory(next_instruction_addr++, Machine.PRINTC);
            } else if (item instanceof TextConstExpn) {
                allocateTextConst(((TextConstExpn)item).getValue());

                writeMemory(next_instruction_addr++, Machine.PUSH);
                short patch_ret_addr = next_instruction_addr;
                writeMemory(next_instruction_addr++, Machine.UNDEFINED);

                writeMemory(next_instruction_addr++, Machine.PUSH);
                writeMemory(next_instruction_addr++, most_recent_textconst_addr);

                writeMemory(next_instruction_addr++, Machine.PUSH);
                writeMemory(next_instruction_addr++, print_string_procedure_addr);

                writeMemory(next_instruction_addr++, Machine.BR);

                // Patch return address
                writeMemory(patch_ret_addr, next_instruction_addr);
            } else if (item instanceof Expn) {
                visit((Expn) item);

                writeMemory(next_instruction_addr++, Machine.PRINTI);
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
     * Wrapper method to call Machine.writeMemory and die if we're OOM.
     */
    private void writeMemory(short addr, short val) {
        try {
            // If the instructions and the constants are about to overlap, we're all out of space.
            if (next_instruction_addr >= most_recent_textconst_addr) {
                throw new MemoryAddressException("OOM");
            }

            Machine.writeMemory(addr, val);
        } catch (MemoryAddressException e) {
            System.err.println("Generated code won't fit in machine memory. Exiting now.");
            System.exit(1);
        }
    }

    /**
     * Generate code for the print-string procedure.
     */
    private void generatePrintStringProcedure() {
        // Branch around the def'n.
        writeMemory(next_instruction_addr++, Machine.PUSH);
        short patch_end_of_def = next_instruction_addr;
        writeMemory(next_instruction_addr++, Machine.UNDEFINED);
        writeMemory(next_instruction_addr++, Machine.BR);

        // Remember the starting address.
        print_string_procedure_addr = next_instruction_addr;

        short start_addr = next_instruction_addr;
        writeMemory(next_instruction_addr++, Machine.DUP);          // Duplicate string address
        writeMemory(next_instruction_addr++, Machine.LOAD);         // Load char
        writeMemory(next_instruction_addr++, Machine.DUP);          // Duplicate char
        writeMemory(next_instruction_addr++, Machine.PUSH);
        short patch_end_of_loop = next_instruction_addr;
        writeMemory(next_instruction_addr++, Machine.UNDEFINED);
        writeMemory(next_instruction_addr++, Machine.BF);           // Exit loop if char is null
        writeMemory(next_instruction_addr++, Machine.PRINTC);       // Print char
        writeMemory(next_instruction_addr++, Machine.PUSH);
        writeMemory(next_instruction_addr++, (short) 1);
        writeMemory(next_instruction_addr++, Machine.ADD);          // Add 1 to string address
        writeMemory(next_instruction_addr++, Machine.PUSH);
        writeMemory(next_instruction_addr++, start_addr);
        writeMemory(next_instruction_addr++, Machine.BR);           // Go to start of loop

        // Patch end of loop address.
        writeMemory(patch_end_of_loop, next_instruction_addr);

        writeMemory(next_instruction_addr++, Machine.POP);          // Pop char
        writeMemory(next_instruction_addr++, Machine.POP);          // Pop string address
        writeMemory(next_instruction_addr++, Machine.BR);           // Go to return address

        // Patch end of def'n address.
        writeMemory(patch_end_of_def, next_instruction_addr);
    }

    /**
     * Allocate some space in the constant pool at the top of machine memory and store the given text there.
     */
    private void allocateTextConst(String text) {
        most_recent_textconst_addr -= text.length() + 1;

        short addr = most_recent_textconst_addr;
        for (char c : text.toCharArray()) {
            writeMemory(addr++, (short) c);
        }
        writeMemory(addr, (short) '\0');
    }
}
