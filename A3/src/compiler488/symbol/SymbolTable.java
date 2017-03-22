package compiler488.symbol;

import java.util.*;

/**
 * SymbolAttributes Table
 * This almost empty class is a framework for implementing
 * a SymbolAttributes Table class for the CSC488S compiler
 * <p>
 * Each implementation can change/modify/delete this class
 * as they see fit.
 *
 * @author Tarang Marathe, George Gianacopoulos
 */

public class SymbolTable {

    /**
     * String used by Main to print symbol table
     * version information.
     */

    public final static String version = "Winter 2017";

    public HashMap<String, Stack<SymbolAttributes>> symbolTable;
    public Stack<List<String>> scopeStack;

    /**
     * SymbolAttributes Table  constructor
     * Create and initialize a symbol table
     */
    public SymbolTable() {
        this.symbolTable = new HashMap<>();
        this.scopeStack = new Stack<>();
    }

    /**
     * create new Scope object; push to stack with depth = current stack size
     */
    public void openScope() {
        this.scopeStack.push(new ArrayList<>());
    }

    /**
     * removes innermost scope from stack,
     * removes symbols attributes within this scope from symbolTable
     */
    public void closeScope() {
        List<String> innerScope = scopeStack.pop();
        for (String id : innerScope) {
            Stack<SymbolAttributes> symbolStack = symbolTable.get(id);
            symbolStack.pop();
            if (symbolStack.isEmpty()) {
                symbolTable.remove(id);
            }
        }
    }

    private boolean declaredLocally(String id) {
        List<String> innerScope = scopeStack.peek();
        return innerScope.contains(id);
    }

    /**
     * @param id         - symbol id
     * @param attributes - an object containing all the attributed of the symbol
     */
    public void enterSymbol(String id, SymbolAttributes attributes) throws RedeclarationException {

        if (declaredLocally(id)) {
            throw new RedeclarationException();
        }

        if (!symbolTable.containsKey(id)) {
            symbolTable.put(id, new Stack<>());
        }
        symbolTable.get(id).push(attributes);
        scopeStack.peek().add(id);
    }

    /**
     * @param id - id string of a symbol
     * @return returns attributes of the most local declaration of symbol id
     */
    public SymbolAttributes retrieveSymbol(String id) throws SymbolNotFoundException {
        if (!symbolTable.containsKey(id)) {
            throw new SymbolNotFoundException();
        }
        return symbolTable.get(id).peek();
    }


    /**
     * The Exception thrown if an attempt to enter a symbol
     * that is already declaredLocally is made.
     */
    public class RedeclarationException extends Exception {
    }

    public class SymbolNotFoundException extends Exception {
    }

}
