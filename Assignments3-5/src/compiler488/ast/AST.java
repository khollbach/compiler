package compiler488.ast;

import compiler488.visitor.Visitor;

/**
 * This is a placeholder at the top of the Abstract Syntax Tree hierarchy. It is
 * a convenient place to add common behaviour.
 * @author  Dave Wortman, Marsha Chechik, Danny House
 */
public class AST {

	public final static String version = "Winter 2017";

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
