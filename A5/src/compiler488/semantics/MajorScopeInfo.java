package compiler488.semantics;

/**
 * Created by hollbac1 on 3/3/17.
 */
public class MajorScopeInfo {
    private ScopeType scopeType;
    private ExpnEvalType returnType;
    private int currentLoopDepth = 0;

    public MajorScopeInfo(ScopeType scopeType) {
        this.scopeType = scopeType;
    }

    public MajorScopeInfo(ScopeType scopeType, ExpnEvalType returnType) {
        this.scopeType = scopeType;
        this.returnType = returnType;
    }

    public ScopeType getScopeType() {
        return scopeType;
    }

    public ExpnEvalType getReturnType() {
        return returnType;
    }

    public int getCurrentLoopDepth() {
        return currentLoopDepth;
    }

    public void incrementCurrentLoopDepth() {
        this.currentLoopDepth++;
    }

    public void decrementCurrentLoopDepth() {
        this.currentLoopDepth--;
    }

    public enum ScopeType {
        PROGRAM,
        PROCEDURE,
        FUNCTION
    }
}
