package compiler488.codegen.table;

/**
 * Abstract interface for looking up addresses of symbols.
 *
 * Created by gianacop on 3/25/17.
 */
public interface AddressLookup<A> {

    /**
     * Returns the address corresponding to the given identifier.
     *
     * @param id The identifier whose corresponding address will be retrieved.
     * @return The address of id.
     */
    A getAddress(String id);
}
