package compiler488.semantics;

import compiler488.ast.expn.*;
import compiler488.ast.stmt.*;
import compiler488.symbol.td.ScalarTypeDescriptor;

import java.util.List;

/**
 * Created by tarang on 2017-03-02.
 */
public class TypeError extends SemanticError {

    /*
        Error message format strings corresponding to various type errors
     */
    private static final String UNEXPECTED_NONSCALAR = "unexpected non-scalar reference: %s";
    private static final String NON_INTEGER_INDEX = "array indexed with non integral expression: %s";
    private static final String NON_INTEGER_OPERAND = "arithmetic expression was passed non-integer reference: %s";
    private static final String INDEXED_NON_ARRAY = "indexed non-array reference: %s";
    private static final String NON_BOOLEAN_OPERAND = "boolean expression was passed non-boolean reference: %s";
    private static final String NON_BOOLEAN_CONDITION = "non-boolean expression as condition: %s";
    private static final String DIFFERING_TYPES = "value assigned to variable of differing type: %s";
    private static final String ASSIGN_TO_PARAM = "assignment to parameter reference: %s";
    private static final String NON_BOOLEAN_EXIT_COND = "exit statement has non-boolean condition: %s";
    private static final String NON_INTEGER_COMPARE = "compare expression was passed a non-integer reference: %s";
    private static final String RESULT_TYPE_MISMATCH = "condition statement result types are not the same: %s";
    private static final String EQUAL_TYPE_MISMATCH = "equality condition was passed two different types: %s";
    private static final String NON_INTEGER_UNARY_MINUS = "unary minus applied to non-integer expression: %s";
    private static final String ROUTINE_PARAM_TYPE_MISMATCH = "argument types don't match declared parameter types: %s";
    private static final String WRONG_RETURN_TYPE = "expected return type and actual do not match: %s, %s";

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

    public TypeError(ExitStmt exitStmt) {
        offendingNode = exitStmt;
        errorMsg = String.format(NON_BOOLEAN_EXIT_COND, exitStmt.getExpn());
    }

    public TypeError(CompareExpn compareExpn) {
        offendingNode = compareExpn;
        errorMsg = String.format(NON_INTEGER_COMPARE, compareExpn);
    }

    public TypeError(ConditionalExpn conditionalExpn) {
        offendingNode = conditionalExpn;
        errorMsg = String.format(NON_BOOLEAN_CONDITION, conditionalExpn);
    }

    public TypeError(ConditionalExpn conditionalExpn, Expn falseValue, Expn trueValue) {
        offendingNode = conditionalExpn;
        errorMsg = String.format(RESULT_TYPE_MISMATCH, conditionalExpn);
    }

    public TypeError(EqualsExpn equalsExpn) {
        offendingNode = equalsExpn;
        errorMsg = String.format(EQUAL_TYPE_MISMATCH, equalsExpn);
    }

    public TypeError(LoopingStmt whileDoStmt) {
        offendingNode = whileDoStmt;
        errorMsg = String.format(NON_BOOLEAN_CONDITION, whileDoStmt);
    }

    public TypeError(UnaryMinusExpn unaryMinusExpn) {
        offendingNode = unaryMinusExpn;
        errorMsg = String.format(NON_INTEGER_UNARY_MINUS, unaryMinusExpn);
    }

    public TypeError(ProcedureCallStmt procCall) {
        offendingNode = procCall;
        errorMsg = String.format(ROUTINE_PARAM_TYPE_MISMATCH, procCall);
    }

    public TypeError(FunctionCallExpn funcExpn) {
        offendingNode = funcExpn;
        errorMsg = String.format(ROUTINE_PARAM_TYPE_MISMATCH, funcExpn);
    }

    public TypeError(ReturnStmt returnStmt, ExpnEvalType expectedType, ExpnEvalType actualType) {
        offendingNode = returnStmt;
        errorMsg = String.format(WRONG_RETURN_TYPE, expectedType, actualType);
    }

    @Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
