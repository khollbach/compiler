package compiler488.symbol;

import java.util.Collections;
import java.util.List;

/**
 * Created by gg on 24/02/17.
 */
abstract class TypeDescriptor {

    private abstract class AtomicTypeDescriptor extends TypeDescriptor {}

    private class IntegerTypeDescriptor extends AtomicTypeDescriptor {}

    private class BooleanTypeDescriptor extends AtomicTypeDescriptor {}


    private class ArrayTypeDescriptor<E extends AtomicTypeDescriptor> extends TypeDescriptor {}

    private class FunctionTypeDescriptor extends TypeDescriptor {

        /** Represents a function's return typeDescriptor. */
        final AtomicTypeDescriptor returnType;

        /** Represents the types of each of a function's parameters. Immutable.*/
        final List<AtomicTypeDescriptor> parameterTypes;

        FunctionTypeDescriptor(AtomicTypeDescriptor returnType, List<AtomicTypeDescriptor> parameterTypes){
            this.returnType = returnType;
            this.parameterTypes = Collections.unmodifiableList(parameterTypes);
        }
    }

    private class ProcedureTypeDescriptor extends TypeDescriptor {

        /**
         * Represents the types of each of a procedure's parameters. Immutable.
         */
        final List<AtomicTypeDescriptor> parameterTypes;

        ProcedureTypeDescriptor(List<AtomicTypeDescriptor> parameterTypes) {
            this.parameterTypes = Collections.unmodifiableList(parameterTypes);
        }
    }
}
