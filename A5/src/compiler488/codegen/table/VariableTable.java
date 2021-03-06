package compiler488.codegen.table;

import compiler488.ast.decl.ArrayDeclPart;
import compiler488.ast.decl.DeclarationPart;
import compiler488.ast.decl.ScalarDecl;

import java.util.*;

/**
 * A symbol table for keeping track of variable addressing.
 *
 * Created by gianacop on 3/25/17.
 */
public class VariableTable implements AddressLookup<VariableTable.Address> {

    private Map<String, Stack<Address>> addressMap;
    private Stack<Scope> scopeStack;

    private short lexicalLevel;

    public VariableTable() {
        addressMap = new HashMap<>();
        scopeStack = new Stack<>();
        lexicalLevel = -1;
    }

    @Override
    public Address getAddress(String id) {
        return addressMap.get(id).peek();
    }

    /**
     * Creates an entry for the given decl.
     *
     * @param decl the ScalarDecl to create an entry for.
     */
    public void createEntry(ScalarDecl decl) {
        createEntry(decl.getName(), (short) 1);
    }

    /**
     * Creates an entry for the given decl.
     *
     * @param decl the DeclPart to create an entry for.
     */
    public void createEntry(DeclarationPart decl) {
        if (decl instanceof ArrayDeclPart) {
            createEntry(
                    decl.getName(),
                    ((ArrayDeclPart) decl).getSize().shortValue(),
                    ((ArrayDeclPart) decl).getLowerBoundary().shortValue()
            );
        } else {
            createEntry(decl.getName(), (short) 1);
        }
    }

    private void createEntry(String id, short allocationSize, short arrayLowerBound) {
        // Enter the symbol into the addressMap
        short offset = scopeStack.peek().getOffset();
        Address entryAddress = new Address(lexicalLevel, offset, arrayLowerBound);
        if (!addressMap.containsKey(id)) {
            addressMap.put(id, new Stack<>());
        }
        addressMap.get(id).push(entryAddress);

        // Add this variable to the innermost open scope's declared variables
        //   and retrieve its offset.
        scopeStack.peek().addVariable(id, allocationSize);
    }

    private void createEntry(String id, short allocationSize) {
        createEntry(id, allocationSize, (short) 0);
    }

    public short getAllocationSize() {
        return scopeStack.peek().getOffset();
    }

    public void openScope(boolean isMajorScope) {
        if (isMajorScope) {
            lexicalLevel++;
            scopeStack.push(new Scope(true, (short) 0));
        } else {
            short offset = scopeStack.peek().getOffset();
            scopeStack.push(new Scope(false, offset));
        }
    }

    public void closeScope(){
        Scope closedScope = scopeStack.pop();
        if (closedScope.isMajorScope) {
            lexicalLevel--;
        }

        for (String id : closedScope.getIds()) {
            removeEntry(id);
        }
    }

    private void removeEntry(String id) {
        addressMap.get(id).pop();
        if (addressMap.get(id).isEmpty()) {
            addressMap.remove(id);
        }
    }

    public short getLexicalLevel() {
        return lexicalLevel;
    }


    /**
     * An immutable pair of shorts representing a variable address.
     */
    public static class Address {

        public final short LL;

        public final short ON;

        public final short LOWER_BOUND;

        Address(short lexicalLevel, short offset, short arrayLowerBound) {
            this.LL = lexicalLevel;
            this.ON = offset;
            this.LOWER_BOUND = arrayLowerBound;
        }
    }

    private static class Scope {
        /** true if this Scope is a major scope. */
        final boolean isMajorScope;

        private short offset;

        private Collection<String> ids;

        Scope(boolean isMajorScope, short offset) {
            this.isMajorScope = isMajorScope;
            this.offset = offset;
            this.ids = new ArrayList<>();
        }

        /**
         * Adds a variable to this scope.
         *
         * @param id the id of the variable being added
         * @param allocationSize the size of the allocation for the variable being added
         * @return the offset of the added variable.
         */
        void addVariable(String id, short allocationSize) {
            ids.add(id);
            offset += allocationSize;
        }

        /**
         * Returns a Collections of the ids of symbols declared in this scope.
         * @return the ids of symbols declared in this scope
         */
        Collection<String> getIds() {
            return ids;
        }

        public short getOffset() {
            return offset;
        }
    }
}
