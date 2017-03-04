package compiler488.semantics;

/**
 * Created by hollbac1 on 3/3/17.
 */
public class MajorScopeInfo {
    private int currentLoopDepth = 0;

    public int getCurrentLoopDepth() {
        return currentLoopDepth;
    }

    public void incrementCurrentLoopDepth() {
        this.currentLoopDepth++;
    }

    public void decrementCurrentLoopDepth() {
        this.currentLoopDepth--;
    }
}
