package compiler488.ast.stmt;

import compiler488.ast.ASTList;
import compiler488.ast.Indentable;
import compiler488.ast.decl.Declaration;
import compiler488.visitor.StatementVisitor;

import java.io.PrintStream;

/**
 * Represents the declarations and instructions of a scope construct.
 */
public class Scope extends Stmt {
    private ASTList<Declaration> declarations; // The declarations at the top.

    private ASTList<Stmt> statements; // The statements to execute.

    private boolean isMajor;

    private short allocationSize;

    public short getAllocationSize() {
        return allocationSize;
    }

    public void setAllocationSize(short allocationSize) {
        this.allocationSize = allocationSize;
    }

    public Scope() {
        declarations = new ASTList<Declaration>();
        statements = new ASTList<Stmt>();
        isMajor = false;
    }

    /**
     * Print a description of the <b>scope</b> construct.
     *
     * @param out   Where to print the description.
     * @param depth How much indentation to use while printing.
     */
    @Override
    public void printOn(PrintStream out, int depth) {
        //Indentable.printIndentOnLn(out, depth, "Scope");
        //Indentable.printIndentOnLn(out, depth, "declarations");
        //declarations.printOnSeperateLines(out, depth + 1);
        //Indentable.printIndentOnLn(out, depth, "statements");
        //statements.printOnSeperateLines(out, depth + 1);
        //Indentable.printIndentOnLn(out, depth, "End Scope");

        Indentable.printIndentOnLn(out, depth, "{");
        declarations.printOnSeperateLines(out, depth + 1);
        statements.printOnSeperateLines(out, depth + 1);
        Indentable.printIndentOnLn(out, depth, "}");
    }

    public ASTList<Declaration> getDeclarations() {
        return declarations;
    }

    public ASTList<Stmt> getStatements() {
        return statements;
    }

    public void setDeclarations(ASTList<Declaration> declarations) {
        this.declarations = declarations;
    }

    public void setStatements(ASTList<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public void accept(StatementVisitor visitor) {
        visitor.visit(this);
    }

    public boolean isMajor() {
        return isMajor;
    }

    public void setIsMajor(boolean isMajor) {
        this.isMajor = isMajor;
    }
}
