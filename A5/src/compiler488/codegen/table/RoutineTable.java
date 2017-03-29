package compiler488.codegen.table;

import java.util.*;

/**
 * Symbol table to manage routine addressing during codegen.
 *
 * Created by gianacop on 3/28/17.
 */
public class RoutineTable implements AddressLookup<RoutineTable.Address> {

    private Map<String, Stack<Address>> addressMap;
    private Stack<Collection<String>> scopeStack;

    public RoutineTable() {
        addressMap = new HashMap<>();
        scopeStack = new Stack<>();
    }

    public void createEntry(String id, short address, short lexicalLevel) {
        if (!addressMap.containsKey(id)) {
            addressMap.put(id, new Stack<>());
        }
        addressMap.get(id).push(new Address(address, lexicalLevel));
        scopeStack.peek().add(id);
    }

    @Override
    public Address getAddress(String id) {
        return addressMap.get(id).peek();
    }

    public void openScope() {
        scopeStack.push(new ArrayList<>());
    }

    public void closeScope() {
        for (String id : scopeStack.pop()) {
            removeEntry(id);
        }
    }

    private void removeEntry(String id) {
        addressMap.get(id).pop();
        if (addressMap.get(id).isEmpty()){
            addressMap.remove(id);
        }
    }

    public static class Address {
        public final short ENTRY_PT;
        public final short LL;

        Address(short address, short lexicalLevel) {
            ENTRY_PT = address;
            LL = lexicalLevel;
        }
    }
}

