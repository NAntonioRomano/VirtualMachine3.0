package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class CMP extends Mnemonic2 {

    public CMP(VirtualMachine vm) {
        super(vm, "CMP");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {
        int value;
        value = A.getValue() - B.getValue();
        ModifyCC(value); // Modifica el CC de acuerdo al resultado de la resta
    }
}
