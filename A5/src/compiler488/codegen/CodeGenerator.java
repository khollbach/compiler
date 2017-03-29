package compiler488.codegen;

import compiler488.runtime.Machine;
import compiler488.runtime.MemoryAddressException;

/**
 * A class to take care of Machine details when generating code.
 *
 * Created by gianacop on 3/28/17.
 */
public class CodeGenerator {

    private short nextProgramAddr;
    private short nextConstPoolAddr;

    public CodeGenerator() {
        nextProgramAddr = 0;
        nextConstPoolAddr = Machine.memorySize - 1;
    }

    public short genCode(short... code) {
        for (short word : code) {
            if (nextProgramAddr > nextConstPoolAddr) {
                errNoMemory();
            } else {
                writeWord(nextProgramAddr, word);
                nextProgramAddr++;
            }
        }

        return nextProgramAddr;
    }

    public void patchCode(short addr, short word) {
        writeWord(addr, word);
    }

    public void patchCode(short addr) {
        writeWord(addr, nextProgramAddr);
    }

    public short getNextInstrAddr() {
        return nextProgramAddr;
    }

    public short getNextConstPoolAddr() {
        return nextConstPoolAddr;
    }


    public short genText(String text) {

        // add null terminator
        if (nextConstPoolAddr < nextProgramAddr) {
            errNoMemory();
        } else {
            writeWord(nextConstPoolAddr, (short) 0);
            nextConstPoolAddr--;
        }

        for (int i = text.length() - 1; i >= 0; i--) {
            if (nextConstPoolAddr < nextProgramAddr) {
                errNoMemory();
            } else {
                writeWord(nextConstPoolAddr, (short) text.charAt(i));
                nextConstPoolAddr--;
            }
        }

        return (short) (nextConstPoolAddr + 1);
    }

    private void writeWord(short addr, short word) {
        try {
            Machine.writeMemory(addr, word);
        } catch (MemoryAddressException e) {
            errNoMemory();
        }
    }

    private void errNoMemory() {
        System.err.println("Generated code won't fit in machine memory. Exiting now.");
        System.exit(1);
    }
}
