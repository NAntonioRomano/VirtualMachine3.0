package mnemonic;

import components.VirtualMachine;
import operands.Operand;

public class OR extends Mnemonic2 {

    public OR(VirtualMachine vm) {
        super(vm, "OR");
    }

    @Override
    public void operate(Operand A, Operand B) throws Exception {

        int value = A.getValue() | B.getValue(); // Operación OR bit a bit
        A.setValue(value); // Almacena el resultado en A
        ModifyCC(value); // Modifica el CC de acuerdo al resultado
 

    }

}
