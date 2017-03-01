package compiler488.visitor;

import compiler488.ast.AST;

/**
 * Created by gg on 01/03/17.
 */
public interface Visitor {

    void visit(AST element);
}
