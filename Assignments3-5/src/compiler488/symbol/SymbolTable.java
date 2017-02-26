package compiler488.symbol;

import java.util.Set;
import java.util.Stack;
import java.util.HashMap;

/** SymbolAttributes Table
 *  This almost empty class is a framework for implementing
 *  a SymbolAttributes Table class for the CSC488S compiler
 *  
 *  Each implementation can change/modify/delete this class
 *  as they see fit.
 *
 *  @author  Tarang Marathe
 */

public class SymbolTable {
	
	/** String used by Main to print symbol table
         *  version information.
         */

	public final static String version = "Winter 2017" ;

	public HashMap<String, Stack<SymbolAttributes>> symbolTable;
	public Stack<Scope> scopeStack;

	/** SymbolAttributes Table  constructor
         *  Create and initialize a symbol table 
	 */
	public SymbolTable(){
		this.symbolTable = new HashMap<String, Stack<SymbolAttributes>>();
		this.scopeStack = new Stack<Scope>();
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

	// create new Scope object; push to stack with depth = current stack size
	private void openScope(){
		this.scopeStack.push(new Scope(scopeStack.size()+1));
	}

	private void closeScope(){
		Scope innerScope = scopeStack.pop();
		Set<String> idStrings = innerScope.getIdStrings();
		for (String id : idStrings){
			Stack<SymbolAttributes> symbolStack = symbolTable.get(id);
			symbolStack.pop();
			if (symbolStack.isEmpty()){
				symbolTable.remove(id);
			}
		}
	}

	private void enterSymbol(String id, SymbolAttributes attributes){

		if (!symbolTable.containsKey(id)){
			symbolTable.put(id, new Stack<>());
		}
		symbolTable.get(id).push(attributes);
		scopeStack.peek().addIdString(id);
	}

	private SymbolAttributes retrieveSymbol(String id){
		return symbolTable.get(id).peek();
	}

}
