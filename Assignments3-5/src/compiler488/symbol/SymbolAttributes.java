package compiler488.symbol;


import compiler488.symbol.td.TypeDescriptor;

/**
 * Created by gg on 24/02/17.
 */
public class SymbolAttributes {

    /* This symbol's scopeStack depth. */
    public final int depth;

    public final TypeDescriptor typeDescriptor;

    public SymbolAttributes(int depth, TypeDescriptor typeDescriptor){
        this.depth = depth;
        this.typeDescriptor = typeDescriptor;

    }


}
