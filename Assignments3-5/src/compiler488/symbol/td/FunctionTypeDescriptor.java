package compiler488.symbol.td;

import java.util.Collections;
import java.util.List;

/**
 * Created by gg on 27/02/17.
 */
public class FunctionTypeDescriptor extends TypeDescriptor {

    /**
     * Represents a function's return typeDescriptor.
     */
    final ScalarTypeDescriptor returnType;

    /**
     * Represents the types of each of a function's parameters. Immutable.
     */
    final List<ScalarTypeDescriptor> parameterTypes;

    FunctionTypeDescriptor(ScalarTypeDescriptor returnType,
                           List<ScalarTypeDescriptor> parameterTypes) {
        this.returnType = returnType;
        this.parameterTypes = Collections.unmodifiableList(parameterTypes);
    }
}
