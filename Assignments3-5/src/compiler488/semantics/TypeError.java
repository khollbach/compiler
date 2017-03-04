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
    private static final String NON_BOOLEAN_CONDITION = "non-boolean expression as condition to if statement: %s";
    private static final String DIFFERING_TYPES = "value assigned to variable of differing type: %s";
    private static final String ASSIGN_TO_PARAM = "assignment to parameter reference: %s";

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
        offendingNode = ifStmt;
        errorMsg = String.format(NON_BOOLEAN_CONDITION, ifStmt.getCondition());
    }

    public TypeError(AssignStmt assignStmt) {
        offendingNode = assignStmt;
        errorMsg = String.format(DIFFERING_TYPES, assignStmt.getLval());
    }

    public TypeError(AssignStmt assignStmt, boolean isParameter) {
        offendingNode = assignStmt;
        errorMsg = String.format(ASSIGN_TO_PARAM, assignStmt.getLval());
    }

    @Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
