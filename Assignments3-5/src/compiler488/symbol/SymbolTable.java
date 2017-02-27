package compiler488.symbol;

import java.util.*;

/** SymbolAttributes Table
 *  This almost empty class is a framework for implementing
 *  a SymbolAttributes Table class for the CSC488S compiler
 *  
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  Tarang Marathe, George Gianacopoulos
 */

public class SymbolTable {
	
	/** String used by Main to print symbol table
         *  version information.
         */

	public final static String version = "Winter 2017" ;

	public HashMap<String, Stack<SymbolAttributes>> symbolTable;
	public Stack<List<String>> scopeStack;

	/** SymbolAttributes Table  constructor
         *  Create and initialize a symbol table 
	 */
	public SymbolTable(){
		this.symbolTable = new HashMap<String, Stack<SymbolAttributes>>();
		this.scopeStack = new Stack<>();
	}

	/**  Initialize - called once by semantic analysis  
	 *                at the start of  compilation     
	 *                May be unnecessary if constructor
 	 *                does all required initialization	
	 */
	public void Initialize() {
	
	   /**   Initialize the symbol table             
	    *	Any additional symbol table initialization
	    *  GOES HERE                                	
	    */
	   
	}

	/**  Finalize - called once by Semantics at the end of compilation
	 *              May be unnecessary 		
	 */
	public void Finalize(){
	
	  /**  Additional finalization code for the 
	   *  symbol table  class GOES HERE.
	   *  
	   */
	}
	

	/** The rest of SymbolAttributes Table
	 *  Data structures, public and private functions
 	 *  to implement the SymbolAttributes Table
	 *  GO HERE.				
	 */

	/**
	 * create new Scope object; push to stack with depth = current stack size
	 */
	private void openScope(){
		this.scopeStack.push(new ArrayList<>());
	}

	/**
	 * removes innermost scope from stack,
	 * removes symbols attributes within this scope from symbolTable
	 */
	private void closeScope(){
		List<String> innerScope = scopeStack.pop();
		for (String id : innerScope){
			Stack<SymbolAttributes> symbolStack = symbolTable.get(id);
			symbolStack.pop();
			if (symbolStack.isEmpty()){
				symbolTable.remove(id);
			}
		}
	}

	private boolean declaredLocally(String id){
	    List innerScope = scopeStack.peek();
	    return innerScope.contains(id);
    }

	/**
	 *
	 * @param id - symbol id
	 * @param attributes - an object containing all the attributed of the symbol
	 */
	private void enterSymbol(String id, SymbolAttributes attributes){

		if (!symbolTable.containsKey(id)){
			symbolTable.put(id, new Stack<>());
		}
		symbolTable.get(id).push(attributes);
		scopeStack.peek().add(id);
	}

	/**
	 *
	 * @param id - id string of a symbol
	 * @return returns attributes of the most local declaration of symbol id
	 */
	private SymbolAttributes retrieveSymbol(String id){
		return symbolTable.get(id).peek();
	}

}
