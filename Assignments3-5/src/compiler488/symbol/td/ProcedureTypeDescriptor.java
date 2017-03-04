package compiler488.symbol.td;

import java.util.Collections;
import java.util.List;

/**
 * Created by gg on 27/02/17.
 */
public class ProcedureTypeDescriptor extends TypeDescriptor {

    /**
     * Represents the types of each of a procedure's parameters. Immutable.
     */
    public final List<ScalarTypeDescriptor> parameterTypes;

    ProcedureTypeDescriptor(List<ScalarTypeDescriptor> parameterTypes) {
        this.parameterTypes = Collections.unmodifiableList(parameterTypes);
    }
}
