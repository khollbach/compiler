package compiler488.semantics;

import compiler488.ast.expn.*;

/**
 * Created by tarang on 2017-03-02.
 */
public class TypeError extends SemanticError{

    /*
        Error message format strings corresponding to various type errors
     */
    private static final String UNEXPECTED_NONSCALAR = "unexpected non-scalar reference: %s";
    private static final String NON_INTEGER_INDEX = "array indexed with non integral expression: %s";
    private static final String NON_INTEGER_OPERAND = "arithmetic expression was passed non-integer reference: %s";
    private static final String INDEXED_NON_ARRAY = "indexed non-array reference: %s";
    private static final String NON_BOOLEAN_OPERAND = "boolean expression was passed non-boolean reference: %s";
    private static final String NON_BOOLEAN_CONDITION = "non-boolean expression as condition to if statement: %s"


    /**
     * This TypeError's error message
     */
    private String errorMsg;

    public TypeError(IdentExpn identExpn) {

    }

    public TypeError(SubsExpn subsExpn, Expn operand) {
        offendingNode = subsExpn;
        errorMsg = String.format(NON_INTEGER_INDEX, subsExpn);
    }

    public TypeError(ArithExpn arithExpn) {
        offendingNode = arithExpn;
        errorMsg = String.format(NON_INTEGER_OPERAND, arithExpn);
    }

    public TypeError(SubsExpn subsExpn) {
        offendingNode = subsExpn;
        errorMsg = String.format(INDEXED_NON_ARRAY, subsExpn.getVariable());
    }

    public TypeError(BoolExpn boolExpn) {
        offendingNode = boolExpn;
        errorMsg = String.format(NON_BOOLEAN_OPERAND, boolExpn);
    }

    public TypeError(IfStmt ifStmt) {
        offendingNode = ifStmt
        errorMsg = String.format(NON_BOOLEAN_CONDITION, ifStmt.condition);
    }

    @Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
