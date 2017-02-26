package compiler488.symbol;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tarang Marathe on 2017-02-25.
 */
public class Scope {

    public final int depth;
    private final Set<String> idStrings;

    public Scope(int depth){
        this.depth = depth;
        this.idStrings = new HashSet<String>();
    }

    // add id strings of vars contained in this scope
    public Boolean addIdString(String e){
        if (this.idStrings.contains(e)){
            return false;
        }
        this.idStrings.add(e);
        return true;
    }

    // return the set of vars contained in the scope
    public Set<String> getIdStrings(){
        return this.idStrings;
    }

}
