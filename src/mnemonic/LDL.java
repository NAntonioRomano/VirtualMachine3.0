package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class LDL extends Mnemonic2 {

    public LDL(VirtualMachine vm) {
        super(vm, "LDL");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {

        int valueA = A.getValue() & 0xFFFF0000;
        int vlaueB = B.getValue() & 0x0000FFFF;

        A.setValue(valueA | vlaueB); 
    }

}

