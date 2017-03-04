package compiler488.semantics;

import compiler488.ast.stmt.ExitStmt;

/**
 * Created by gianacop on 3/3/17.
 */
public class ExitLevelError extends SemanticError {

    private static final String EXIT_LEVEL_TOO_LARGE = "attempt to exit from %d nested loops with actual nesting: %d";

    private String errorMsg;

    public ExitLevelError(ExitStmt exitStmt, int actualDepth) {
        errorMsg = String.format(EXIT_LEVEL_TOO_LARGE, exitStmt.getLevel(), actualDepth);
        offendingNode = exitStmt;
    }

    @Override
    public String toString() {
        return super.toString() + errorMsg;
    }
}
