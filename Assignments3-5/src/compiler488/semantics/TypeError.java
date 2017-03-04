package compiler488.semantics;

import compiler488.ast.expn.ArithExpn;
import compiler488.ast.expn.CompareExpn;
import compiler488.ast.expn.ConditionalExpn;
import compiler488.ast.expn.Expn;
import compiler488.ast.expn.IdentExpn;
import compiler488.ast.expn.SubsExpn;

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
    private static final String NON_INTEGER_OPERAND = "arithmetic expression was passed non-integer type: %s";
    private static final String NON_MATCHING_OPERANDS = "Expected matching operands was passed: %s, %s";
    private static final String NON_MATCHING_RESULT_EXPRESSIONS = "Expected matching result type for conditional "
    		+ "was passed: %s, %s";
    private static final String INDEXED_NON_ARRAY = "indexed non-array reference: %s";

    /**
     * This TypeError's error message
     */
    private String errorMsg;

    public TypeError(IdentExpn identExpn) {
        offendingNode = identExpn;
        errorMsg = String.format(UNEXPECTED_NONSCALAR, identExpn.getIdent());
    }

    public TypeError(SubsExpn subsExpn, Expn operand) {
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

	@Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
