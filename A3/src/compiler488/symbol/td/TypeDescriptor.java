package compiler488.symbol.td;

/**
 * Class used to keep track of types in the compiler488 SymbolTable
 * <p>
 * Created by George Gianacopoulos on 24/02/17.
 */
public abstract class TypeDescriptor {

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
