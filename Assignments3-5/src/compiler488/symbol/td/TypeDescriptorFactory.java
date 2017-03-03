package compiler488.symbol.td;

import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.Type;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by gg on 27/02/17.
 */
public class TypeDescriptorFactory {

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
