package compiler488.visitor;

import compiler488.ast.expn.*;

/**
 * Created by gg on 01/03/17.
 */
public interface ExpressionVisitor {

    void visit(Expn expn);
    void visit(ArithExpn arithExpn);
    void visit(BoolConstExpn boolConst);
    void visit(BoolExpn boolExpn);
    void visit(CompareExpn compareExpn);
    void visit(ConditionalExpn conditionalExpn);
    void visit(EqualsExpn equalsExpn);
    void visit(FunctionCallExpn funcExpn);
    void visit(IdentExpn identExpn);
    void visit(IntConstExpn intConstExpn);
    void visit(NotExpn notExpn);
    void visit(SkipConstExpn newlineExpn);
    void visit(SubsExpn subsExpn);
    void visit(TextConstExpn textExpn);
    void visit(UnaryMinusExpn unaryMinusExpn);

}
