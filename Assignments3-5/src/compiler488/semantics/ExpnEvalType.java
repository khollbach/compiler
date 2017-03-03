package compiler488.semantics;

/**
 * The types that an expression may evaluate to.
 *
 * Created by gg on 03/03/17.
 */
public enum ExpnEvalType {
    /**
     * The type that an erroneous expression evaluates to.
     */
    ERROR,

    /**
     * The type that valid arithmetic expressions evaluate to.
     */
    INTEGER,

    /**
     * The type that ... TODO
     */
    BOOLEAN,

    TEXT

}
