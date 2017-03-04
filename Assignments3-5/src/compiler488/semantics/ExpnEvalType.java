package compiler488.semantics;

/**
 * The types that an expression may evaluate to.
 *
 * Created by gg on 03/03/17.
 */
public enum ExpnEvalType {

    /**
     * The type of variable references and function calls when the symbol referenced is undeclared.
     */
    UNDEFINED,

    /**
     * The type that arithmetic expressions evaluate to.
     */
    INTEGER,

    /**
     * The type that boolean expressions evaluate to.
     */
    BOOLEAN

}
