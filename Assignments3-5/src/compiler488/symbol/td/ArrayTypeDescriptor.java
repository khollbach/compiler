package compiler488.symbol.td;

/**
 * Created by gg on 27/02/17.
 */
class ArrayTypeDescriptor extends TypeDescriptor {

    /**
     * Represents the type of an array's elements.
     */
    final ScalarTypeDescriptor elementType;

    ArrayTypeDescriptor(ScalarTypeDescriptor elementType) {
        this.elementType = elementType;
    }
}
