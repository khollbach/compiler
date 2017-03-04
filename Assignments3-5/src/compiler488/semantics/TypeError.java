package compiler488.semantics;

import compiler488.ast.expn.*;
import compiler488.ast.stmt.AssignStmt;
import compiler488.ast.stmt.IfStmt;

/**
 * Created by tarang on 2017-03-02.
 */
public class TypeError extends SemanticError{

    /*
        Error message format strings corresponding to various type errors
     */
    private static final String UNEXPECTED_NONBOOLEAN = "expected Boolean was passed: %s";
    private static final String UNEXPECTED_NONSCALAR = "unexpected non-scalar reference: %s";
    private static final String NON_INTEGER_INDEX = "array indexed with non integral expression: %s";
    private static final String NON_MATCHING_OPERANDS = "Expected matching operands was passed: %s, %s";
    private static final String NON_MATCHING_RESULT_EXPRESSIONS = "Expected matching result type for conditional "
    		+ "was passed: %s, %s";
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
        offendingNode = identExpn;
        errorMsg = String.format(UNEXPECTED_NONSCALAR, identExpn);
    }

    public TypeError(SubsExpn subsExpn, Expn ignored) {
        offendingNode = subsExpn;
        errorMsg = String.format(NON_INTEGER_INDEX, subsExpn);
    }

    public TypeError(ArithExpn arithExpn) {
        offendingNode = arithExpn;
        errorMsg = String.format(NON_INTEGER_OPERAND, arithExpn);
    }

    public TypeError(CompareExpn compareExpn) {
        offendingNode = compareExpn;
        errorMsg = String.format(NON_MATCHING_OPERANDS, 
        		compareExpn.getLeft().evalType(), compareExpn.getRight().evalType());
    }


    public TypeError(SubsExpn subsExpn) {
        offendingNode = subsExpn;
        errorMsg = String.format(INDEXED_NON_ARRAY, subsExpn.getVariable());
    }

    public TypeError(ConditionalExpn conditionalExpn, ExpnEvalType receivedType) {
		// TODO Auto-generated constructor stub
        offendingNode = conditionalExpn;
        errorMsg = String.format(UNEXPECTED_NONBOOLEAN, receivedType);

	}

    public TypeError(ConditionalExpn conditionalExpn, ExpnEvalType receivedType1, ExpnEvalType receivedType2) {
        offendingNode = conditionalExpn;
        errorMsg = String.format(NON_MATCHING_RESULT_EXPRESSIONS,
        		conditionalExpn.getTrueValue().evalType(), receivedType1, receivedType2);
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
