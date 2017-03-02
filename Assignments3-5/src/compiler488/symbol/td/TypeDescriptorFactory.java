package compiler488.symbol.td;

import compiler488.ast.InvalidASTException;
import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.expn.BoolConstExpn;
import compiler488.ast.expn.ConstExpn;
import compiler488.ast.expn.IntConstExpn;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.Type;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gg on 27/02/17.
 */
public class TypeDescriptorFactory {



    public static TypeDescriptor typeOf(ConstExpn expn) {
        if (expn instanceof IntConstExpn) {
            return new IntegerTypeDescriptor();
        } else if (expn instanceof BoolConstExpn) {
            return new BooleanTypeDescriptor();
        } else {
            // We have a TextConstExpn
            // A correctly built AST should never contain a TextConstExpn
            // in a place where this method is invoked
            throw new InvalidASTException();
        }
    }


    public static ScalarTypeDescriptor create(ScalarDecl decl) {
        return createScalarTDFromType(decl.getType());
    }

    public static TypeDescriptor create(DeclarationPart declPart, Type type) {
        ScalarTypeDescriptor scalarTD = createScalarTDFromType(type);
        if (declPart instanceof ArrayDeclPart) {
            return new ArrayTypeDescriptor(scalarTD);
        } else {
            return scalarTD;
        }
    }

    public static TypeDescriptor create(RoutineDecl routineDecl) {
        // create TypeDescriptor for the return type
        ScalarTypeDescriptor returnType = null;
        if (routineDecl.getType() != null) {
            returnType = createScalarTDFromType(routineDecl.getType());
        }

        // create TypeDescriptors for the parameters
        List<ScalarTypeDescriptor> parameterTypes;
        if (routineDecl.getRoutineBody().getParameters() != null) {
            parameterTypes =
                    routineDecl.getRoutineBody().getParameters()
                            .stream()
                            .map(TypeDescriptorFactory::create)
                            .collect(Collectors.toList());
        } else {
            parameterTypes = Collections.emptyList();
        }

        if (returnType != null) {
            return new FunctionTypeDescriptor(returnType, parameterTypes);
        } else {
            return new ProcedureTypeDescriptor(parameterTypes);
        }
    }

    private static ScalarTypeDescriptor createScalarTDFromType(Type type) {
        if (type instanceof BooleanType) {
            return new BooleanTypeDescriptor();
        } else {
            return new IntegerTypeDescriptor();
        }
    }
}
