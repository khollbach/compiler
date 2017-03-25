package compiler488.ast.decl;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.visitor.DeclarationVisitor;

import java.io.PrintStream;

/**
 * Holds the declaration of multiple elements.
 */
public class MultiDeclarations extends Declaration {
    /* The elements being declared */
    private ASTList<DeclarationPart> elements;

    public MultiDeclarations() {
        elements = new ASTList<DeclarationPart>();
    }

    /**
     * Returns a string that describes the array.
     */
    @Override
    public String toString() {
        return "var " + elements + " : " + type;
    }


    /**
     * Print the multiple declarations of the same type.
     *
     * @param out   Where to print the description.
     * @param depth How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        Indentable.printIndentOnLn(out, depth, this + "");
    }


    public ASTList<DeclarationPart> getElements() {
        return elements;
    }

    public void setElements(ASTList<DeclarationPart> elements) {
        this.elements = elements;
    }

    public void accept(DeclarationVisitor visitor) {
        visitor.visit(this);
    }
}
