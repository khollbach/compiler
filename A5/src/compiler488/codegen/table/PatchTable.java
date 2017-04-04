package compiler488.codegen.table;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * A class used to store addresses to patch.
 *
 * Created by gg on 03/04/17.
 */
public class PatchTable {

    private List<Collection<Short>> patchLevels;

    public PatchTable() {
        patchLevels = new LinkedList<>();
    }

    public void pushLevel() {
        patchLevels.add(new HashSet<>());
    }

    public Collection<Short> popLevel() {
        return patchLevels.remove(patchLevels.size() - 1);
    }

    public void addPatch(short addr) {
        addPatch(addr, 1);
    }

    public void addPatch(short addr, int level) {
        patchLevels.get(patchLevels.size() - level).add(addr);
    }
}
