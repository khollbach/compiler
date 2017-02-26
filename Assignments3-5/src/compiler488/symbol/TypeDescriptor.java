package compiler488.symbol;

import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.RoutineDecl;
import compiler488.ast.decl.ScalarDecl;
import compiler488.ast.type.BooleanType;
import compiler488.ast.type.Type;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class used to keep track of types in the compiler488 SymbolTable
 *
 * Created by George Gianacopoulos on 24/02/17.
 */
abstract class TypeDescriptor {

    /*
    * TODO:
    *   - factory functions for creating TypeDescriptors from:
    *       > ScalarDeclaration
    *       > DeclarationPart + Type
    *       > RoutineDecl
    *
    *     and maybe the following???
    *       > BoolConstExpn
    *       > IntConstExpn
    *       > BinaryExpn (types of required operands? except '=' could have either type...)
    */

    static ScalarTypeDescriptor create(ScalarDecl decl) {
        return createScalarTDFromType(decl.getType());
    }

    static TypeDescriptor create(DeclarationPart declPart, Type type) {
        ScalarTypeDescriptor scalarTD = createScalarTDFromType(type);
        if (declPart instanceof ArrayDeclPart) {
            return new ArrayTypeDescriptor(scalarTD);
        } else {
            return scalarTD;
        }
    }

    static TypeDescriptor create(RoutineDecl routineDecl) {
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
                    .map(TypeDescriptor::create)
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

    private static abstract class ScalarTypeDescriptor extends TypeDescriptor {}

    private static class IntegerTypeDescriptor extends ScalarTypeDescriptor {}

    private static class BooleanTypeDescriptor extends ScalarTypeDescriptor {}

    private static class ArrayTypeDescriptor extends TypeDescriptor {

        /** Represents the type of an array's elements. */
        final ScalarTypeDescriptor elementType;

        ArrayTypeDescriptor(ScalarTypeDescriptor elementType) {
            this.elementType = elementType;
        }
    }

    private static class FunctionTypeDescriptor extends TypeDescriptor {

        /** Represents a function's return typeDescriptor. */
        final ScalarTypeDescriptor returnType;

        /** Represents the types of each of a function's parameters. Immutable.*/
        final List<ScalarTypeDescriptor> parameterTypes;

        FunctionTypeDescriptor(ScalarTypeDescriptor returnType,
                               List<ScalarTypeDescriptor> parameterTypes){
            this.returnType = returnType;
            this.parameterTypes = Collections.unmodifiableList(parameterTypes);
        }
    }

    private static class ProcedureTypeDescriptor extends TypeDescriptor {

        /**
         * Represents the types of each of a procedure's parameters. Immutable.
         */
        final List<ScalarTypeDescriptor> parameterTypes;

        ProcedureTypeDescriptor(List<ScalarTypeDescriptor> parameterTypes) {
            this.parameterTypes = Collections.unmodifiableList(parameterTypes);
        }
    }

    @Override
    /**
     * Compares TypeDescriptors for equality.
     */
    public boolean equals(Object that) {
        if (that instanceof TypeDescriptor) {
            if (this instanceof ScalarTypeDescriptor) {
                // check if both are of the same scalar type
                return this.getClass().equals(that.getClass());

            } else if (this instanceof ArrayTypeDescriptor
                    && that instanceof ArrayTypeDescriptor) {
                // check if both array type descriptors have the same
                // element type
                ArrayTypeDescriptor atThis = (ArrayTypeDescriptor) this;
                ArrayTypeDescriptor atThat = (ArrayTypeDescriptor) that;
                return atThis.elementType.equals(atThat.elementType);

            } else if (this instanceof FunctionTypeDescriptor
                    && that instanceof FunctionTypeDescriptor) {
                // check if both function type descriptors have the same
                // return type and parameter types
                FunctionTypeDescriptor ftThis = (FunctionTypeDescriptor) this;
                FunctionTypeDescriptor ftThat = (FunctionTypeDescriptor) that;
                boolean sameReturnTypes =
                        ftThis.returnType.equals(ftThat.returnType);
                boolean sameParameterTypes =
                        ftThis.parameterTypes.equals(ftThat.parameterTypes);

                return sameParameterTypes && sameReturnTypes;
            } else if (this instanceof ProcedureTypeDescriptor
                    && that instanceof ProcedureTypeDescriptor) {
                // check if both procedure type descriptors have the same signature
                ProcedureTypeDescriptor ptThis = (ProcedureTypeDescriptor) this;
                ProcedureTypeDescriptor ptThat = (ProcedureTypeDescriptor) that;

                return ptThis.parameterTypes.equals(ptThat.parameterTypes);
            }
        }

        return false;
    }
}
