package mnemonic;

import components.VirtualMachine;
import operands.*;

public class LDH extends Mnemonic2 {

    public LDH(VirtualMachine vm) {
        super(vm, "LDH");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {

        int valueA = A.getValue() & 0x0000FFFF;
        int vlaueB = B.getValue() & 0x0000FFFF;
        vlaueB = vlaueB << 16;

        A.setValue(valueA | vlaueB); 


    }

}
