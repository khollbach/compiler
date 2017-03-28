package compiler488.codegen.table;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Symbol table to manage routine addressing during codegen.
 *
 * Created by gianacop on 3/28/17.
 */
public class RoutineTable implements AddressLookup<Short> {

    private Map<String, Stack<Short>> addressMap;
    private Stack<Collection<String>> scopeStack;

    public RoutineTable() {
        addressMap = new HashMap<>();
        scopeStack = new Stack<>();
    }

    public void createEntry(String id, short address) {
        if (!addressMap.containsKey(id)) {
            addressMap.put(id, new Stack<>());
        }
        addressMap.get(id).push(address);
        scopeStack.peek().add(id);
    }

    @Override
    public Short getAddress(String id) {
        return addressMap.get(id).peek();
    }

    public void openScope() {
        scopeStack.push(new Stack<>());
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
}

