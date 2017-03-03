package compiler488.symbol;


import compiler488.symbol.td.TypeDescriptor;

/**
 * Created by gg on 24/02/17.
 */
public class SymbolAttributes {

    /* True if this is the SymbolAttributes of a parameter for a procedure or function. */
    public final boolean isParameter;

    public final TypeDescriptor typeDescriptor;

    public SymbolAttributes(boolean isParameter, TypeDescriptor typeDescriptor){
        this.isParameter = isParameter;
        this.typeDescriptor = typeDescriptor;

    }


}
