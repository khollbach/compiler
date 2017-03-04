package compiler488.ast;

/**
 * This is a placeholder at the top of the Abstract Syntax Tree hierarchy. It is
 * a convenient place to add common behaviour.
 *
 * @author Dave Wortman, Marsha Chechik, Danny House
 */
public class AST {

    public final static String version = "Winter 2017";

    // All nodes in the AST should keep track of where they came from
    // in the source file.
    private int sourceCoordinateLine;
    private int sourceCoordinateColumn;

    public void setSourceCoordinateLine(int lineNumber) {
        sourceCoordinateLine = lineNumber;
    }

    public void setSourceCoordinateColumn(int columnNumber) {
        sourceCoordinateColumn = columnNumber;
    }

    public int getSourceCoordinateLine() {
        return sourceCoordinateLine;
    }

    public int getSourceCoordinateColumn() {
        return sourceCoordinateColumn;
    }

}
