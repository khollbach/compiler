package compiler488.semantics;

import compiler488.ast.decl.ArrayDeclPart;

/**
 * Created by gianacop on 3/3/17.
 */
public class ArrayDeclError extends SemanticError {

    private static final String BAD_ARRAY_BOUNDS = "array lower bound is greater than upper bound: %s";

    private String errorMsg;

    public ArrayDeclError(ArrayDeclPart arrayDeclPart) {
        errorMsg = String.format(BAD_ARRAY_BOUNDS, arrayDeclPart);
        offendingNode = arrayDeclPart;
    }
}
